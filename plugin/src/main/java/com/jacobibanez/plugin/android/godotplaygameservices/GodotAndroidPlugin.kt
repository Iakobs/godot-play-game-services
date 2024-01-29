package com.jacobibanez.plugin.android.godotplaygameservices

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import com.google.android.gms.games.PlayGamesSdk
import com.jacobibanez.plugin.android.godotplaygameservices.achievements.AchievementsProxy
import com.jacobibanez.plugin.android.godotplaygameservices.leaderboards.LeaderboardsProxy
import com.jacobibanez.plugin.android.godotplaygameservices.players.PlayersProxy
import com.jacobibanez.plugin.android.godotplaygameservices.signals.getSignals
import com.jacobibanez.plugin.android.godotplaygameservices.signin.SignInProxy
import com.jacobibanez.plugin.android.godotplaygameservices.snapshots.SnapshotsProxy
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

/**
 * This is the main Godot Plugin class exposing the interfaces to use with Godot. In this class you
 * will find all the methods that can be called in your game via GDScript.
 */
class GodotAndroidPlugin(godot: Godot) : GodotPlugin(godot) {

    /** @suppress */
    override fun getPluginName() = BuildConfig.GODOT_PLUGIN_NAME

    private val signInProxy = SignInProxy(godot)
    private val achievementsProxy = AchievementsProxy(godot)
    private val leaderboardsProxy = LeaderboardsProxy(godot)
    private val playersProxy = PlayersProxy(godot)
    private val snapshotsProxy = SnapshotsProxy(godot)

    /** @suppress */
    override fun getPluginSignals(): MutableSet<SignalInfo> {
        return getSignals()
    }

    /** @suppress */
    override fun onMainCreate(activity: Activity?): View? {
        Thread.setDefaultUncaughtExceptionHandler { _, exception ->
            Log.e(pluginName, "Uncaught Exception! ${exception.message}")
        }
        return super.onMainCreate(activity)
    }

    /** @suppress */
    override fun onMainActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onMainActivityResult(requestCode, resultCode, data)
        playersProxy.onActivityResult(requestCode, resultCode, data)
        snapshotsProxy.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * This method initializes the Play Games SDK. It should be called right after checking that
     * the plugin is loaded into Godot, for example:
     *
     * ```
     * func _ready() -> void:
     * 	if Engine.has_singleton(_plugin_name):
     * 		print("Plugin found!")
     * 		var android_plugin := Engine.get_singleton(_plugin_name)
     * 		android_plugin.initialize()
     * 	else:
     * 		printerr("No plugin found!")
     *```
     *
     * If the user has automatic sign-in enabled, the initialization will check for authentication.
     */
    @UsedByGodot
    fun initialize() {
        Log.d(pluginName, "Initializing Google Play Game Services")
        PlayGamesSdk.initialize(activity!!)
        isAuthenticated()
    }

    /**
     * Use this method to check if the user is already authenticated. If the user is authenticated,
     * a popup will be shown on screen.
     *
     * The method emits the [com.jacobibanez.plugin.android.godotplaygameservices.signals.SignInSignals.userAuthenticated] signal.
     */
    @UsedByGodot
    fun isAuthenticated() = signInProxy.isAuthenticated()

    /**
     * Use this method to provide a manual way to the user for signing in.
     *
     * The method emits the [com.jacobibanez.plugin.android.godotplaygameservices.signals.SignInSignals.userAuthenticated] signal.
     */
    @UsedByGodot
    fun signIn() = signInProxy.signIn()

    /**
     * Requests server-side access to Play Games Services for the currently signed-in player.
     *
     * When requested, an authorization code is returned that can be used by your server to exchange
     * for an access token that can be used by your server to access the Play Games Services web APIs.
     *
     * @param serverClientId The client ID of the server that will perform the authorization code flow exchange.
     * @param forceRefreshToken If true, when the returned authorization code is exchanged, a refresh
     * token will be included in addition to an access token.
     *
     */
    @UsedByGodot
    fun requestServerSideAccess(serverClientId: String, forceRefreshToken: Boolean) =
        signInProxy.signInRequestServerSideAccess(serverClientId, forceRefreshToken)

    /**
     * Use this method to increment a given achievement in the given amount. For normal achievements,
     * use the [unlockAchievement] method instead.
     *
     * The method emits the [com.jacobibanez.plugin.android.godotplaygameservices.signals.AchievementsSignals.achievementUnlocked] signal.
     *
     * @param achievementId The achievement id.
     * @param amount The number of steps to increment by. Must be greater than 0.
     */
    @UsedByGodot
    fun incrementAchievement(achievementId: String, amount: Int) =
        achievementsProxy.incrementAchievement(achievementId, amount)

    /**
     * Call this method and subscribe to the emitted signal to receive the list of the game
     * achievements in JSON format. The JSON received from the [com.jacobibanez.plugin.android.godotplaygameservices.signals.AchievementsSignals.achievementsLoaded]
     * signal contains a representation of the [com.google.android.gms.games.achievement.Achievement](https://developers.google.com/android/reference/com/google/android/gms/games/achievement/Achievement) class.
     *
     * @param forceReload If true, this call will clear any locally cached data and attempt to fetch
     * the latest data from the server.
     */
    @UsedByGodot
    fun loadAchievements(forceReload: Boolean) =
        achievementsProxy.loadAchievements(forceReload)

    /**
     * Use this method to reveal a hidden achievement to the current signed player. If the achievement
     * is already unlocked, this method will have no effect.
     *
     * The method emits the [com.jacobibanez.plugin.android.godotplaygameservices.signals.AchievementsSignals.achievementRevealed] signal.
     *
     * @param achievementId The achievement id.
     */
    @UsedByGodot
    fun revealAchievement(achievementId: String) =
        achievementsProxy.revealAchievement(achievementId)

    /**
     * Use this method to open a new window with the achievements of the game, and the progress of
     * the player to unlock those achievements.
     */
    @UsedByGodot
    fun showAchievements() = achievementsProxy.showAchievements()

    /**
     * Immediately unlocks the given achievement for the signed in player. If the achievement is
     * secret, it will be revealed to the player.
     *
     * The method emits the [com.jacobibanez.plugin.android.godotplaygameservices.signals.AchievementsSignals.achievementUnlocked] signal.
     *
     * @param achievementId The achievement id.
     */
    @UsedByGodot
    fun unlockAchievement(achievementId: String) =
        achievementsProxy.unlockAchievement(achievementId)

    /**
     * Shows a new screen with all the leaderboards for the game.
     */
    @UsedByGodot
    fun showAllLeaderboards() = leaderboardsProxy.showAllLeaderboards()

    /**
     * Shows a new screen for a specific leaderboard.
     *
     * @param leaderboardId The leaderboard id.
     */
    @UsedByGodot
    fun showLeaderboard(leaderboardId: String) =
        leaderboardsProxy.showLeaderboard(leaderboardId)

    /**
     * Shows a specific leaderboard for a given span of time.
     *
     * @param leaderboardId The leaderboard id.
     * @param timeSpan The time span for the leaderboard refresh. It can be any of the [com.jacobibanez.plugin.android.godotplaygameservices.leaderboards.TimeSpan] values.
     */
    @UsedByGodot
    fun showLeaderboardForTimeSpan(leaderboardId: String, timeSpan: Int) =
        leaderboardsProxy.showLeaderboardForTimeSpan(leaderboardId, timeSpan)

    /**
     * Shows a specific leaderboard for a given span of time and a specific type of collection.
     *
     * @param leaderboardId The leaderboard id.
     * @param timeSpan The time span for the leaderboard refresh. It can be any of the [com.jacobibanez.plugin.android.godotplaygameservices.leaderboards.TimeSpan] values.
     * @param collection The collection type for the leaderboard. It can be any of the [[com.jacobibanez.plugin.android.godotplaygameservices.leaderboards.Collection]] values.
     */
    @UsedByGodot
    fun showLeaderboardForTimeSpanAndCollection(
        leaderboardId: String,
        timeSpan: Int,
        collection: Int
    ) = leaderboardsProxy.showLeaderboardForTimeSpanAndCollection(
        leaderboardId, timeSpan, collection
    )

    /**
     * Submits the score to the leaderboard for the currently signed-in player. The score is ignored
     * if it is worse (as defined by the leaderboard configuration) than a previously submitted
     * score for the same player.
     *
     * @param leaderboardId The leaderboard id.
     * @param score The raw score value. For more details, please [see this page](https://developers.google.com/games/services/common/concepts/leaderboards).
     */
    @UsedByGodot
    fun submitScore(leaderboardId: String, score: Int) =
        leaderboardsProxy.submitScore(leaderboardId, score)

    /**
     * Call this method and subscribe to the emitted signal to receive the score of the currently
     * signed in player for the given leaderboard, time span, and collection in JSON format.
     * The JSON received from the [com.jacobibanez.plugin.android.godotplaygameservices.signals.LeaderboardSignals.scoreLoaded]
     * signal contains a representation of the [com.google.android.gms.games.leaderboard.LeaderboardScore](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/LeaderboardScore) class.
     *
     * @param leaderboardId The leaderboard id.
     * @param timeSpan The time span for the leaderboard refresh. It can be any of the [com.jacobibanez.plugin.android.godotplaygameservices.leaderboards.TimeSpan] values.
     * @param collection The collection type for the leaderboard. It can be any of the [[com.jacobibanez.plugin.android.godotplaygameservices.leaderboards.Collection]] values.
     */
    @UsedByGodot
    fun loadPlayerScore(
        leaderboardId: String,
        timeSpan: Int,
        collection: Int
    ) = leaderboardsProxy.loadPlayerScore(leaderboardId, timeSpan, collection)

    /**
     * Call this method and subscribe to the emitted signal to receive all leaderboards for the
     * game in JSON format. The JSON received from the [com.jacobibanez.plugin.android.godotplaygameservices.signals.LeaderboardSignals.allLeaderboardsLoaded]
     * signal, contains a list of elements representing the [com.google.android.gms.games.leaderboard.Leaderboard](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/Leaderboard) class.
     *
     * @param forceReload If true, this call will clear any locally cached data and attempt to fetch
     * the latest data from the server.
     */
    @UsedByGodot
    fun loadAllLeaderboards(forceReload: Boolean) =
        leaderboardsProxy.loadAllLeaderboards(forceReload)

    /**
     * Call this method and subscribe to the emitted signal to receive the specific leaderboard in
     * JSON format. The JSON received from the [com.jacobibanez.plugin.android.godotplaygameservices.signals.LeaderboardSignals.leaderboardLoaded]
     * signal, contains a representation of the [com.google.android.gms.games.leaderboard.Leaderboard](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/Leaderboard) class.
     *
     * @param leaderboardId The leaderboard id.
     * @param forceReload If true, this call will clear any locally cached data and attempt to fetch
     * the latest data from the server.
     */
    @UsedByGodot
    fun loadLeaderboard(leaderboardId: String, forceReload: Boolean) =
        leaderboardsProxy.loadLeaderboard(leaderboardId, forceReload)

    /**
     * Call this method and subscribe to the emitted signal to receive the list of friends for the
     * currently signed in player in JSON format. The JSON received from the [com.jacobibanez.plugin.android.godotplaygameservices.signals.PlayerSignals.friendsLoaded]
     * signal, contains a list of elements representing the [com.google.android.gms.games.Player](https://developers.google.com/android/reference/com/google/android/gms/games/Player) class.
     *
     * @param pageSize The number of entries to request for this initial page.
     * @param forceReload If true, this call will clear any locally cached data and attempt to fetch
     * the latest data from the server.
     * @param askForPermission If the user has not granted access to their friends list, and this
     * is set to true, a new window will open asking the user for permission to their friends list.
     */
    @UsedByGodot
    fun loadFriends(pageSize: Int, forceReload: Boolean, askForPermission: Boolean) =
        playersProxy.loadFriends(pageSize, forceReload, askForPermission)

    /**
     * Displays a screen where the user can see a comparison of their own profile against another
     * player's profile.
     *
     * @param otherPlayerId The player ID of the player to compare with.
     */
    @UsedByGodot
    fun compareProfile(otherPlayerId: String) = playersProxy.compareProfile(otherPlayerId)

    /**
     * Displays a screen where the user can see a comparison of their own profile against another
     * player's profile.
     *
     * Should be used when the game has its own player names separate from the Play Games Services
     * gamer tag. These names will be used in the profile display and only sent to the server if the
     * player initiates a friend invitation to the profile being viewed, so that the sender and
     * recipient have context relevant to their game experience.
     *
     * @param otherPlayerId The player ID of the player to compare with.
     * @param otherPlayerInGameName The game's own display name of the player referred to by otherPlayerId.
     * @param currentPlayerInGameName The game's own display name of the current player.
     */
    @UsedByGodot
    fun compareProfileWithAlternativeNameHints(
        otherPlayerId: String,
        otherPlayerInGameName: String,
        currentPlayerInGameName: String
    ) = playersProxy.compareProfileWithAlternativeNameHints(
        otherPlayerId,
        otherPlayerInGameName,
        currentPlayerInGameName
    )

    /**
     * Displays a screen where the user can search for players. If the user selects a player, then
     * the [com.jacobibanez.plugin.android.godotplaygameservices.signals.PlayerSignals.playerSearched]
     * signal will be emitted, returning the selected player.
     */
    @UsedByGodot
    fun searchPlayer() = playersProxy.searchPlayer()

    /**
     * Call this method and subscribe to the emitted signal to receive the currently signed in player
     * in JSON format.The JSON received from the [com.jacobibanez.plugin.android.godotplaygameservices.signals.PlayerSignals.currentPlayerLoaded]
     * signal, contains a representation of the [com.google.android.gms.games.Player](https://developers.google.com/android/reference/com/google/android/gms/games/Player) class.
     *
     * @param forceReload If true, this call will clear any locally cached data and attempt to fetch
     * the latest data from the server.
     */
    @UsedByGodot
    fun loadCurrentPlayer(forceReload: Boolean) = playersProxy.loadCurrentPlayer(forceReload)

    /**
     * Opens a new window to display the saved games for the current player. If you select one of the
     * saved games, the [com.jacobibanez.plugin.android.godotplaygameservices.signals.SnapshotSignals.gameLoaded]
     * signal will be emitted.
     *
     * @param title The title to display in the action bar of the returned Activity.
     * @param allowAddButton Whether or not to display a "create new snapshot" option in the selection UI.
     * @param allowDelete Whether or not to provide a delete overflow menu option for each snapshot in the selection UI.
     * @param maxSnapshots The maximum number of snapshots to display in the UI. Use [DISPLAY_LIMIT_NONE](https://developers.google.com/android/reference/com/google/android/gms/games/SnapshotsClient#DISPLAY_LIMIT_NONE) to display all snapshots.
     */
    @UsedByGodot
    fun showSavedGames(
        title: String,
        allowAddButton: Boolean,
        allowDelete: Boolean,
        maxSnapshots: Int
    ) = snapshotsProxy.showSavedGames(title, allowAddButton, allowDelete, maxSnapshots)

    /**
     * Saves game data to the Google Cloud. The [com.jacobibanez.plugin.android.godotplaygameservices.signals.SnapshotSignals.gameSaved]
     * signal will be emitted after saving the game.
     *
     * @param fileName The name of the save file. Must be between 1 and 100 non-URL-reserved
     * characters (a-z, A-Z, 0-9, or the symbols "-", ".", "_", or "~").
     * @param description The description of the save file.
     * @param saveData A ByteArray with the saved data of the game.
     * @param playedTimeMillis The played time of this snapshot in milliseconds.
     * @param progressValue The progress value for this snapshot.
     */
    @UsedByGodot
    fun saveGame(
        fileName: String,
        description: String,
        saveData: ByteArray,
        playedTimeMillis: Long,
        progressValue: Long
    ) = snapshotsProxy.saveGame(fileName, description, saveData, playedTimeMillis, progressValue)

    /**
     * Loads game data from the Google Cloud. This method emits the [com.jacobibanez.plugin.android.godotplaygameservices.signals.SnapshotSignals.gameLoaded]
     * signal.
     *
     * @param fileName The name of the save file. Must be between 1 and 100 non-URL-reserved
     * characters (a-z, A-Z, 0-9, or the symbols "-", ".", "_", or "~").
     */
    @UsedByGodot
    fun loadGame(fileName: String) = snapshotsProxy.loadGame(fileName)
}
