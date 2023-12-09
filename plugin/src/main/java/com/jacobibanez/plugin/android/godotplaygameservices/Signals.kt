package com.jacobibanez.plugin.android.godotplaygameservices

import org.godotengine.godot.plugin.SignalInfo

// Games Sign In
val isUserAuthenticatedSuccess =
    SignalInfo("isUserAuthenticatedSuccess", Boolean::class.javaObjectType)
val isUserAuthenticatedFailure = SignalInfo("isUserAuthenticatedFailure")
val requestServerSideAccessSuccess =
    SignalInfo("requestServerSideAccessSuccess", String::class.java)
val requestServerSideAccessFailure = SignalInfo("requestServerSideAccessFailure")
val signInSuccess = SignalInfo("signInSuccess", Boolean::class.javaObjectType)
val signInFailure = SignalInfo("signInFailure")

// Achievements
val incrementAchievementSuccess =
    SignalInfo("incrementAchievementSuccess", Boolean::class.javaObjectType)
val incrementAchievementSuccessFailure = SignalInfo("incrementAchievementSuccessFailure")
val loadAchievementsSuccess = SignalInfo("loadAchievementsSuccess", String::class.java)
val loadAchievementsFailure = SignalInfo("loadAchievementsFailure")
val revealAchievementSuccess = SignalInfo("revealAchievement")
val revealAchievementFailure = SignalInfo("revealAchievement")
val unlockAchievementSuccess = SignalInfo("unlockAchievementSuccess")
val unlockAchievementFailure = SignalInfo("unlockAchievementFailure")

// Leaderboards
val submitScoreSuccess = SignalInfo("submitScoreSuccess")
val submitScoreFailure = SignalInfo("submitScoreFailure")

// Friends
val loadFriendsSuccess = SignalInfo("loadFriendsSuccess", String::class.java)
val loadFriendsFailure = SignalInfo("loadFriendsFailure")

fun getSignals(): MutableSet<SignalInfo> = mutableSetOf(
    isUserAuthenticatedSuccess,
    isUserAuthenticatedFailure,
    requestServerSideAccessSuccess,
    requestServerSideAccessFailure,
    signInSuccess,
    signInFailure,

    incrementAchievementSuccess,
    incrementAchievementSuccessFailure,
    loadAchievementsSuccess,
    loadAchievementsFailure,
    revealAchievementSuccess,
    revealAchievementFailure,
    unlockAchievementSuccess,
    unlockAchievementFailure,

    submitScoreSuccess,
    submitScoreFailure,

    loadFriendsSuccess,
    loadFriendsFailure
)