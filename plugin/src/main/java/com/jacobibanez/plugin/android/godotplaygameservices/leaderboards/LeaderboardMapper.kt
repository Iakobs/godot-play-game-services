package com.jacobibanez.plugin.android.godotplaygameservices.leaderboards

import com.google.android.gms.games.leaderboard.Leaderboard
import com.google.android.gms.games.leaderboard.LeaderboardScore
import com.google.android.gms.games.leaderboard.LeaderboardVariant
import com.jacobibanez.plugin.android.godotplaygameservices.friends.fromPlayer
import org.godotengine.godot.Dictionary

/** @suppress */
fun fromLeaderboardScore(score: LeaderboardScore): Dictionary = Dictionary().apply {
    put("displayRank", score.displayRank)
    put("displayScore", score.displayScore)
    put("rank", score.rank)
    put("rawScore", score.rawScore)
    put("scoreHolder", fromPlayer(score.scoreHolder))
    put("scoreHolderDisplayName", score.scoreHolderDisplayName)
    put("scoreHolderHiResImageUri", score.scoreHolderHiResImageUri?.toString())
    put("scoreHolderIconImageUri", score.scoreHolderIconImageUri?.toString())
    put("scoreTag", score.scoreTag)
    put("timestampMillis", score.timestampMillis)
}

/** @suppress */
fun fromLeaderboard(leaderboard: Leaderboard): Dictionary = Dictionary().apply {
    put("leaderboardId", leaderboard.leaderboardId)
    put("displayName", leaderboard.displayName)
    put("iconImageUri", leaderboard.iconImageUri?.toString())
    ScoreOrder.fromOrder(leaderboard.scoreOrder)?.let {
        put("scoreOrder", it.name)
    }
    put("variants", fromLeaderboardVariant(leaderboard.variants))
}

/** @suppress */
fun fromLeaderboardVariant(variants: ArrayList<LeaderboardVariant>): List<Dictionary> = variants
    .map { variant ->
        Dictionary().apply {
            Collection.fromType(variant.collection)?.let {
                put("collection", it.name)
            }
            TimeSpan.fromSpan(variant.timeSpan)?.let {
                put("timeSpan", it.name)
            }
            put("displayPlayerRank", variant.displayPlayerRank)
            put("displayPlayerScore", variant.displayPlayerScore)
            put("numScores", variant.numScores)
            put("playerRank", variant.playerRank)
            put("playerScoreTag", variant.playerScoreTag)
            put("rawPlayerScore", variant.rawPlayerScore)
            put("hasPlayerInfo", variant.hasPlayerInfo())
        }
    }.toList()
