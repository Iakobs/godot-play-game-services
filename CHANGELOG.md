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
