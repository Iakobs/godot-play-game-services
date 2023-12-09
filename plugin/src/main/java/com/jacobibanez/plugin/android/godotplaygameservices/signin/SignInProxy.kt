package com.jacobibanez.plugin.android.godotplaygameservices.signin

import android.util.Log
import com.google.android.gms.games.GamesSignInClient
import com.google.android.gms.games.PlayGames
import com.jacobibanez.plugin.android.godotplaygameservices.BuildConfig
import com.jacobibanez.plugin.android.godotplaygameservices.isUserAuthenticatedFailure
import com.jacobibanez.plugin.android.godotplaygameservices.isUserAuthenticatedSuccess
import com.jacobibanez.plugin.android.godotplaygameservices.requestServerSideAccessFailure
import com.jacobibanez.plugin.android.godotplaygameservices.requestServerSideAccessSuccess
import com.jacobibanez.plugin.android.godotplaygameservices.signInFailure
import com.jacobibanez.plugin.android.godotplaygameservices.signInSuccess
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal

class SignInProxy(
    private val godot: Godot,
    private val gamesSignInClient: GamesSignInClient = PlayGames.getGamesSignInClient(godot.getActivity()!!)
) {

    private val tag: String = SignInProxy::class.java.simpleName

    fun isAuthenticated() {
        Log.d(tag, "Checking if user is authenticated")
        gamesSignInClient.isAuthenticated.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "User authenticated: ${task.result.isAuthenticated}")
                emitSignal(
                    godot,
                    BuildConfig.GODOT_PLUGIN_NAME,
                    isUserAuthenticatedSuccess,
                    task.result.isAuthenticated
                )
            } else {
                Log.e(tag, "User not authenticated. Cause: ${task.exception}", task.exception)
                emitSignal(godot, BuildConfig.GODOT_PLUGIN_NAME, isUserAuthenticatedFailure)
            }
        }
    }

    fun requestServerSideAccess(serverClientId: String, forceRefreshToken: Boolean) {
        Log.d(
            tag,
            "Requesting server side access for client id $serverClientId with refresh token $forceRefreshToken"
        )
        gamesSignInClient.requestServerSideAccess(serverClientId, forceRefreshToken)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "Access granted to server side for user: $serverClientId")
                    emitSignal(godot, BuildConfig.GODOT_PLUGIN_NAME, requestServerSideAccessSuccess, task.result)
                } else {
                    Log.e(
                        tag,
                        "Failed to request server side access. Cause: ${task.exception}",
                        task.exception
                    )
                    emitSignal(godot, BuildConfig.GODOT_PLUGIN_NAME, requestServerSideAccessFailure)
                }
            }
    }

    fun signIn() {
        Log.d(tag, "Signing in")
        gamesSignInClient.signIn().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "User signed in: ${task.result.isAuthenticated}")
                emitSignal(godot, BuildConfig.GODOT_PLUGIN_NAME, signInSuccess, task.result.isAuthenticated)
            } else {
                Log.e(tag, "User not signed in. Cause: ${task.exception}", task.exception)
                emitSignal(godot, BuildConfig.GODOT_PLUGIN_NAME, signInFailure)
            }
        }
    }
}