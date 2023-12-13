extends Node
## Client with  achievements functionality.
##
## This autoload exposes methods and signals to control the game achievements for
## the currently signed in player.

## Signal emitted after calling the [code]increment_achievement()[/code] 
## and [code]unlock_achievement()[/code] methods.[br]
## [br]
## [param is_unlocked]: Indicates if the achievement is unlocked or not.[br]
## [param achievement_id]: The achievement id.
signal achievement_unlocked(is_unlocked: bool, achievement_id: String)
## Signal emitted after calling the [code]load_achievements()[/code] method.[br]
## [br]
## [param achievements]: An array containing all the achievements for the game.
signal achievements_loaded(achievements: Array[Achievement])
## Signal emitted after calling the [code]reveal_achievement()[/code] method.[br]
## [br]
## [param is_revealed]: Indicates if the achievement is revealed or not.[br]
## [param achievement_id]: The achievement id.
signal achievement_revealed(is_revealed: bool, achievement_id: String)

func _ready() -> void:
	_connect_signals()

func _connect_signals() -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.achievementUnlocked.connect(func(is_unlocked: bool, achievement_id: String):
			achievement_unlocked.emit(is_unlocked, achievement_id)
		)
		GodotPlayGameServices.android_plugin.achievementsLoaded.connect(func(achievements_json: String):
			var json = JSON.new()
			var error = json.parse(achievements_json)
			if error == OK:
				var data_received = json.data
				if typeof(data_received) == TYPE_ARRAY:
					var mapped_array: Array[Achievement] = []
					for achievement: Dictionary in data_received:
						mapped_array.append(Achievement.new(achievement))
					achievements_loaded.emit(mapped_array)
				else:
					printerr("Unexpected data")
			else:
				printerr("JSON Parse Error: ", json.get_error_message(), " in ", achievements_json, " at line ", json.get_error_line())
		)
		GodotPlayGameServices.android_plugin.achievementRevealed.connect(func(is_revealed: bool, achievement_id: String):
			achievement_revealed.emit(is_revealed, achievement_id)
		)

## Use this method to increment a given achievement in the given amount. For normal 
## achievements, use the [code]unlockAchievement[/code] method instead.[br]
##
## [br]The method emits the [code]achievement_unlocked[/code] signal.[br]
##
## [br][param achievement_id]: The achievement id.
## [br][param amount]: The number of steps to increment by. Must be greater than 0.
func increment_achievement(achievement_id: String, amount: int) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.incrementAchievement(achievement_id, amount)

## Use this method and subscribe to the emitted signal to receive the list of the game
## achievements.[br]
## [br]
## The method emits the [code]achievements_loaded[/code] signal.
## [br]
## [br][param force_reload]: If true, this call will clear any locally cached 
## data and attempt to fetch the latest data from the server.
func load_achievements(force_reload: bool) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.loadAchievements(force_reload)

## Use this method to reveal a hidden achievement to the current signed player. 
## If the achievement is already unlocked, this method will have no effect.[br]
## [br]
## The method emits the [code]achievement_revealed[/code] signal.
## [br]
## [br][param achievement_id]: The achievement id.
func reveal_achievement(achievement_id: String) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.revealAchievement(achievement_id)

## Use this method to open a new window with the achievements of the game, and 
## the progress of the player to unlock those achievements.
func show_achievements() -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.showAchievements()

## Immediately unlocks the given achievement for the signed in player. If the 
## achievement is secret, it will be revealed to the player.[br]
## [br]
## The method emits the [code]achievement_unlocked[/code] signal.
## [br]
## [br][param achievement_id]: The achievement id.
func unlock_achievement(achievement_id: String) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.unlockAchievement(achievement_id)

## A class representing an achievement
class Achievement:
	
	enum Type {
		TYPE_STANDARD,
		TYPE_INCREMENTAL
	}
	
	enum State {
		STATE_UNLOCKED,
		STATE_REVEALED,
		STATE_HIDDEN
	}
	
	var achievement_id: String
	var achievement_name: String
	var description: String 
	var type: Type
	var state: State
	var xp_value: int
	var revealed_image_uri: String
	var unlocked_image_uri: String
	var current_steps: int
	var total_steps: int
	var formatted_current_steps: String
	var formatted_total_steps: String
	var last_updated_timestamp: int
	
	func _init(dictionary: Dictionary) -> void:
		achievement_id = dictionary.achievementId
		achievement_name = dictionary.name
		description = dictionary.description
		type = Type[dictionary.type]
		state = State[dictionary.state]
		xp_value = dictionary.xpValue
		revealed_image_uri = dictionary.revealedImageUri
		unlocked_image_uri = dictionary.unlockedImageUri
		current_steps = dictionary.currentSteps
		total_steps = dictionary.totalSteps
		formatted_current_steps = dictionary.formattedCurrentSteps
		formatted_total_steps = dictionary.formattedTotalSteps
		last_updated_timestamp = dictionary.lastUpdatedTimestamp
	
	func _to_string() -> String:
		var result := PackedStringArray()
		
		result.append("achievement_id: %s" % achievement_id)
		result.append("achievement_name: %s" % achievement_name)
		result.append("description: %s" % description)
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
