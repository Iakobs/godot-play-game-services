package com.jacobibanez.plugin.android.godotplaygameservices.snapshots

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.SnapshotsClient
import com.google.android.gms.games.SnapshotsClient.EXTRA_SNAPSHOT_METADATA
import com.google.android.gms.games.SnapshotsClient.RESOLUTION_POLICY_HIGHEST_PROGRESS
import com.google.android.gms.games.SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED
import com.google.android.gms.games.SnapshotsClient.SnapshotConflict
import com.google.android.gms.games.snapshot.SnapshotMetadata
import com.google.android.gms.games.snapshot.SnapshotMetadataChange
import com.google.gson.Gson
import com.jacobibanez.plugin.android.godotplaygameservices.BuildConfig.GODOT_PLUGIN_NAME
import com.jacobibanez.plugin.android.godotplaygameservices.signals.SnapshotSignals.conflictEmitted
import com.jacobibanez.plugin.android.godotplaygameservices.signals.SnapshotSignals.gameLoaded
import com.jacobibanez.plugin.android.godotplaygameservices.signals.SnapshotSignals.gameSaved
import com.jacobibanez.plugin.android.godotplaygameservices.signals.SnapshotSignals.snapshotsLoaded
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal

private enum class Origin {
    SAVE, LOAD
}

class SnapshotsProxy(
    private val godot: Godot,
    private val snapshotsClient: SnapshotsClient = PlayGames.getSnapshotsClient(godot.getActivity()!!)
) {
    private val tag = SnapshotsProxy::class.java.simpleName

    private val showSavedGamesRequestCode = 9010
    private val snapshotNotFoundCode = 26570

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == showSavedGamesRequestCode && resultCode == Activity.RESULT_OK) {
            data?.let { intent ->
                if (intent.hasExtra(EXTRA_SNAPSHOT_METADATA)) {
                    val snapshotMetadata = intent.extras
                        ?.get(EXTRA_SNAPSHOT_METADATA) as SnapshotMetadata
                    loadGame(snapshotMetadata.uniqueName, false)
                }
            }
        }
    }

    fun showSavedGames(
        title: String,
        allowAddButton: Boolean,
        allowDelete: Boolean,
        maxSnapshots: Int
    ) {
        Log.d(tag, "Showing save games")
        snapshotsClient.getSelectSnapshotIntent(title, allowAddButton, allowDelete, maxSnapshots)
            .addOnSuccessListener { intent ->
                ActivityCompat.startActivityForResult(
                    godot.getActivity()!!, intent,
                    showSavedGamesRequestCode, null
                )
            }
    }

    fun saveGame(
        fileName: String,
        description: String,
        saveData: ByteArray,
        playedTimeMillis: Long,
        progressValue: Long
    ) {
        Log.d(tag, "Saving game data with name $fileName and description ${description}.")
        snapshotsClient.open(fileName, true, RESOLUTION_POLICY_HIGHEST_PROGRESS)
            .addOnSuccessListener { dataOrConflict ->
                if (dataOrConflict.isConflict) {
                    handleConflict(Origin.SAVE.name, dataOrConflict.conflict)
                    return@addOnSuccessListener
                }
                dataOrConflict.data?.let { snapshot ->
                    snapshot.snapshotContents.writeBytes(saveData)
                    val metadata = SnapshotMetadataChange.Builder().apply {
                        setDescription(description)
                        setPlayedTimeMillis(playedTimeMillis)
                        setProgressValue(progressValue)
                    }.build()

                    snapshotsClient.commitAndClose(snapshot, metadata)
                    emitSignal(
                        godot,
                        GODOT_PLUGIN_NAME,
                        gameSaved,
                        true,
                        fileName,
                        description
                    )
                }
            }
    }

    fun loadGame(fileName: String, createIfNotFound: Boolean) {
        Log.d(tag, "Loading snapshot with name $fileName.")
        snapshotsClient.open(fileName, createIfNotFound, RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val dataOrConflict = task.result
                    if (dataOrConflict.isConflict) {
                        handleConflict(Origin.LOAD.name, dataOrConflict.conflict)
                        return@addOnCompleteListener
                    }
                    dataOrConflict.data?.let { snapshot ->
                        emitSignal(
                            godot,
                            GODOT_PLUGIN_NAME,
                            gameLoaded,
                            Gson().toJson(fromSnapshot(godot, snapshot))
                        )
                    }
                } else {
                    val exception = task.exception
                    Log.e(
                        tag,
                        "Error while opening Snapshot $fileName for loading. Cause: $exception"
                    )
                    if (exception is ApiException && exception.statusCode == snapshotNotFoundCode) {
                        emitSignal(
                            godot,
                            GODOT_PLUGIN_NAME,
                            gameLoaded,
                            Gson().toJson(null)
                        )
                    }
                }
            }
    }

    fun loadSnapshots(forceReload: Boolean) {
        Log.d(tag, "Loading snapshots")
        snapshotsClient.load(forceReload).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(
                    tag,
                    "Snapshots loaded successfully. Data is stale? ${task.result.isStale}"
                )
                val snapshots = task.result.get()!!
                val result: List<Dictionary> = snapshots.map { snapshotMetadata ->
                    fromSnapshotMetadata(godot, snapshotMetadata)
                }.toList()
                snapshots.release()
                emitSignal(
                    godot,
                    GODOT_PLUGIN_NAME,
                    snapshotsLoaded,
                    Gson().toJson(result)
                )
            } else {
                Log.e(
                    tag,
                    "Failed to load snapshots. Cause: ${task.exception}",
                    task.exception
                )
                emitSignal(
                    godot,
                    GODOT_PLUGIN_NAME,
                    snapshotsLoaded,
                    Gson().toJson(null)
                )
            }
        }
    }

    fun deleteSnapshot(snapshotId: String) {
        var isDeleted = false
        snapshotsClient.load(true).addOnSuccessListener { annotatedData ->
            annotatedData.get()?.let { buffer ->
                buffer
                    .toList()
                    .firstOrNull { it.snapshotId == snapshotId }?.let { snapshotMetadata ->
                        Log.d(tag, "Deleting snapshot with id $snapshotId")
                        snapshotsClient.delete(snapshotMetadata).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(
                                    tag,
                                    "Snapshot with id $snapshotId deleted successfully."
                                )
                                isDeleted = true
                            } else {
                                Log.e(
                                    tag,
                                    "Failed to delete snapshot with id $snapshotId. Cause: ${task.exception}",
                                    task.exception
                                )
                            }
                        }
                    }
            }
        }
        if (!isDeleted) {
            Log.d(tag, "Snapshot with id $snapshotId not found!")
        }
    }

    private fun handleConflict(origin: String, conflict: SnapshotConflict?) {
        conflict?.let {
            val snapshot = it.snapshot
            val fileName = snapshot.metadata.uniqueName
            val description = snapshot.metadata.description
            Log.e(
                tag, "Conflict with id ${conflict.conflictId} during saving of data with " +
                        "name $fileName and description ${description}."
            )
            emitSignal(
                godot,
                GODOT_PLUGIN_NAME,
                conflictEmitted,
                Gson().toJson(fromConflict(godot, origin, it))
            )
        }
    }
}