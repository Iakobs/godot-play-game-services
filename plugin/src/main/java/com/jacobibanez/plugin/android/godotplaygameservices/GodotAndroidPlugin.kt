package com.jacobibanez.plugin.android.godotplaygameservices

import android.util.Log
import com.google.android.gms.games.PlayGamesSdk
import com.jacobibanez.plugin.android.godotplaygameservices.achievements.AchievementsProxy
import com.jacobibanez.plugin.android.godotplaygameservices.friends.FriendsProxy
import com.jacobibanez.plugin.android.godotplaygameservices.leaderboards.LeaderboardsProxy
import com.jacobibanez.plugin.android.godotplaygameservices.signin.SignInProxy
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

class GodotAndroidPlugin(godot: Godot) : GodotPlugin(godot) {

    override fun getPluginName() = BuildConfig.GODOT_PLUGIN_NAME

    private val signInProxy = SignInProxy(godot)
    private val achievementsProxy = AchievementsProxy(godot)
    private val leaderboardsProxy = LeaderboardsProxy(godot)
    private val friendsProxy = FriendsProxy(godot)

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        return getSignals()
    }

    @UsedByGodot
    private fun initialize() {
        Log.d(pluginName, "Initializing Google Play Game Services")
        PlayGamesSdk.initialize(activity!!)
        isAuthenticated()
    }

    @UsedByGodot
    private fun isAuthenticated() = signInProxy.isAuthenticated()

    @UsedByGodot
    private fun requestServerSideAccess(serverClientId: String, forceRefreshToken: Boolean) =
        signInProxy.requestServerSideAccess(serverClientId, forceRefreshToken)

    @UsedByGodot
    private fun signIn() = signInProxy.signIn()

    @UsedByGodot
    private fun incrementAchievement(achievementId: String, amount: Int) =
        achievementsProxy.incrementAchievement(achievementId, amount)

    @UsedByGodot
    private fun loadAchievements(forceReload: Boolean) =
        achievementsProxy.loadAchievements(forceReload)

    @UsedByGodot
    private fun revealAchievement(achievementId: String) =
        achievementsProxy.revealAchievement(achievementId)

    @UsedByGodot
    private fun showAchievements() = achievementsProxy.showAchievements()

    @UsedByGodot
    private fun unlockAchievement(achievementId: String) =
        achievementsProxy.unlockAchievement(achievementId)

    @UsedByGodot
    private fun showAllLeaderboards() = leaderboardsProxy.showAllLeaderboards()

    @UsedByGodot
    private fun showLeaderboard(leaderboardId: String) =
        leaderboardsProxy.showLeaderboard(leaderboardId)

    @UsedByGodot
    private fun showLeaderboardForTimeSpan(leaderboardId: String, timeSpan: Int) =
        leaderboardsProxy.showLeaderboardForTimeSpan(leaderboardId, timeSpan)

    @UsedByGodot
    private fun showLeaderboardForTimeSpanAndCollection(
        leaderboardId: String,
        timeSpan: Int,
        collection: Int
    ) = leaderboardsProxy.showLeaderboardForTimeSpanAndCollection(
        leaderboardId, timeSpan, collection
    )

    @UsedByGodot
    private fun submitScore(leaderboardId: String, score: Int) =
        leaderboardsProxy.submitScore(leaderboardId, score)

    @UsedByGodot
    private fun loadFriends(pageSize: Int, forceReload: Boolean) =
        friendsProxy.loadFriends(pageSize, forceReload)

    @UsedByGodot
    private fun compareProfile(otherPlayerId: String) = friendsProxy.compareProfile(otherPlayerId)

    @UsedByGodot
    private fun compareProfileWithAlternativeNameHints(
        otherPlayerId: String,
        otherPlayerInGameName: String,
        currentPlayerInGameName: String
    ) = friendsProxy.compareProfileWithAlternativeNameHints(
        otherPlayerId,
        otherPlayerInGameName,
        currentPlayerInGameName
    )
}
