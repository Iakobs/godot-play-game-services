package com.jacobibanez.plugin.android.godotplaygameservices.achievements

import com.google.android.gms.games.achievement.Achievement
import com.google.android.gms.games.achievement.Achievement.TYPE_INCREMENTAL
import com.jacobibanez.plugin.android.godotplaygameservices.friends.fromPlayer
import org.godotengine.godot.Dictionary

/** @suppress */
fun fromAchievement(achievement: Achievement): Dictionary = Dictionary().apply {
    put("achievementId", achievement.achievementId)
    put("name", achievement.name)
    put("description", achievement.description)
    put("player", fromPlayer(achievement.player))
    Type.fromType(achievement.type)?.let {
        put("type", it.name)
    }
    State.fromState(achievement.state)?.let {
        put("state", it.name)
    }
    put("xpValue", achievement.xpValue)
    put("revealedImageUri", achievement.revealedImageUri.toString())
    put("unlockedImageUri", achievement.unlockedImageUri.toString())
    put("currentSteps", if (achievement.type == 1) achievement.currentSteps else 0)
    put("totalSteps", if (achievement.type == 1) achievement.totalSteps else 0)
    put(
        "formattedCurrentSteps",
        if (achievement.type == TYPE_INCREMENTAL) achievement.formattedCurrentSteps else ""
    )
    put(
        "formattedTotalSteps",
        if (achievement.type == TYPE_INCREMENTAL) achievement.formattedTotalSteps else ""
    )
    put("lastUpdatedTimestamp", achievement.lastUpdatedTimestamp)
}