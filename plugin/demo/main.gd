extends Node2D

var _plugin_name = "GodotPlayGameServices"
var _android_plugin

func _ready():
	if Engine.has_singleton(_plugin_name):
		_android_plugin = Engine.get_singleton(_plugin_name)
		_android_plugin.initialize()
	else:
		printerr("Couldn't find plugin " + _plugin_name)

func _on_Button_pressed():
	if _android_plugin:
		_android_plugin.showAllLeaderboards()
