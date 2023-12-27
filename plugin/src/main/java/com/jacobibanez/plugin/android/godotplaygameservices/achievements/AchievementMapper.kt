package com.jacobibanez.plugin.android.godotplaygameservices.achievements

import com.google.android.gms.games.achievement.Achievement
import com.google.android.gms.games.achievement.Achievement.TYPE_INCREMENTAL
import com.jacobibanez.plugin.android.godotplaygameservices.players.fromPlayer
import com.jacobibanez.plugin.android.godotplaygameservices.utils.toStringAndSave
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot

/** @suppress */
fun fromAchievement(godot: Godot, achievement: Achievement) = Dictionary().apply {
    put("achievementId", achievement.achievementId)
    put("name", achievement.name)
    put("description", achievement.description)
    put("player", fromPlayer(godot, achievement.player))
    put("xpValue", achievement.xpValue)
    put("currentSteps", if (achievement.type == TYPE_INCREMENTAL) achievement.currentSteps else 0)
    put("totalSteps", if (achievement.type == TYPE_INCREMENTAL) achievement.totalSteps else 0)
    put("lastUpdatedTimestamp", achievement.lastUpdatedTimestamp)
    Type.fromType(achievement.type)?.let { put("type", it.name) }
    State.fromState(achievement.state)?.let { put("state", it.name) }
    if (achievement.type == TYPE_INCREMENTAL) {
        achievement.formattedCurrentSteps?.let { put("formattedCurrentSteps", it) }
    }
    if (achievement.type == TYPE_INCREMENTAL) {
        achievement.formattedTotalSteps?.let { put("formattedTotalSteps", it) }
    }
    achievement.revealedImageUri?.let {
        put(
            "revealedImageUri",
            it.toStringAndSave(
                godot,
                "revealedImageUri",
                achievement.achievementId
            )
        )
    }
    achievement.unlockedImageUri?.let {
        put(
            "unlockedImageUri",
            it.toStringAndSave(
                godot,
                "unlockedImageUri",
                achievement.achievementId
            )
        )
    }
}