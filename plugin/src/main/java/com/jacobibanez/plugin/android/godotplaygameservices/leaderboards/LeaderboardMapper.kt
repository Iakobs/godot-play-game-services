package com.jacobibanez.plugin.android.godotplaygameservices.leaderboards

import com.google.android.gms.games.LeaderboardsClient
import com.google.android.gms.games.leaderboard.Leaderboard
import com.google.android.gms.games.leaderboard.LeaderboardScore
import com.google.android.gms.games.leaderboard.LeaderboardVariant
import com.jacobibanez.plugin.android.godotplaygameservices.players.fromPlayer
import com.jacobibanez.plugin.android.godotplaygameservices.utils.toStringAndSave
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot

/** @suppress */
fun fromLeaderboardScore(godot: Godot, score: LeaderboardScore) =
    Dictionary().apply {
        put("displayRank", score.displayRank)
        put("displayScore", score.displayScore)
        put("rank", score.rank)
        put("rawScore", score.rawScore)
        put("scoreHolderDisplayName", score.scoreHolderDisplayName)
        put("scoreTag", score.scoreTag)
        put("timestampMillis", score.timestampMillis)
        score.scoreHolder?.let { player ->
            put("scoreHolder", fromPlayer(godot, player))

            put(
                "scoreHolderHiResImageUri",
                score.scoreHolderHiResImageUri.toStringAndSave(
                    godot,
                    "scoreHolderHiResImageUri",
                    player.playerId
                )
            )
            put(
                "scoreHolderIconImageUri",
                score.scoreHolderIconImageUri.toStringAndSave(
                    godot,
                    "scoreHolderIconImageUri",
                    player.playerId
                )
            )
        }
    }

/** @suppress */
fun fromLeaderboard(godot: Godot, leaderboard: Leaderboard) = Dictionary().apply {
    put("leaderboardId", leaderboard.leaderboardId)
    put("displayName", leaderboard.displayName)
    put("variants", fromLeaderboardVariant(leaderboard.variants))
    ScoreOrder.fromOrder(leaderboard.scoreOrder)?.let { put("scoreOrder", it.name) }
    leaderboard.iconImageUri?.let {
        put(
            "iconImageUri",
            it.toStringAndSave(
                godot,
                "iconImageUri",
                leaderboard.leaderboardId
            )
        )
    }
}

/** @suppress */
fun fromLeaderboardVariant(variants: ArrayList<LeaderboardVariant>): List<Dictionary> = variants
    .map { variant ->
        Dictionary().apply {
            Collection.fromType(variant.collection)?.let { put("collection", it.name) }
            TimeSpan.fromSpan(variant.timeSpan)?.let { put("timeSpan", it.name) }
            put("displayPlayerRank", variant.displayPlayerRank)
            put("displayPlayerScore", variant.displayPlayerScore)
            put("numScores", variant.numScores)
            put("playerRank", variant.playerRank)
            put("playerScoreTag", variant.playerScoreTag)
            put("rawPlayerScore", variant.rawPlayerScore)
            put("hasPlayerInfo", variant.hasPlayerInfo())
        }
    }.toList()

/** @suppress */
fun fromLeaderboardScores(godot: Godot, scores: LeaderboardsClient.LeaderboardScores) = Dictionary().apply {
    put("leaderboard", fromLeaderboard(godot, scores.leaderboard!!))
    val scoresMapped = scores.scores.map { fromLeaderboardScore(godot, it) }.toList()
    put("scores", scoresMapped)
}