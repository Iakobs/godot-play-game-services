package com.jacobibanez.plugin.android.godotplaygameservices.players

import com.google.android.gms.games.Player
import com.google.android.gms.games.PlayerLevel
import com.google.android.gms.games.PlayerLevelInfo
import org.godotengine.godot.Dictionary

/** @suppress */
fun fromPlayer(player: Player): Dictionary = Dictionary().apply {
    put("bannerImageLandscapeUri", player.bannerImageLandscapeUri?.toString())
    put("bannerImagePortraitUri", player.bannerImagePortraitUri?.toString())
    put("displayName", player.displayName)
    put("hiResImageUri", player.hiResImageUri?.toString())
    put("iconImageUri", player.iconImageUri?.toString())
    put("playerId", player.playerId)
    put("retrievedTimestamp", player.retrievedTimestamp)
    put("title", player.title)
    put("hasHiResImage", player.hasHiResImage())
    put("hasIconImage", player.hasIconImage())
    player.levelInfo?.let {
        put("levelInfo", fromPlayerLevelInfo(it))
    }
    player.currentPlayerInfo?.let { currentPlayerInfo ->
        FriendsListVisibilityStatus.fromStatus(currentPlayerInfo.friendsListVisibilityStatus)
            ?.let {
                put("friendsListVisibilityStatus", it.name)
            }
    }
    player.relationshipInfo?.let { relationshipInfo ->
        PlayerFriendStatus.fromStatus(relationshipInfo.friendStatus)?.let {
            put("friendStatus", it.name)
        }
    }
}

/** @suppress */
fun fromPlayerLevelInfo(levelInfo: PlayerLevelInfo): Dictionary = Dictionary().apply {
    put("currentLevel", fromPlayerLevel(levelInfo.currentLevel))
    put("currentXpTotal", levelInfo.currentXpTotal)
    put("lastLevelUpTimestamp", levelInfo.lastLevelUpTimestamp)
    put("nextLevel", fromPlayerLevel(levelInfo.nextLevel))
    put("isMaxLevel", levelInfo.isMaxLevel)
}

/** @suppress */
fun fromPlayerLevel(playerLevel: PlayerLevel): Dictionary = Dictionary().apply {
    put("levelNumber", playerLevel.levelNumber)
    put("maxXp", playerLevel.maxXp)
    put("minXp", playerLevel.minXp)
}