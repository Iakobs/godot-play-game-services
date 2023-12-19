package com.jacobibanez.plugin.android.godotplaygameservices.players

import com.google.android.gms.games.Player

/**
 * Friends list visibility statuses.
 */
enum class FriendsListVisibilityStatus(
    /**
     * Integer representation of the statuses, taken from [com.google.android.gms.games.Player.FriendsListVisibilityStatus](https://developers.google.com/android/reference/com/google/android/gms/games/Player.FriendsListVisibilityStatus)
     */
    val status: Int
) {
    /**
     * The friends list is currently unavailable for the game.
     */
    FEATURE_UNAVAILABLE(Player.FriendsListVisibilityStatus.FEATURE_UNAVAILABLE),

    /**
     * The friends list is not visible to the game, but the game can ask for access.
     */
    REQUEST_REQUIRED(Player.FriendsListVisibilityStatus.REQUEST_REQUIRED),

    /**
     * Unknown if the friends list is visible to the game, or whether the game can ask for access from the user.
     */
    UNKNOWN(Player.FriendsListVisibilityStatus.UNKNOWN),

    /**
     * The friends list is currently visible to the game.
     */
    VISIBLE(Player.FriendsListVisibilityStatus.VISIBLE);

    companion object {
        /**
         * Returns a [FriendsListVisibilityStatus] given its integer representation, or null if the type is invalid.
         */
        fun fromStatus(type: Int): FriendsListVisibilityStatus? = when (type) {
            Player.FriendsListVisibilityStatus.FEATURE_UNAVAILABLE -> FEATURE_UNAVAILABLE
            Player.FriendsListVisibilityStatus.REQUEST_REQUIRED -> REQUEST_REQUIRED
            Player.FriendsListVisibilityStatus.UNKNOWN -> UNKNOWN
            Player.FriendsListVisibilityStatus.VISIBLE -> VISIBLE
            else -> null
        }
    }
}

/**
 * This player's friend status relative to the currently signed in player.
 */
enum class PlayerFriendStatus(
    /**
     * Integer representation of the status, taken from [com.google.android.gms.games.Player.PlayerFriendStatus](https://developers.google.com/android/reference/com/google/android/gms/games/PlayerRelationshipInfo#getFriendStatus())
     */
    val status: Int
) {
    /**
     * The currently signed in player and this player are friends.
     */
    FRIEND(Player.PlayerFriendStatus.FRIEND),

    /**
     * The currently signed in player is not a friend of this player.
     */
    NO_RELATIONSHIP(Player.PlayerFriendStatus.NO_RELATIONSHIP),

    /**
     * The currently signed in player's friend status with this player is unknown.
     */
    UNKNOWN(Player.PlayerFriendStatus.UNKNOWN);

    companion object {
        /**
         * Returns a [PlayerFriendStatus] given its integer representation, or null if the type is invalid.
         */
        fun fromStatus(type: Int): PlayerFriendStatus? = when (type) {
            Player.PlayerFriendStatus.FRIEND -> FRIEND
            Player.PlayerFriendStatus.NO_RELATIONSHIP -> NO_RELATIONSHIP
            Player.PlayerFriendStatus.UNKNOWN -> UNKNOWN
            else -> null
        }
    }
}