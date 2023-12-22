package com.jacobibanez.plugin.android.godotplaygameservices.snapshots

import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.SnapshotsClient
import com.google.android.gms.games.snapshot.SnapshotMetadataChange
import com.jacobibanez.plugin.android.godotplaygameservices.BuildConfig
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
        snapshotsClient.open(fileName, true, SnapshotsClient.RESOLUTION_POLICY_HIGHEST_PROGRESS)
            .addOnSuccessListener { dataOrConflict ->
                if (dataOrConflict.isConflict) {
                    Log.e(
                        tag,
                        "Conflict during saving of data with name $fileName and description ${description}."
                    )
                    emitSignal(
                        godot,
                        BuildConfig.GODOT_PLUGIN_NAME,
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
                        BuildConfig.GODOT_PLUGIN_NAME,
                        gameSaved,
                        true,
                        fileName,
                        description
                    )
                }
            }
    }
}