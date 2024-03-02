package com.jacobibanez.plugin.android.godotplaygameservices.events

import android.util.Log
import com.google.android.gms.games.EventsClient
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.event.EventBuffer
import com.google.gson.Gson
import com.jacobibanez.plugin.android.godotplaygameservices.BuildConfig
import com.jacobibanez.plugin.android.godotplaygameservices.signals.EventsSignals.eventsLoaded
import com.jacobibanez.plugin.android.godotplaygameservices.signals.EventsSignals.eventsLoadedByIds
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal

/** @suppress */
class EventsProxy(
    private val godot: Godot,
    private val eventsClient: EventsClient = PlayGames.getEventsClient(godot.getActivity()!!)
) {

    private val tag: String = EventsProxy::class.java.simpleName

    fun incrementEvent(eventId: String, incrementAmount: Int) {
        Log.d(tag, "Submitting event $eventId by value $incrementAmount")
        eventsClient.increment(eventId, incrementAmount)
    }

    fun loadEvents(forceReload: Boolean) {
        Log.d(tag, "Retrieving events for this user (forceReload = $forceReload)")
        eventsClient.load(forceReload).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(
                    tag,
                    "Events loaded successfully. Data is stale? ${task.result.isStale}"
                )
                val safeBuffer: EventBuffer = task.result.get()!!
                val events: List<Dictionary> = if (safeBuffer.toList().isNotEmpty()) {
                    safeBuffer.map { fromEvent(godot, it) }.toList()
                } else {
                    emptyList()
                }
                emitSignal(
                    godot,
                    BuildConfig.GODOT_PLUGIN_NAME,
                    eventsLoaded,
                    Gson().toJson(events)
                )
            } else {
                Log.e(tag, "Failed to load events. Cause: ${task.exception}", task.exception)
                emitSignal(
                    godot,
                    BuildConfig.GODOT_PLUGIN_NAME,
                    eventsLoaded,
                    Gson().toJson(emptyList<Dictionary>())
                )
            }
        }
    }

    fun loadEventsByIds(forceReload: Boolean, eventIds: Array<String>) {
        Log.d(tag, "Retrieving events for from eventIds $eventIds (forceReload = $forceReload)")
        eventsClient.loadByIds(forceReload, *(eventIds)).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(
                    tag,
                    "Events loaded successfully. Data is stale? ${task.result.isStale}"
                )
                val safeBuffer: EventBuffer = task.result.get()!!
                val events: List<Dictionary> = if (safeBuffer.toList().isNotEmpty()) {
                    safeBuffer.map { fromEvent(godot, it) }.toList()
                } else {
                    emptyList()
                }
                emitSignal(
                    godot,
                    BuildConfig.GODOT_PLUGIN_NAME,
                    eventsLoadedByIds,
                    Gson().toJson(events)
                )
            } else {
                Log.e(tag, "Failed to load events. Cause: ${task.exception}", task.exception)
                emitSignal(
                    godot,
                    BuildConfig.GODOT_PLUGIN_NAME,
                    eventsLoadedByIds,
                    Gson().toJson(emptyList<Dictionary>())
                )
            }
        }
    }
}