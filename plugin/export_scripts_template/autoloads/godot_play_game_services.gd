extends Node

var android_plugin: Object

func _ready() -> void:
	var plugin_name := "GodotPlayGameServices"
	
	if not android_plugin:
		if Engine.has_singleton(plugin_name):
			print("Plugin found!")
			
			android_plugin = Engine.get_singleton(plugin_name)
			android_plugin.initialize()
		else:
			printerr("No plugin found!")
