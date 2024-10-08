package com.jacobibanez.plugin.android.godotplaygameservices.snapshots

import com.google.android.gms.games.SnapshotsClient.SnapshotConflict
import com.google.android.gms.games.snapshot.Snapshot
import com.google.android.gms.games.snapshot.SnapshotMetadata
import com.jacobibanez.plugin.android.godotplaygameservices.games.fromGame
import com.jacobibanez.plugin.android.godotplaygameservices.players.fromPlayer
import com.jacobibanez.plugin.android.godotplaygameservices.utils.toStringAndSave
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot

fun fromConflict(godot: Godot, origin: String, conflict: SnapshotConflict) = Dictionary().apply {
    put("origin", origin)
    put("conflictId", conflict.conflictId)
    put("conflictingSnapshot", fromSnapshot(godot, conflict.conflictingSnapshot))
    put("serverSnapshot", fromSnapshot(godot, conflict.snapshot))
}

fun fromSnapshot(godot: Godot, snapshot: Snapshot) = Dictionary().apply {
    put("content", snapshot.snapshotContents.readFully())
    put("metadata", fromSnapshotMetadata(godot, snapshot.metadata))
}

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

