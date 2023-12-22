## v1.1.0

### Saved Games
- Display saved games in new window
- Save game data

### Save images to device
All properties of leaderboards, achievements, scores and players containing URI to images, where pointing to the `content://` resource, which was not accessible from Godot. This has been fixed, downloading the `content://` resource to the device, in the `user:/` Godot path, making the images available from Godot.

### New signal
New signal [`imageStored`](plugin/src/main/java/com/jacobibanez/plugin/android/godotplaygameservices/signals/Signals.kt), sent when an image is written to the device.

## v1.0.0

### Sign in
- Authenticate
- Sign in

### Achievements
- Increment and unlock achievements
- Reveal achievements
- Load achievements
- Show achievements

### Leaderboards
-  Show leaderboards
- Submit scores
- Load Scores
- Load leaderboards

### Players
- Load friend list
- Compare profiles
- Search players
- Load current player
