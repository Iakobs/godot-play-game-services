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
import com.google.android.gms.games.snapshot.SnapshotMetadata
import com.google.android.gms.games.snapshot.SnapshotMetadataChange
import com.jacobibanez.plugin.android.godotplaygameservices.BuildConfig.GODOT_PLUGIN_NAME
import com.jacobibanez.plugin.android.godotplaygameservices.signals.SnapshotSignals.gameLoaded
import com.jacobibanez.plugin.android.godotplaygameservices.signals.SnapshotSignals.gameSaved
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal


/** @suppress */
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

    fun saveGame(fileName: String, saveData: String, description: String) {
        Log.d(tag, "Saving game data with name $fileName and description ${description}.")
        snapshotsClient.open(fileName, true, RESOLUTION_POLICY_HIGHEST_PROGRESS)
            .addOnSuccessListener { dataOrConflict ->
                if (dataOrConflict.isConflict) {
                    Log.e(
                        tag,
                        "Conflict during saving of data with name $fileName and description ${description}."
                    )
                    emitSignal(
                        godot,
                        GODOT_PLUGIN_NAME,
                        gameSaved,
                        false,
                        fileName,
                        description
                    )
                    return@addOnSuccessListener
                }
                dataOrConflict.data?.let { snapshot ->
                    snapshot.snapshotContents.writeBytes(saveData.toByteArray())
                    val metadata = SnapshotMetadataChange.Builder()
                        .setDescription(description)
                        .build()

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
        Log.d(tag, "Loading game.")
        snapshotsClient.open(fileName, false, RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED)
            .addOnFailureListener { exception ->
                Log.e(tag, "Error while opening Snapshot.", exception);
            }.continueWith { dataOrConflictTask ->
                dataOrConflictTask.result.data?.let { snapshot ->
                    return@continueWith snapshot
                }
            }.addOnCompleteListener { task ->
                emitSignal(
                    godot,
                    GODOT_PLUGIN_NAME,
                    gameLoaded,
                    fromSnapshot(godot, task.result)
                )
            }
    }
}