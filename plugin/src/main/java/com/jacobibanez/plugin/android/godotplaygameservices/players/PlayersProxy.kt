package com.jacobibanez.plugin.android.godotplaygameservices.players

import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.games.FriendsResolutionRequiredException
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.PlayersClient
import com.google.gson.Gson
import com.jacobibanez.plugin.android.godotplaygameservices.BuildConfig
import com.jacobibanez.plugin.android.godotplaygameservices.signals.PlayerSignals.friendsLoaded
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal

/** @suppress */
class PlayersProxy(
    private val godot: Godot,
    private val playersClient: PlayersClient = PlayGames.getPlayersClient(godot.getActivity()!!)
) {

    private val tag: String = PlayersProxy::class.java.simpleName

    private val showSharingFriendsConsentRequestCode = 9006
    private val compareProfileRequestCode = 9007
    private val compareProfileWithAlternativeNameHintsRequestCode = 9008

    fun loadFriends(pageSize: Int, forceReload: Boolean, askForPermission: Boolean) {
        Log.d(tag, "Loading friends with page size of $pageSize and forceReload = $forceReload")
        playersClient.loadFriends(pageSize, forceReload).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(
                    tag,
                    "Friends loaded. Data is stale? ${task.result.isStale}"
                )
                val safeBuffer = task.result.get()!!
                val friendsCount = safeBuffer.count
                val friends: List<Dictionary> =
                    if (friendsCount > 0) {
                        safeBuffer.map { fromPlayer(it) }.toList()
                    } else {
                        emptyList()
                    }

                emitSignal(
                    godot,
                    BuildConfig.GODOT_PLUGIN_NAME,
                    friendsLoaded,
                    Gson().toJson(friends)
                )
            } else {
                if (task.exception is FriendsResolutionRequiredException && askForPermission) {
                    askForFriendsListPermission(task.exception as FriendsResolutionRequiredException)
                } else {
                    Log.e(
                        tag, "Unable to load friends. Cause: ${task.exception}", task.exception
                    )
                    emitSignal(
                        godot,
                        BuildConfig.GODOT_PLUGIN_NAME,
                        friendsLoaded,
                        Gson().toJson(emptyList<Dictionary>())
                    )
                }
            }
        }
    }

    fun compareProfile(otherPlayerId: String) {
        Log.d(tag, "Comparing profile with player with id $otherPlayerId")
        playersClient.getCompareProfileIntent(otherPlayerId).addOnSuccessListener { intent ->
            ActivityCompat.startActivityForResult(
                godot.getActivity()!!,
                intent,
                compareProfileRequestCode,
                null
            )
        }
    }

    fun compareProfileWithAlternativeNameHints(
        otherPlayerId: String,
        otherPlayerInGameName: String,
        currentPlayerInGameName: String
    ) {
        Log.d(
            tag, "Comparing profile with player with id $otherPlayerId. " +
                    "Current player in-game name: $currentPlayerInGameName. " +
                    "Other player in-game name: $otherPlayerInGameName"
        )
        playersClient.getCompareProfileIntentWithAlternativeNameHints(
            otherPlayerId,
            otherPlayerInGameName,
            currentPlayerInGameName
        ).addOnSuccessListener { intent ->
            ActivityCompat.startActivityForResult(
                godot.getActivity()!!,
                intent,
                compareProfileWithAlternativeNameHintsRequestCode,
                null
            )
        }
    }

    private fun askForFriendsListPermission(exception: FriendsResolutionRequiredException) {
        val pendingIntent = exception.resolution
        godot.getActivity()!!.startIntentSenderForResult(
            pendingIntent.intentSender,
            showSharingFriendsConsentRequestCode,
            null,
            0,
            0,
            0,
            null
        )
    }
}