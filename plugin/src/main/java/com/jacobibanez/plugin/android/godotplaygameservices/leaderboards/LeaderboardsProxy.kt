package com.jacobibanez.plugin.android.godotplaygameservices.leaderboards

import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.games.LeaderboardsClient
import com.google.android.gms.games.PlayGames
import com.jacobibanez.plugin.android.godotplaygameservices.BuildConfig
import com.jacobibanez.plugin.android.godotplaygameservices.submitScoreFailure
import com.jacobibanez.plugin.android.godotplaygameservices.submitScoreSuccess
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin

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
            "Showing leaderboard with id $leaderboardId for time span ${getTimeSpan(timeSpan)}"
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
            "Showing leaderboard with id $leaderboardId for time span ${getTimeSpan(timeSpan)} and collection ${
                getCollection(
                    collection
                )
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
        leaderboardsClient.submitScoreImmediate(leaderboardId, score.toLong()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "Score submitted successfully")
                GodotPlugin.emitSignal(godot, BuildConfig.GODOT_PLUGIN_NAME, submitScoreSuccess)
            } else {
                Log.e(
                    tag,
                    "Error submitting score. Cause: ${task.exception}",
                    task.exception
                )
                GodotPlugin.emitSignal(godot, BuildConfig.GODOT_PLUGIN_NAME, submitScoreFailure)
            }
        }
    }

    private fun getTimeSpan(timeSpan: Int): String = when (timeSpan) {
        0 -> "TIME_SPAN_DAILY"
        1 -> "TIME_SPAN_WEEKLY"
        2 -> "TIME_SPAN_ALL_TIME"
        else -> ""
    }

    private fun getCollection(timeSpan: Int): String = when (timeSpan) {
        0 -> "COLLECTION_PUBLIC"
        3 -> "COLLECTION_FRIENDS"
        else -> ""
    }
}