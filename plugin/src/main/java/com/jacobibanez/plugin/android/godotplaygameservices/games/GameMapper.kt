package com.jacobibanez.plugin.android.godotplaygameservices.games

import com.google.android.gms.games.Game
import com.jacobibanez.plugin.android.godotplaygameservices.utils.toStringAndSave
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot

fun fromGame(godot: Godot, game: Game) = Dictionary().apply {
    put("areSnapshotsEnabled", game.areSnapshotsEnabled())
    put("achievementTotalCount", game.achievementTotalCount)
    put("applicationId", game.applicationId)
    put("description", game.description)
    put("developerName", game.developerName)
    put("displayName", game.displayName)
    put("leaderboardCount", game.leaderboardCount)
    game.primaryCategory?.let { put("primaryCategory", it) }
    game.secondaryCategory?.let { put("secondaryCategory", it) }
    put("themeColor", game.themeColor)
    put("hasGamepadSupport", game.hasGamepadSupport())
    game.hiResImageUri?.let {
        put(
            "hiResImageUri",
            it.toStringAndSave(godot, "hiResImageUri", game.applicationId)
        )
    }
    game.iconImageUri?.let {
        put(
            "iconImageUri",
            it.toStringAndSave(godot, "iconImageUri", game.applicationId)
        )
    }
    game.featuredImageUri?.let {
        put(
            "featuredImageUri",
            it.toStringAndSave(godot, "featuredImageUri", game.applicationId)
        )
    }
}