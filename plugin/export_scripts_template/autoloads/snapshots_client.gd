extends Node
## Client with save and load games functionality.
##
## This autoload exposes methods and signals to save and load games in the Google cloud of the player.

## Signal emitted after calling the [method save_game] method.[br]
## [br]
## [param is_saved]: Wheter if the game was saved or not.[br]
## [param save_data_name]: The save file name.[br]
## [param save_data_description]: The save file description.
signal game_saved(is_saved: bool, save_data_name: String, save_data_description: String)

## Signal emitted after calling the [method load_game] method or after selecting
## a saved game in the window presented after calling the [method show_saved_games]
## method.[br]
## [br]
## [param saved_data]: The contents of the saved data.
signal game_loaded(saved_data: String)

## Constant passed to the [method show_saved_games] method to not limit the number of displayed saved files.  
const DISPLAY_LIMIT_NONE := -1

func _ready() -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.gameSaved.connect(
			func(is_saved: bool, save_data_name: String, save_data_description: String):
				game_saved.emit(is_saved, save_data_name, save_data_description)
		)
		GodotPlayGameServices.android_plugin.gameLoaded.connect(func(saved_data: String):
			game_loaded.emit(saved_data)
		)

## Opens a new window to display the saved games for the current player. If you select
## a saved game, the [signal game_loaded] signal will be emitted.[br]
## [br]
## [param title]: The title to display in the action bar of the returned Activity.[br]
## [param allow_add_button]: Whether or not to display a "create new snapshot" option in the selection UI.[br]
## [param allow_delete]: Whether or not to provide a delete overflow menu option for each snapshot in the selection UI.[br]
## [param max_snapshots]: The maximum number of snapshots to display in the UI. Use [constant DISPLAY_LIMIT_NONE] to display all snapshots.
func show_saved_games(
	title: String, 
	allow_add_button: bool, 
	allow_delete: bool, 
	max_snapshots: int
) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.showSavedGames(title, allow_add_button, allow_delete, max_snapshots)

## Saves game data to the Google Cloud.[br]
## [br]
## This method emits the [signal game_saved] signal.[br]
## [br]
## [param fileName]: The name of the save file. Must be between 1 and 100 non-URL-reserved characters (a-z, A-Z, 0-9, or the symbols "-", ".", "_", or "~").[br]
## [param saveData]: A String with the saved data of the game.[br]
## [param description]: The description of the save file.
func save_game(file_name: String, save_data: String, description: String) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.saveGame(file_name, save_data, description)

## Loads game data from the Google Cloud.[br]
## [br]
## This method emits the [signal game_loaded] signal.[br]
## [br]
## [param fileName]: The name of the save file. Must be between 1 and 100 non-URL-reserved charactes (a-z, A-Z, 0-9, or the symbols "-", ".", "_", or "~").
func load_game(file_name: String):
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.loadGame(file_name)
