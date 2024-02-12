@tool
extends Node

const CONFIG_FILE_NAME := "user://game_services.cfg"
const CONFIG_SECTION := "GAME_SERVICES"
const GAME_ID_KEY := "GAME_ID"

@onready var game_id: LineEdit = %GameId
@onready var submit: Button = %Submit

var _config = ConfigFile.new()

func _ready() -> void:
	_load_config()
	
	if _config:
		game_id.text = _config.get_value(CONFIG_SECTION, GAME_ID_KEY, "")
	
	submit.pressed.connect(func():
		_save_config()
		var file := FileAccess.open("android/build/res/values/strings.xml", FileAccess.WRITE)
		file.store_string("<?xml version=\"1.0\" encoding=\"utf-8\"?><resources><string translatable=\"false\" name=\"game_services_project_id\">%s</string></resources>" % game_id.text)
	)

func _load_config() -> void:
	var err := _config.load(CONFIG_FILE_NAME)
	if err != OK:
		printerr("Error loading configuration for Game Services! Error: %s" % err)

func _save_config() -> void:
	var clean_user_input := str(game_id.text.to_int())
	game_id.text = clean_user_input
	if _config:
		_config.set_value(CONFIG_SECTION, GAME_ID_KEY, clean_user_input)
		_config.save(CONFIG_FILE_NAME)
