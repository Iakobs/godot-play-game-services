package com.jacobibanez.plugin.android.godotplaygameservices.achievements

import com.google.android.gms.games.achievement.Achievement
import com.jacobibanez.plugin.android.godotplaygameservices.achievements.Type.TYPE_INCREMENTAL
import com.jacobibanez.plugin.android.godotplaygameservices.achievements.Type.TYPE_STANDARD

/**
 * Type of achievements.
 *
 * [TYPE_STANDARD]
 * [TYPE_INCREMENTAL]
 */
enum class Type(
    /**
     * Integer representation of the achievement type, taken from [com.google.android.gms.games.achievement.Achievement](https://developers.google.com/android/reference/com/google/android/gms/games/achievement/Achievement)
     */
    val type: Int
) {
    /**
     * A standard achievement.
     */
    TYPE_STANDARD(Achievement.TYPE_STANDARD),

    /**
     * An incremental achievement.
     */
    TYPE_INCREMENTAL(Achievement.TYPE_INCREMENTAL);

    companion object {
        /**
         * Returns a [Type] given its integer representation, or null if the type is invalid.
         */
        fun fromType(type: Int): Type? = when (type) {
            Achievement.TYPE_STANDARD -> TYPE_STANDARD
            Achievement.TYPE_INCREMENTAL -> TYPE_INCREMENTAL
            else -> null
        }
    }
}

/**
 * State for achievements.
 */
enum class State(
    /**
     * Integer representation of the achievement state, taken from [com.google.android.gms.games.achievement.Achievement](https://developers.google.com/android/reference/com/google/android/gms/games/achievement/Achievement)
     */
    val state: Int
) {
    /**
     * An unlocked achievement.
     */
    STATE_UNLOCKED(Achievement.STATE_UNLOCKED),

    /**
     * A revealed achievement.
     */
    STATE_REVEALED(Achievement.STATE_REVEALED),

    /**
     * A hidden achievement.
     */
    STATE_HIDDEN(Achievement.STATE_HIDDEN);

    companion object {
        /**
         * Returns a [State] given its integer representation, or null if the state is invalid.
         */
        fun fromState(state: Int): State? = when (state) {
            Achievement.STATE_UNLOCKED -> STATE_UNLOCKED
            Achievement.STATE_REVEALED -> STATE_REVEALED
            Achievement.STATE_HIDDEN -> STATE_HIDDEN
            else -> null
        }
    }
}