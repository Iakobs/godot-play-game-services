package com.jacobibanez.plugin.android.godotplaygameservices.snapshots

import com.google.android.gms.games.snapshot.SnapshotMetadata
import com.jacobibanez.plugin.android.godotplaygameservices.games.fromGame
import com.jacobibanez.plugin.android.godotplaygameservices.players.fromPlayer
import com.jacobibanez.plugin.android.godotplaygameservices.utils.toStringAndSave
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot

/** @suppress */
fun fromSnapshotMetadata(godot: Godot, metadata: SnapshotMetadata) = Dictionary().apply {
    put("snapshotId", metadata.snapshotId)
    put("uniqueName", metadata.uniqueName)
    put("description", metadata.description)
    put("coverImageAspectRatio", metadata.coverImageAspectRatio)
    put("progressValue", metadata.progressValue)
    put("lastModifiedTimestamp", metadata.lastModifiedTimestamp)
    put("playedTime", metadata.playedTime)
    put("hasChangePending", metadata.hasChangePending())
    put("owner", fromPlayer(godot, metadata.owner))
    put("game", fromGame(godot, metadata.game))
    metadata.deviceName?.let { put("deviceName", it) }
    metadata.coverImageUri?.let {
        put(
            "coverImageUri",
            it.toStringAndSave(godot, "coverImageUri", metadata.uniqueName)
        )
    }
}

