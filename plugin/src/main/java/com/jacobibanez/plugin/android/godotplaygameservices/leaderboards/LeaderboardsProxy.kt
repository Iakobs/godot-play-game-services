package com.jacobibanez.plugin.android.godotplaygameservices.leaderboards

import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.games.LeaderboardsClient
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.leaderboard.LeaderboardBuffer
import com.google.gson.Gson
import com.jacobibanez.plugin.android.godotplaygameservices.BuildConfig
import com.jacobibanez.plugin.android.godotplaygameservices.signals.LeaderboardSignals.allLeaderboardsLoaded
import com.jacobibanez.plugin.android.godotplaygameservices.signals.LeaderboardSignals.leaderboardLoaded
import com.jacobibanez.plugin.android.godotplaygameservices.signals.LeaderboardSignals.playerCenteredScoresLoaded
import com.jacobibanez.plugin.android.godotplaygameservices.signals.LeaderboardSignals.scoreLoaded
import com.jacobibanez.plugin.android.godotplaygameservices.signals.LeaderboardSignals.scoreSubmitted
import com.jacobibanez.plugin.android.godotplaygameservices.signals.LeaderboardSignals.topScoresLoaded
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal

/** @suppress */
class LeaderboardsProxy(
    private val godot: Godot,
    private val leaderboardsClient: LeaderboardsClient = PlayGames.getLeaderboardsClient(godot.getActivity()!!)
) {

    private val tag: String = LeaderboardsProxy::class.java.simpleName

    private val showAllLeaderboardsRequestCode = 9002
    private val showLeaderboardRequestCode = 9003
    private val showLeaderboardForTimeSpanRequestCode = 9004
    private val showLeaderboardForTimeSpanAndCollectionRequestCode = 9005

    fun showAllLeaderboards() {
        Log.d(tag, "Showing all leaderboards")
        leaderboardsClient.allLeaderboardsIntent.addOnSuccessListener { intent ->
            ActivityCompat.startActivityForResult(
                godot.getActivity()!!,
                intent,
                showAllLeaderboardsRequestCode,
                null
            )
        }
    }

    fun showLeaderboard(leaderboardId: String) {
        Log.d(tag, "Showing leaderboard with id $leaderboardId")
        leaderboardsClient.getLeaderboardIntent(leaderboardId).addOnSuccessListener { intent ->
            ActivityCompat.startActivityForResult(
                godot.getActivity()!!,
                intent,
                showLeaderboardRequestCode,
                null
            )
        }
    }

    fun showLeaderboardForTimeSpan(leaderboardId: String, timeSpan: Int) {
        Log.d(
            tag,
            "Showing leaderboard with id $leaderboardId for time span ${TimeSpan.fromSpan(timeSpan)?.name}"
        )
        leaderboardsClient.getLeaderboardIntent(leaderboardId, timeSpan)
            .addOnSuccessListener { intent ->
                ActivityCompat.startActivityForResult(
                    godot.getActivity()!!,
                    intent,
                    showLeaderboardForTimeSpanRequestCode,
                    null
                )
            }
    }

    fun showLeaderboardForTimeSpanAndCollection(
        leaderboardId: String,
        timeSpan: Int,
        collection: Int
    ) {
        Log.d(
            tag,
            "Showing leaderboard with id $leaderboardId for time span ${TimeSpan.fromSpan(timeSpan)?.name} and collection ${
                Collection.fromType(collection)?.name
            }"
        )
        leaderboardsClient.getLeaderboardIntent(leaderboardId, timeSpan, collection)
            .addOnSuccessListener { intent ->
                ActivityCompat.startActivityForResult(
                    godot.getActivity()!!,
                    intent,
                    showLeaderboardForTimeSpanAndCollectionRequestCode,
                    null
                )
            }
    }

    fun submitScore(leaderboardId: String, score: Int) {
        Log.d(tag, "Submitting score of $score to leaderboard $leaderboardId")
        leaderboardsClient.submitScoreImmediate(leaderboardId, score.toLong())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "Score submitted successfully")
                    emitSignal(
                        godot,
                        BuildConfig.GODOT_PLUGIN_NAME,
                        scoreSubmitted,
                        true,
                        leaderboardId
                    )
                } else {
                    Log.e(
                        tag,
                        "Error submitting score. Cause: ${task.exception}",
                        task.exception
                    )
                    emitSignal(
                        godot,
                        BuildConfig.GODOT_PLUGIN_NAME,
                        scoreSubmitted,
                        false,
                        leaderboardId
                    )
                }
            }
    }

    fun loadPlayerScore(leaderboardId: String, timeSpan: Int, collection: Int) {
        Log.d(
            tag, "Loading player score for leaderboard $leaderboardId, " +
                    "span ${TimeSpan.fromSpan(timeSpan)?.name} and collection " +
                    "${Collection.fromType(collection)?.name}"
        )
        leaderboardsClient.loadCurrentPlayerLeaderboardScore(
            leaderboardId, timeSpan, collection
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "Score loaded successfully. Data is stale? ${task.result.isStale}")
                val score: Dictionary? = task.result.get()?.let { fromLeaderboardScore(godot, it) }
                emitSignal(
                    godot,
                    BuildConfig.GODOT_PLUGIN_NAME,
                    scoreLoaded,
                    leaderboardId,
                    Gson().toJson(score)
                )
            } else {
                Log.e(tag, "Failed to load score. Cause: ${task.exception}", task.exception)
                emitSignal(
                    godot,
                    BuildConfig.GODOT_PLUGIN_NAME,
                    scoreLoaded,
                    leaderboardId,
                    Gson().toJson(null)
                )
            }
        }
    }

    fun loadAllLeaderboards(forceReload: Boolean) {
        Log.d(tag, "Loading all leaderboards")
        leaderboardsClient.loadLeaderboardMetadata(forceReload).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(
                    tag,
                    "Leaderboards loaded successfully. Data is stale? ${task.result.isStale}"
                )
                val safeBuffer: LeaderboardBuffer = task.result.get()!!
                val leaderboardsCount = safeBuffer.count
                val leaderboards: List<Dictionary> =
                    if (leaderboardsCount > 0) {
                        safeBuffer.map { fromLeaderboard(godot, it) }.toList()
                    } else {
                        emptyList()
                    }

                emitSignal(
                    godot,
                    BuildConfig.GODOT_PLUGIN_NAME,
                    allLeaderboardsLoaded,
                    Gson().toJson(leaderboards)
                )
            } else {
                Log.e(tag, "Failed to load Leaderboards. Cause: ${task.exception}", task.exception)
                emitSignal(
                    godot,
                    BuildConfig.GODOT_PLUGIN_NAME,
                    allLeaderboardsLoaded,
                    Gson().toJson(emptyList<Dictionary>())
                )
            }
        }
    }

    fun loadLeaderboard(leaderboardId: String, forceReload: Boolean) {
        Log.d(tag, "Loading leaderboard $leaderboardId")
        leaderboardsClient.loadLeaderboardMetadata(leaderboardId, forceReload)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(
                        tag,
                        "Leaderboard loaded successfully. Data is stale? ${task.result.isStale}"
                    )
                    val leaderboard: Dictionary? = task.result.get()?.let {
                        fromLeaderboard(godot, it)
                    }
                    emitSignal(
                        godot,
                        BuildConfig.GODOT_PLUGIN_NAME,
                        leaderboardLoaded,
                        Gson().toJson(leaderboard)
                    )
                } else {
                    Log.e(
                        tag,
                        "Failed to load leaderboard. Cause: ${task.exception}",
                        task.exception
                    )
                    emitSignal(
                        godot,
                        BuildConfig.GODOT_PLUGIN_NAME,
                        leaderboardLoaded,
                        Gson().toJson(null)
                    )
                }
            }
    }

    fun loadPlayerCenteredScores(
        leaderboardId: String,
        timeSpan: Int,
        collection: Int,
        maxResults: Int,
        forceReload: Boolean
    ) {
        Log.d(
            tag, "Loading player centered scores for leaderboard $leaderboardId, " +
                    "span ${TimeSpan.fromSpan(timeSpan)?.name}, collection " +
                    "${Collection.fromType(collection)?.name} and max results $maxResults"
        )
        leaderboardsClient.loadPlayerCenteredScores(leaderboardId, timeSpan, collection, maxResults, forceReload)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(
                        tag,
                        "Player centered scores loaded successfully. Data is stale? ${task.result.isStale}"
                    )
                    val leaderboardScores = task.result.get()!!
                    val result: Dictionary = fromLeaderboardScores(godot, leaderboardScores)
                    leaderboardScores.release()
                    emitSignal(
                        godot,
                        BuildConfig.GODOT_PLUGIN_NAME,
                        playerCenteredScoresLoaded,
                        leaderboardId,
                        Gson().toJson(result)
                    )
                } else {
                    Log.e(
                        tag,
                        "Failed to load player centered scores. Cause: ${task.exception}",
                        task.exception
                    )
                    emitSignal(
                        godot,
                        BuildConfig.GODOT_PLUGIN_NAME,
                        playerCenteredScoresLoaded,
                        leaderboardId,
                        Gson().toJson(null)
                    )
                }
            }
    }

    fun loadTopScores(
        leaderboardId: String,
        timeSpan: Int,
        collection: Int,
        maxResults: Int,
        forceReload: Boolean
    ) {
        Log.d(
            tag, "Loading top scores for leaderboard $leaderboardId, " +
                    "span ${TimeSpan.fromSpan(timeSpan)?.name}, collection " +
                    "${Collection.fromType(collection)?.name} and max results $maxResults"
        )
        leaderboardsClient.loadTopScores(leaderboardId, timeSpan, collection, maxResults, forceReload)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(
                        tag,
                        "Top scores loaded successfully. Data is stale? ${task.result.isStale}"
                    )
                    val leaderboardScores = task.result.get()!!
                    val result: Dictionary = fromLeaderboardScores(godot, leaderboardScores)
                    leaderboardScores.release()
                    emitSignal(
                        godot,
                        BuildConfig.GODOT_PLUGIN_NAME,
                        topScoresLoaded,
                        leaderboardId,
                        Gson().toJson(result)
                    )
                } else {
                    Log.e(
                        tag,
                        "Failed to load top scores. Cause: ${task.exception}",
                        task.exception
                    )
                    emitSignal(
                        godot,
                        BuildConfig.GODOT_PLUGIN_NAME,
                        topScoresLoaded,
                        leaderboardId,
                        Gson().toJson(null)
                    )
                }
            }
    }
}