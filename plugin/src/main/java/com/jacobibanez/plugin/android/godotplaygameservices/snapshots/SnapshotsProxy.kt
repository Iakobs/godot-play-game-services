package com.jacobibanez.plugin.android.godotplaygameservices.snapshots

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.SnapshotsClient
import com.google.android.gms.games.SnapshotsClient.EXTRA_SNAPSHOT_METADATA
import com.google.android.gms.games.SnapshotsClient.RESOLUTION_POLICY_HIGHEST_PROGRESS
import com.google.android.gms.games.SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED
import com.google.android.gms.games.SnapshotsClient.SnapshotConflict
import com.google.android.gms.games.snapshot.SnapshotMetadata
import com.google.android.gms.games.snapshot.SnapshotMetadataChange
import com.jacobibanez.plugin.android.godotplaygameservices.BuildConfig.GODOT_PLUGIN_NAME
import com.jacobibanez.plugin.android.godotplaygameservices.signals.SnapshotSignals.conflictEmitted
import com.jacobibanez.plugin.android.godotplaygameservices.signals.SnapshotSignals.gameLoaded
import com.jacobibanez.plugin.android.godotplaygameservices.signals.SnapshotSignals.gameSaved
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal

class SnapshotsProxy(
    private val godot: Godot,
    private val snapshotsClient: SnapshotsClient = PlayGames.getSnapshotsClient(godot.getActivity()!!)
) {
    private val tag = SnapshotsProxy::class.java.simpleName

    private val showSavedGamesRequestCode = 9010

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == showSavedGamesRequestCode && resultCode == Activity.RESULT_OK) {
            data?.let { intent ->
                if (intent.hasExtra(EXTRA_SNAPSHOT_METADATA)) {
                    val snapshotMetadata = intent.extras
                        ?.get(EXTRA_SNAPSHOT_METADATA) as SnapshotMetadata
                    loadGame(snapshotMetadata.uniqueName)
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
                    handleConflict(dataOrConflict.conflict)
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

    fun loadGame(fileName: String) {
        Log.d(tag, "Loading snapshot with name $fileName.")
        snapshotsClient.open(fileName, false, RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED)
            .addOnFailureListener { exception ->
                Log.e(tag, "Error while opening Snapshot $fileName for loading.", exception);
            }.continueWith { task ->
                val dataOrConflict = task.result
                if (dataOrConflict.isConflict) {
                    handleConflict(dataOrConflict.conflict)
                    return@continueWith null
                }
                dataOrConflict.data?.let { snapshot ->
                    return@continueWith snapshot
                }
            }.addOnCompleteListener { task ->
                task.result?.let { snapshot ->
                    emitSignal(
                        godot,
                        GODOT_PLUGIN_NAME,
                        gameLoaded,
                        fromSnapshot(godot, snapshot)
                    )
                }
            }
    }

    private fun handleConflict(conflict: SnapshotConflict?) {
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
                fromConflict(godot, it)
            )
        }
    }
}