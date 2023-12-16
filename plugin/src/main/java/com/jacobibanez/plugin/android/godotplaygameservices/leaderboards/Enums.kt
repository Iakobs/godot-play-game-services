package com.jacobibanez.plugin.android.godotplaygameservices.leaderboards

import com.google.android.gms.games.leaderboard.Leaderboard
import com.google.android.gms.games.leaderboard.LeaderboardVariant

/**
 * Time span for leaderboards.
 */
enum class TimeSpan(
    /**
     * Integer representation of the time span, taken from [com.google.android.gms.games.leaderboard.LeaderboardVariant](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/LeaderboardVariant)
     */
    val span: Int
) {
    /**
     * A leaderboard that resets everyday.
     */
    TIME_SPAN_DAILY(LeaderboardVariant.TIME_SPAN_DAILY),

    /**
     * A leaderboard that resets every week.
     */
    TIME_SPAN_WEEKLY(LeaderboardVariant.TIME_SPAN_WEEKLY),

    /**
     * A leaderboard that never resets.
     */
    TIME_SPAN_ALL_TIME(LeaderboardVariant.TIME_SPAN_ALL_TIME);

    companion object {
        /**
         * Returns a [TimeSpan] given its integer representation, or null if the span is invalid.
         */
        fun fromSpan(span: Int): TimeSpan? = when (span) {
            LeaderboardVariant.TIME_SPAN_DAILY -> TIME_SPAN_DAILY
            LeaderboardVariant.TIME_SPAN_WEEKLY -> TIME_SPAN_WEEKLY
            LeaderboardVariant.TIME_SPAN_ALL_TIME -> TIME_SPAN_ALL_TIME
            else -> null
        }
    }
}

/**
 * Collection type for leaderboards.
 */
enum class Collection(
    /**
     * Integer representation of the collection, taken from [com.google.android.gms.games.leaderboard.LeaderboardVariant](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/LeaderboardVariant)
     */
    val type: Int
) {
    /**
     * A public leaderboard.
     */
    COLLECTION_PUBLIC(LeaderboardVariant.COLLECTION_PUBLIC),

    /**
     * Only friends leaderboard.
     */
    COLLECTION_FRIENDS(LeaderboardVariant.COLLECTION_FRIENDS);

    companion object {
        /**
         * Returns a [Collection] given its integer representation, or null if the type is invalid.
         */
        fun fromType(type: Int): Collection? = when (type) {
            LeaderboardVariant.COLLECTION_PUBLIC -> COLLECTION_PUBLIC
            LeaderboardVariant.COLLECTION_FRIENDS -> COLLECTION_FRIENDS
            else -> null
        }
    }
}

/**
 * The order of a leaderboard.
 */
enum class ScoreOrder(
/**
 * Integer representation of the collection, taken from [com.google.android.gms.games.leaderboard.LeaderboardVariant](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/LeaderboardVariant)
 */
val order: Int
) {
    /**
     * Score order constant for leaderboards where scores are sorted in descending order.
     */
    SCORE_ORDER_LARGER_IS_BETTER(Leaderboard.SCORE_ORDER_LARGER_IS_BETTER),

    /**
     * Score order constant for leaderboards where scores are sorted in ascending order.
     */
    SCORE_ORDER_SMALLER_IS_BETTER(Leaderboard.SCORE_ORDER_SMALLER_IS_BETTER);

    companion object {
        /**
         * Returns a [ScoreOrder] given its integer representation, or null if the order is invalid.
         */
        fun fromOrder(order: Int): ScoreOrder? = when(order) {
            Leaderboard.SCORE_ORDER_LARGER_IS_BETTER -> SCORE_ORDER_LARGER_IS_BETTER
            Leaderboard.SCORE_ORDER_SMALLER_IS_BETTER -> SCORE_ORDER_SMALLER_IS_BETTER
            else -> null
        }
    }
}