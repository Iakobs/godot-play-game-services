extends Node
## Client with achievements functionality.
##
## This autoload exposes methods and signals to control the game achievements for
## the currently signed in player.

## Signal emitted after calling the [method increment_achievement] 
## or [method unlock_achievement] methods.[br]
## [br]
## [param is_unlocked]: Indicates if the achievement is unlocked or not.[br]
## [param achievement_id]: The achievement id.
signal achievement_unlocked(is_unlocked: bool, achievement_id: String)

## Signal emitted after calling the [method load_achievements] method.[br]
## [br]
## [param achievements]: An array containing all the achievements for the game.
## The array will be empty if there was an error loading the achievements.
signal achievements_loaded(achievements: Array[Achievement])

## Signal emitted after calling the [method reveal_achievement] method.[br]
## [br]
## [param is_revealed]: Indicates if the achievement is revealed or not.[br]
## [param achievement_id]: The achievement id.
signal achievement_revealed(is_revealed: bool, achievement_id: String)

## Achievement type.
enum Type {
	TYPE_STANDARD = 0, ## A standard achievement.
	TYPE_INCREMENTAL = 1 ## An incremental achievement.
}

## Achievement state.
enum State {
	STATE_UNLOCKED = 0, ## An unlocked achievement.
	STATE_REVEALED = 1, ## A revealed achievement.
	STATE_HIDDEN = 2 ## A hidden achievement.
}

func _ready() -> void:
	_connect_signals()

func _connect_signals() -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.achievementUnlocked.connect(func(is_unlocked: bool, achievement_id: String):
			achievement_unlocked.emit(is_unlocked, achievement_id)
		)
		GodotPlayGameServices.android_plugin.achievementsLoaded.connect(func(achievements_json: String):
			var safe_array := GodotPlayGameServices.json_marshaller.safe_parse_array(achievements_json)
			var achievements: Array[Achievement] = []
			for dictionary: Dictionary in safe_array:
				achievements.append(Achievement.new(dictionary))
			
			achievements_loaded.emit(achievements)
		)
		GodotPlayGameServices.android_plugin.achievementRevealed.connect(func(is_revealed: bool, achievement_id: String):
			achievement_revealed.emit(is_revealed, achievement_id)
		)

## Use this method to increment a given achievement in the given amount. For normal 
## achievements, use the [method unlock_achievement] method instead.[br]
## [br]
## The method emits the [signal achievement_unlocked] signal.[br]
## [br]
## [param achievement_id]: The achievement id.[br]
## [param amount]: The number of steps to increment by. Must be greater than 0.
func increment_achievement(achievement_id: String, amount: int) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.incrementAchievement(achievement_id, amount)

## Use this method and subscribe to the emitted signal to receive the list of the game
## achievements.[br]
## [br]
## The method emits the [signal achievements_loaded] signal.[br]
## [br]
## [param force_reload]: If true, this call will clear any locally cached 
## data and attempt to fetch the latest data from the server. Send it set to [code]true[/code]
## the first time, and [code]false[/code] in subsequent calls, or when you want
## to clear the cache.
func load_achievements(force_reload: bool) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.loadAchievements(force_reload)

## Use this method to reveal a hidden achievement to the current signed in player. 
## If the achievement is already unlocked, this method will have no effect.[br]
## [br]
## The method emits the [signal achievement_revealed] signal.[br]
## [br]
## [param achievement_id]: The achievement id.
func reveal_achievement(achievement_id: String) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.revealAchievement(achievement_id)

## Use this method to open a new window with the achievements of the game, and 
## the progress of the player made so far to unlock those achievements.
func show_achievements() -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.showAchievements()

## Immediately unlocks the given achievement for the signed in player. If the 
## achievement is secret, it will be revealed to the player.[br]
## [br]
## The method emits the [signal achievement_unlocked] signal.[br]
## [br]
## [param achievement_id]: The achievement id.
func unlock_achievement(achievement_id: String) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.unlockAchievement(achievement_id)

## A class representing an achievement.
class Achievement:
	var achievement_id: String ## The achievement id.
	var achievement_name: String ## The achievement name.
	var description: String ## The description of the achievement.
	var player: PlayersClient.Player ## The player associated to this achievement.
	var type: Type ## The achievement type.
	var state: State ## The achievement state.
	var xp_value: int ## The XP value of this achievement.
	var revealed_image_uri: String ## A URI that can be used to load the achievement's revealed image icon.
	var unlocked_image_uri: String ## A URI that can be used to load the achievement's unlocked image icon.
	## The number of steps this user has gone toward unlocking this achievement;
	## only applicable for [code]TYPE_INCREMENTAL[/code] achievement types.
	var current_steps: int
	## Retrieves the total number of steps necessary to unlock this achievement; 
	## only applicable for [code]TYPE_INCREMENTAL[/code] achievement types.
	var total_steps: int
	## Retrieves the number of steps this user has gone toward unlocking this
	## achievement, formatted for the user's locale; only applicable for 
	## [code]TYPE_INCREMENTAL[/code] achievement types.
	var formatted_current_steps: String
	## Loads the total number of steps necessary to unlock this achievement,
	## formatted for the user's local; only applicable for [code]TYPE_INCREMENTAL[/code] 
	## achievement types.
	var formatted_total_steps: String
	## Retrieves the timestamp (in millseconds since epoch) at which this achievement 
	## was last updated.
	var last_updated_timestamp: int
	
	## Constructor that creates an Achievement from a [Dictionary] containing the properties.
	func _init(dictionary: Dictionary) -> void:
		if dictionary.has("achievementId"): achievement_id = dictionary.achievementId
		if dictionary.has("name"): achievement_name = dictionary.name
		if dictionary.has("description"): description = dictionary.description
		if dictionary.has("player"): player = PlayersClient.Player.new(dictionary.player)
		if dictionary.has("type"): type = Type[dictionary.type]
		if dictionary.has("state"): state = State[dictionary.state]
		if dictionary.has("xpValue"): xp_value = dictionary.xpValue
		if dictionary.has("revealedImageUri"): revealed_image_uri = dictionary.revealedImageUri
		if dictionary.has("unlockedImageUri"): unlocked_image_uri = dictionary.unlockedImageUri
		if dictionary.has("currentSteps"): current_steps = dictionary.currentSteps
		if dictionary.has("totalSteps"): total_steps = dictionary.totalSteps
		if dictionary.has("formattedCurrentSteps"): formatted_current_steps = dictionary.formattedCurrentSteps
		if dictionary.has("formattedTotalSteps"): formatted_total_steps = dictionary.formattedTotalSteps
		if dictionary.has("lastUpdatedTimestamp"): last_updated_timestamp = dictionary.lastUpdatedTimestamp
	
	func _to_string() -> String:
		var result := PackedStringArray()
		
		result.append("achievement_id: %s" % achievement_id)
		result.append("achievement_name: %s" % achievement_name)
		result.append("description: %s" % description)
		result.append("player: {%s}" % str(player))
		result.append("type: %s" % Type.find_key(type))
		result.append("state: %s" % State.find_key(state))
		result.append("xp_value: %s" % xp_value)
		result.append("revealed_image_uri: %s" % revealed_image_uri)
		result.append("unlocked_image_uri: %s" % unlocked_image_uri)
		result.append("current_steps: %s" % current_steps)
		result.append("total_steps: %s" % total_steps)
		result.append("formatted_current_steps: %s" % formatted_current_steps)
		result.append("formatted_total_steps: %s" % formatted_total_steps)
		result.append("last_updated_timestamp: %s" % last_updated_timestamp)
		
		return ", ".join(result)
