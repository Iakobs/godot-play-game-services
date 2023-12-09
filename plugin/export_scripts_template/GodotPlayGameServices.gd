extends Node

signal is_user_authenticated_success

var _plugin_name := "GodotPlayGameServices"

var android_plugin: Object

func _ready() -> void:
	if Engine.has_singleton(_plugin_name):
		print("Plugin found!")
		
		android_plugin = Engine.get_singleton(_plugin_name)
		android_plugin.initialize()
		
		android_plugin.isUserAuthenticatedSuccess.connect(func(is_authenticated:bool):
			is_user_authenticated_success.emit(is_authenticated)
		)
	else:
		printerr("No Google Play Game Services android plugin found!")
