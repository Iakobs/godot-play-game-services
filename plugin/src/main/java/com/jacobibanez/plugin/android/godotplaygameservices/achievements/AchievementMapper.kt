package com.jacobibanez.plugin.android.godotplaygameservices.achievements

import com.google.android.gms.games.achievement.Achievement
import com.google.android.gms.games.achievement.Achievement.STATE_HIDDEN
import com.google.android.gms.games.achievement.Achievement.STATE_REVEALED
import com.google.android.gms.games.achievement.Achievement.STATE_UNLOCKED
import com.google.android.gms.games.achievement.Achievement.TYPE_INCREMENTAL
import com.google.android.gms.games.achievement.Achievement.TYPE_STANDARD
import org.godotengine.godot.Dictionary

fun fromAchievement(achievement: Achievement?): Dictionary = if (achievement != null) {
    Dictionary().apply {
        put("achievementId", achievement.achievementId)
        put("name", achievement.name)
        put("description", achievement.description)
        put("type", getType(achievement.type))
        put("state", getState(achievement.state))
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
} else {
    Dictionary()
}

private fun getType(type: Int): String = when (type) {
    TYPE_STANDARD -> "TYPE_STANDARD"
    TYPE_INCREMENTAL -> "TYPE_INCREMENTAL"
    else -> ""
}

private fun getState(state: Int): String = when (state) {
    STATE_UNLOCKED -> "STATE_UNLOCKED"
    STATE_REVEALED -> "STATE_REVEALED"
    STATE_HIDDEN -> "STATE_HIDDEN"
    else -> ""
}