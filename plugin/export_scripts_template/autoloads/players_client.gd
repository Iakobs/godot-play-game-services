extends Node
## Client with player functionality.
##
## This autoload exposes methods and signals to control the player information for
## the currently signed in player.

## Signal emitted after calling the [method load_friends] method.[br]
## [br]
## [param friends]: An array containing the friends for the current player.
## The array will be empty if there was an error loading the friends list.
signal friends_loaded(friends: Array[Player])

## Signal emitted after selecting a player in the search window opened by the
## [method search_player] method.[br]
## [br]
## [param player]: The selected player.
signal player_searched(player: Player)

## Signal emitted after calling the [method load_current_player] method.[br]
## [br]
## [param current_player]: The currently signed-in player, or null if there where
## any errors loading the player.
signal current_player_loaded(current_player: Player)

## Friends list visibility statuses.
enum FriendsListVisibilityStatus {
	FEATURE_UNAVAILABLE = 3, ## The friends list is currently unavailable for the game.
	REQUEST_REQUIRED = 2, ## The friends list is not visible to the game, but the game can ask for access.
	UNKNOWN = 0, ## Unknown if the friends list is visible to the game, or whether the game can ask for access from the user.
	VISIBLE = 1 ## The friends list is currently visible to the game.
}

## This player's friend status relative to the currently signed in player.
enum PlayerFriendStatus {
	FRIEND = 4, ## The currently signed in player and this player are friends.
	NO_RELATIONSHIP = 0, ## The currently signed in player is not a friend of this player.
	UNKNOWN = -1 ## The currently signed in player's friend status with this player is unknown.
}

func _ready() -> void:
	_connect_signals()

func _connect_signals() -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.friendsLoaded.connect(func(friends_json: String):
			var safe_array := GodotPlayGameServices.json_marshaller.safe_parse_array(friends_json)
			var friends: Array[Player] = []
			for dictionary: Dictionary in safe_array:
				friends.append(Player.new(dictionary))
			
			friends_loaded.emit(friends)
		)
		GodotPlayGameServices.android_plugin.playerSearched.connect(func(friend_json: String):
			var safe_dictionary := GodotPlayGameServices.json_marshaller.safe_parse_dictionary(friend_json)
			player_searched.emit(Player.new(safe_dictionary))
		)
		GodotPlayGameServices.android_plugin.currentPlayerLoaded.connect(func(friend_json: String):
			var safe_dictionary := GodotPlayGameServices.json_marshaller.safe_parse_dictionary(friend_json)
			current_player_loaded.emit(Player.new(safe_dictionary))
		)

## Use this method and subscribe to the emitted signal to receive the list of friends
## for the current player.[br]
## [br]
## The method emits the [signal friends_loaded] signal.[br]
## [br]
## [param page_size]: The number of entries to request for this initial page.[br]
## [param force_reload]: If true, this call will clear any locally cached 
## data and attempt to fetch the latest data from the server. Send it set to [code]true[/code]
## the first time, and [code]false[/code] in subsequent calls, or when you want
## to clear the cache.[br]
## [param ask_for_permission]: If the user has not granted access to their friends 
## list, and this is set to true, a new window will open asking the user for permission 
## to their friends list.
func load_friends(page_size: int, force_reload: bool, ask_for_permission: bool) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.loadFriends(
			page_size,
			force_reload,
			ask_for_permission
		)

## Displays a screen where the user can see a comparison of their own profile
## against another player's profile.[br]
## [br]
## [param other_player_id]: The player ID of the player to compare with.
func compare_profile(other_player_id: String) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.compareProfile(other_player_id)

## Displays a screen where the user can see a comparison of their own profile
## against another player's profile.[br]
## [br]
## Should be used when the game has its own player names separate from the Play 
## Games Services gamer tag. These names will be used in the profile display and 
## only sent to the server if the player initiates a friend invitation to the 
## profile being viewed, so that the sender and recipient have context relevant 
## to their game experience.[br]
## [br]
## [param other_player_id]: The player ID of the player to compare with.[br]
## [param other_player_in_game_name]: The game's own display name of the player referred to by otherPlayerId.[br]
## [param current_player_in_game_name]: The game's own display name of the current player.
func compare_profile_with_alternative_name_hints(
	other_player_id: String,
	other_player_in_game_name: String,
	current_player_in_game_name: String
) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.compareProfileWithAlternativeNameHints(
			other_player_id,
			other_player_in_game_name,
			current_player_in_game_name
		)

## Displays a screen where the user can search for players. If the user selects 
## a player, then the [signal player_searched] signal will be emitted, returning
## the selected player.
func search_player() -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.searchPlayer()

## Use this method and subscribe to the emitted signal to receive the currently
## signed in player.[br]
## [br]
## The method emits the [signal current_player_loaded] signal.[br]
## [br]
## [param force_reload]: If true, this call will clear any locally cached 
## data and attempt to fetch the latest data from the server. Send it set to [code]true[/code]
## the first time, and [code]false[/code] in subsequent calls, or when you want
## to clear the cache.
func load_current_player(force_reload: bool) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.loadCurrentPlayer(force_reload)

## Player information.
class Player:
	var banner_image_landscape_uri: String ## Banner image of the player in landscape.
	var banner_image_portrait_uri: String ## Banner image of the player in portrait.
	var friends_list_visibility_status: FriendsListVisibilityStatus ## Visibility status of this player's friend list.
	var display_name: String ## The display name of the player.
	var hi_res_image_uri: String ## The hi-res image of the player.
	var icon_image_uri: String ## The icon image of the player.
	var level_info: PlayerLevelInfo ## Information about the player level.
	var player_id: String ## The player id.
	var friend_status: PlayerFriendStatus ## The friend status of this player with the signed in player.
	var retrieved_timestamp: int ## The timestamp at which this player record was last updated locally.
	var title: String ## The title of the player.
	var has_hi_res_image: bool ## Whether this player has a hi-res profile image to display.
	var has_icon_image: bool ## Whether this player has an icon-size profile image to display.
	
	## Constructor that creates a Player from a [Dictionary] containing the properties.
	func _init(dictionary: Dictionary) -> void:
		if dictionary.has("bannerImageLandscapeUri"): banner_image_landscape_uri = dictionary.bannerImageLandscapeUri
		if dictionary.has("bannerImagePortraitUri"): banner_image_portrait_uri = dictionary.bannerImagePortraitUri
		if dictionary.has("friendsListVisibilityStatus"): friends_list_visibility_status = FriendsListVisibilityStatus.get(dictionary.friendsListVisibilityStatus)
		if dictionary.has("displayName"): display_name = dictionary.displayName
		if dictionary.has("hiResImageUri"): hi_res_image_uri = dictionary.hiResImageUri
		if dictionary.has("iconImageUri"): icon_image_uri = dictionary.iconImageUri
		if dictionary.has("levelInfo"): level_info = PlayerLevelInfo.new(dictionary.levelInfo)
		if dictionary.has("playerId"): player_id = dictionary.playerId
		if dictionary.has("friendStatus"): friend_status = PlayerFriendStatus.get(dictionary.friendStatus)
		if dictionary.has("retrievedTimestamp"): retrieved_timestamp = dictionary.retrievedTimestamp
		if dictionary.has("title"): title = dictionary.title
		if dictionary.has("hasHiResImage"): has_hi_res_image = dictionary.hasHiResImage
		if dictionary.has("hasIconImage"): has_icon_image = dictionary.hasIconImage
	
	func _to_string() -> String:
		var result := PackedStringArray()
		
		result.append("banner_image_landscape_uri: %s" % banner_image_landscape_uri)
		result.append("banner_image_portrait_uri: %s" % banner_image_portrait_uri)
		result.append("friends_list_visibility_status: %s" % FriendsListVisibilityStatus.find_key(friends_list_visibility_status))
		result.append("display_name: %s" % display_name)
		result.append("hi_res_image_uri: %s" % hi_res_image_uri)
		result.append("icon_image_uri: %s" % icon_image_uri)
		result.append("level_info: {%s}" % str(level_info))
		result.append("player_id: %s" % player_id)
		result.append("friend_status: %s" % PlayerFriendStatus.find_key(friend_status))
		result.append("retrieved_timestamp: %s" % retrieved_timestamp)
		result.append("title: %s" % title)
		result.append("has_hi_res_image: %s" % has_hi_res_image)
		result.append("has_icon_image: %s" % has_icon_image)
		
		return ", ".join(result)

## The current level information of a player.
class PlayerLevelInfo:
	var current_level: PlayerLevel ## The player's current level object.
	var current_xp_total: int ## The player's current XP value.
	var last_level_up_timestamp: int ## The timestamp of the player's last level-up.
	var next_level: PlayerLevel ## The player's next level object.
	var is_max_level: bool ## True if the player reached the maximum level ([member PlayerLevelInfo.current_level] is the same as [member PlayerLevelInfo.next_level]).
	
	## Constructor that creates a PlayerLevelInfo from a [Dictionary] containing the properties.
	func _init(dictionary: Dictionary) -> void:
		if dictionary.has("currentLevel"): current_level = PlayerLevel.new(dictionary.currentLevel)
		if dictionary.has("currentXpTotal"): current_xp_total = dictionary.currentXpTotal
		if dictionary.has("lastLevelUpTimestamp"): last_level_up_timestamp = dictionary.lastLevelUpTimestamp
		if dictionary.has("nextLevel"): next_level = PlayerLevel.new(dictionary.nextLevel)
		if dictionary.has("isMaxLevel"): is_max_level = dictionary.isMaxLevel
	
	func _to_string() -> String:
		var result := PackedStringArray()
		
		result.append("current_level: {%s}" % str(current_level))
		result.append("current_xp_total: %s" % current_xp_total)
		result.append("last_level_up_timestamp: %s" % last_level_up_timestamp)
		result.append("next_level: {%s}" % str(next_level))
		result.append("is_max_level: %s" % is_max_level)
		
		return ", ".join(result)

## The level of a player.
class PlayerLevel:
	var level_number: int ## The number for this level.
	var max_xp: int ## The maximum XP value represented by this level, exclusive.
	var min_xp: int ## The minimum XP value needed to attain this level, inclusive.
	
	## Constructor that creates a PlayerLevel from a [Dictionary] containing the properties.
	func _init(dictionary: Dictionary) -> void:
		if dictionary.has("levelNumber"): level_number = dictionary.levelNumber
		if dictionary.has("maxXp"): max_xp = dictionary.maxXp
		if dictionary.has("minXp"): min_xp = dictionary.minXp
	
	func _to_string() -> String:
		var result := PackedStringArray()
		
		result.append("level_number: %s" % level_number)
		result.append("max_xp: %s" % max_xp)
		result.append("min_xp: %s" % min_xp)
		
		return ", ".join(result)
