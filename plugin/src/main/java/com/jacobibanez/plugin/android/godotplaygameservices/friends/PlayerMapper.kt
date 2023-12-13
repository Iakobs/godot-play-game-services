package com.jacobibanez.plugin.android.godotplaygameservices.friends

import com.google.android.gms.games.Player
import com.google.android.gms.games.Player.FriendsListVisibilityStatus.FEATURE_UNAVAILABLE
import com.google.android.gms.games.Player.FriendsListVisibilityStatus.REQUEST_REQUIRED
import com.google.android.gms.games.Player.FriendsListVisibilityStatus.UNKNOWN
import com.google.android.gms.games.Player.FriendsListVisibilityStatus.VISIBLE
import com.google.android.gms.games.Player.PlayerFriendStatus.FRIEND
import com.google.android.gms.games.Player.PlayerFriendStatus.NO_RELATIONSHIP
import com.google.android.gms.games.Player.PlayerFriendStatus.UNKNOWN as FRIEND_STATUS_UNKNOWN
import com.google.android.gms.games.PlayerLevel
import com.google.android.gms.games.PlayerLevelInfo
import org.godotengine.godot.Dictionary

fun fromPlayer(player: Player?): Dictionary = if (player != null) {
    Dictionary().apply {
        put("bannerImageLandscapeUri", player.bannerImageLandscapeUri.toString())
        put("bannerImagePortraitUri", player.bannerImagePortraitUri.toString())
        put(
            "friendsListVisibilityStatus", if (player.currentPlayerInfo != null) {
                getVisibilityStatus(player.currentPlayerInfo!!.friendsListVisibilityStatus)
            } else {
                ""
            }
        )
        put("displayName", player.displayName)
        put("hiResImageUri", player.hiResImageUri.toString())
        put("levelInfo", fromPlayerLevelInfo(player.levelInfo))
        put("playerId", player.playerId)
        put(
            "friendStatus", if (player.relationshipInfo != null) {
                getFriendStatus(player.relationshipInfo!!.friendStatus)
            } else {
                ""
            }
        )
        put("retrievedTimestamp", player.retrievedTimestamp)
        put("title", player.title)
        put("hasHiResImage", player.hasHiResImage())
        put("hasIconImage", player.hasIconImage())
    }
} else {
    Dictionary()
}

fun fromPlayerLevelInfo(levelInfo: PlayerLevelInfo?): Dictionary = if (levelInfo != null) {
    Dictionary().apply {
        put("currentLevel", fromPlayerLevel(levelInfo.currentLevel))
        put("currentXpTotal", levelInfo.currentXpTotal)
        put("lastLevelUpTimestamp", levelInfo.lastLevelUpTimestamp)
        put("nextLevel", fromPlayerLevel(levelInfo.nextLevel))
        put("isMaxLevel", levelInfo.isMaxLevel)
    }
} else {
    Dictionary()
}

fun fromPlayerLevel(playerLevel: PlayerLevel?): Dictionary = if (playerLevel != null) {
    Dictionary().apply {
        put("levelNumber", playerLevel.levelNumber)
        put("maxXp", playerLevel.maxXp)
        put("minXp", playerLevel.minXp)
    }
} else {
    Dictionary()
}

private fun getVisibilityStatus(state: Int): String = when (state) {
    FEATURE_UNAVAILABLE -> "FEATURE_UNAVAILABLE"
    REQUEST_REQUIRED -> "REQUEST_REQUIRED"
    UNKNOWN -> "UNKNOWN"
    VISIBLE -> "VISIBLE"
    else -> ""
}

private fun getFriendStatus(state: Int): String = when (state) {
    FRIEND -> "FRIEND"
    NO_RELATIONSHIP -> "NO_RELATIONSHIP"
    FRIEND_STATUS_UNKNOWN -> "UNKNOWN"
    else -> ""
}