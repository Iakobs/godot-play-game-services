package com.jacobibanez.plugin.android.godotplaygameservices.events

import android.annotation.SuppressLint
import com.google.android.gms.games.event.Event
import com.jacobibanez.plugin.android.godotplaygameservices.players.fromPlayer
import com.jacobibanez.plugin.android.godotplaygameservices.utils.toStringAndSave
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot

/** @suppress */
@SuppressLint("VisibleForTests")
fun fromEvent(godot: Godot, event: Event) = Dictionary().apply {
    put("description", event.description)
    put("eventId", event.eventId)
    put("formattedValue", event.formattedValue)
    event.iconImageUri?.let {
        put(
            "iconImageUri",
            it.toStringAndSave(godot, "iconImageUri", event.eventId)
        )
    }
    put("name", event.name)
    put("player", fromPlayer(godot, event.player))
    put("value", event.value)
    put("isVisible", event.isVisible)
}