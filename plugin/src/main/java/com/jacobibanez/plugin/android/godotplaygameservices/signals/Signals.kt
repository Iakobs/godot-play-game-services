package com.jacobibanez.plugin.android.godotplaygameservices.signals

import org.godotengine.godot.plugin.SignalInfo

/** @suppress */
fun getSignals(): MutableSet<SignalInfo> = mutableSetOf(
    SignInSignals.isUserAuthenticated,
    SignInSignals.isUserSignedIn,

    AchievementsSignals.isAchievementUnlocked,
    AchievementsSignals.loadAchievementsSuccess,
    AchievementsSignals.loadAchievementsFailure,
    AchievementsSignals.revealAchievementSuccess,
    AchievementsSignals.revealAchievementFailure,
    AchievementsSignals.unlockAchievementSuccess,
    AchievementsSignals.unlockAchievementFailure,

    LeaderboardSignals.submitScoreSuccess,
    LeaderboardSignals.submitScoreFailure,

    FriendSignals.loadFriendsSuccess,
    FriendSignals.loadFriendsFailure
)

/**
 * Signals emitted by Sign In methods
 */
object SignInSignals {
    /**
     * This signal is emitted when calling the [com.jacobibanez.plugin.android.godotplaygameservices.GodotAndroidPlugin.isAuthenticated] method.
     *
     * @return `true` if the user is authenticated. `false` otherwise.
     */
    val isUserAuthenticated = SignalInfo("isUserAuthenticated", Boolean::class.javaObjectType)

    /**
     * This signal is emitted when calling the [com.jacobibanez.plugin.android.godotplaygameservices.GodotAndroidPlugin.signIn] method.
     *
     * @return `true` if the user is signed in. `false` otherwise.
     *
     */
    val isUserSignedIn = SignalInfo("isUserSignedIn", Boolean::class.javaObjectType)
}

/**
 * Signals emitted by Achievements methods
 */
object AchievementsSignals {
    /**
     * This signal is emitted when calling the [com.jacobibanez.plugin.android.godotplaygameservices.GodotAndroidPlugin.incrementAchievement] method.
     *
     * @return `true` if the achievement is unlocked. `false` otherwise.
     */
    val isAchievementUnlocked = SignalInfo("isAchievementUnlocked", Boolean::class.javaObjectType)
    val loadAchievementsSuccess = SignalInfo("loadAchievementsSuccess", String::class.java)
    val loadAchievementsFailure = SignalInfo("loadAchievementsFailure")
    val revealAchievementSuccess = SignalInfo("revealAchievement")
    val revealAchievementFailure = SignalInfo("revealAchievement")
    val unlockAchievementSuccess = SignalInfo("unlockAchievementSuccess")
    val unlockAchievementFailure = SignalInfo("unlockAchievementFailure")
}

/**
 * Signals emitted by Leaderboards methods
 */
object LeaderboardSignals {
    val submitScoreSuccess = SignalInfo("submitScoreSuccess")
    val submitScoreFailure = SignalInfo("submitScoreFailure")
}

/**
 * Signals emitted by Friends methods
 */
object FriendSignals {
    val loadFriendsSuccess = SignalInfo("loadFriendsSuccess", String::class.java)
    val loadFriendsFailure = SignalInfo("loadFriendsFailure")
}
