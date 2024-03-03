## v1.7.0
### Send JSON strings instead of Dictionaries
The `gameLoaded` and `conflictEmitted` signals where sending an instance of the Dictionary class. Instead, they send a JSON string now, as suggested by @nepalisameer in [this issue](https://github.com/Iakobs/godot-play-game-services/issues/22).

## v1.6.0
### Load Snapshots
The [loadPlayerCenteredScores](https://developers.google.com/android/reference/com/google/android/gms/games/SnapshotsClient#load(boolean)) method from Google's API has been added, returning the list of Snapshots for the current signed in player.

### Fix crash when loading non existing save game
When calling the `loadGame` method with a non existing file name, the app crashed. This is fixed now, the app just prints a log with the error and continues execution.

### Add new parameter to loadGame method
Added the `createIfNotFound` parameter to the `loadGame` method, with a default value of `false` to not break backwards compatibility. This parameter creates a new snapshot if the file name does not exist.

### Add method to delete snapshots
Added new method to delete snapshots by snapshot id.

### Add methods for events API
Added three new methods for events API:
- incrementEvent
- loadEvents
- loadEventsByIds

## v1.5.0
### Order of autoloads
The autoloads where causing errors on first launch of the project, due to the load order and dependencies between them. The load order has now been fixed to avoid this errors. Also, the plugin is now disabled by default in the demo project. Look at the [demo project documentation](https://github.com/Iakobs/godot-play-game-services/tree/main/plugin/demo) for further info.

### Load player centered scores
The [loadPlayerCenteredScores](https://developers.google.com/android/reference/com/google/android/gms/games/LeaderboardsClient#loadPlayerCenteredScores(java.lang.String,%20int,%20int,%20int,%20boolean)) method from Google's API has been added to the plugin. This method returns a list of scores, centered in the signed in player, for a given leaderboard. There's no example in the demo app yet, but it's usage is documented in the code itself.

### Load top scores
The [loadTopScores](https://developers.google.com/android/reference/com/google/android/gms/games/LeaderboardsClient#loadTopScores(java.lang.String,%20int,%20int,%20int,%20boolean)) method from Google's API has been added to the plugin. This method returns the top scores of a leaderboard for a given leaderboard. There's no example in the demo app yet, but it's usage is documented in the code itself.

## v1.4.0
### Increase max length of Game ID
In the new dock that the plugin adds to the bottom bar, the max length of the Game ID has been increased from 12 to 20 characters.

Also, the input of the user is now converted to a number, stripping any letters or symbols from it.

Thanks to @godfryd for pointing this issue out in [this issue](https://github.com/Iakobs/godot-play-game-services/issues/13).

## v1.3.0
### Add method to request a server side access token
Added the `requestServerSideAccess` method to the plugin to request a server side access token, in case the users of the plugin want to develop a backend to communicate with the [web REST API](https://developers.google.com/games/services/web/api/rest) that Google provides.

Thanks to @AndrewSumsion for the [suggestion](https://github.com/Iakobs/godot-play-game-services/issues/10).

## v1.2.1
### Rename the `Game` class in `snapshots_client.gd` to `GameInfo`
The `Game` inner class in the `SnapshotsClient` autoload was renamed to `GameInfo` to avoid possible common clashes with games who might already use that name in their own namespace, as suggested by @g-libardi in [this issue](https://github.com/Iakobs/godot-play-game-services/issues/9).

## v1.2.0
### Deprecated signal
The `userSignedIn` signal is deprecated in favor of the already existing `userAuthenticated` signal. Now both `isAuthenticated` and `signIn` methods, emit the same signal.

### Fake Logging
Added some fake logging to the main menu. The Title of the screen shows different messages to show some common problems, like plugin not loaded or user not signed in.

### Bug fixing
The `leaderboards_client.gd` script had a wrong call to the android plugin in the `show_leaderboard` function. Thanks to [mrbut1995](https://github.com/Iakobs/godot-play-game-services/issues/5) for finding it and notifying!

## v1.1.0
### Saved Games
- Display saved games in new window that allows to load and delete saved games.
- Save game data.
- Load game data.
- Receive conflicting game data (not possible to resolve the conflict manually yet).

### Save images to device
All properties of leaderboards, achievements, scores and players containing URI to images, where pointing to the `content://` resource, which was not accessible from Godot. This has been fixed, downloading the `content://` resource to the device, in the `user:/` Godot path, making the images available from Godot.

### New signal
New signal [`imageStored`](plugin/src/main/java/com/jacobibanez/plugin/android/godotplaygameservices/signals/Signals.kt), sent when an image is written to the device.

## v1.0.0
### Sign in
- Authenticate.
- Sign in.

### Achievements
- Increment and unlock achievements.
- Reveal achievements.
- Load achievements.
- Show achievements.

### Leaderboards
- Show leaderboards.
- Submit scores.
- Load Scores.
- Load leaderboards.

### Players
- Load friend list.
- Compare profiles.
- Search players.
- Load current player.
