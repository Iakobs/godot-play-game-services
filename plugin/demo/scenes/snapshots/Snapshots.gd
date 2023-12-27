extends Control

@onready var back_button: Button = %Back

@onready var load_button: Button = %LoadButton

@onready var name_line_edit: LineEdit = %NameLineEdit
@onready var description_line_edit: LineEdit = %DescriptionLineEdit
@onready var value_text_edit: TextEdit = %ValueTextEdit
@onready var save_game_button: Button = %SaveGameButton

var _current_name: String
var _current_data: String
var _current_description: String

func _ready() -> void:
	back_button.pressed.connect(func():
		get_tree().change_scene_to_file("res://scenes/MainMenu.tscn")
	)
	load_button.pressed.connect(func():
		SnapshotsClient.show_saved_games("Saved Games", false, true, SnapshotsClient.DISPLAY_LIMIT_NONE)
	)
	_connect_save_game_data()

func _connect_save_game_data() -> void:
	name_line_edit.text_changed.connect(func(_new_text: String):
		_validate_save_game_data()
	)
	description_line_edit.text_changed.connect(func(_new_text: String):
		_validate_save_game_data()
	)
	value_text_edit.text_changed.connect(func():
		_validate_save_game_data()
	)
	save_game_button.pressed.connect(func():
		_set_current_save_game_data()
		_reset_save_game_data()
		SnapshotsClient.save_game(
			_current_name,
			_current_description,
			_current_data.to_utf8_buffer()
		)
	)
	SnapshotsClient.game_saved.connect(
		func(is_saved: bool, save_data_name: String, save_data_description: String):
			if is_saved and save_data_name == _current_name\
			and save_data_description == _current_description:
				pass
	)
	SnapshotsClient.game_loaded.connect(
		func(snapshot: SnapshotsClient.Snapshot):
			_reset_save_game_data()
			
			var metadata = snapshot.metadata
			var data = snapshot.content.get_string_from_utf8()
			
			_current_name = metadata.unique_name
			_current_description = metadata.description
			_current_data = data
			
			name_line_edit.text = _current_name
			description_line_edit.text = _current_description
			value_text_edit.text = _current_data
	)

func _validate_save_game_data() -> void:
	var data_is_valid := not name_line_edit.text.is_empty()\
	and not description_line_edit.text.is_empty()\
	and not value_text_edit.text.is_empty()
	
	save_game_button.disabled = not data_is_valid

func _set_current_save_game_data() -> void:
	_current_name = name_line_edit.text
	_current_data = value_text_edit.text
	_current_description = description_line_edit.text

func _reset_save_game_data() -> void:
	name_line_edit.text = ""
	value_text_edit.text = ""
	description_line_edit.text = ""
	save_game_button.disabled = true
