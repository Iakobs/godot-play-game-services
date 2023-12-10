extends Node

signal is_user_authenticated
signal is_user_signed_in

var android_plugin: Object

var _plugin_name := "GodotPlayGameServices"

func _ready() -> void:
	if Engine.has_singleton(_plugin_name):
		print("Plugin found!")

		android_plugin = Engine.get_singleton(_plugin_name)
		android_plugin.initialize()

		connect_signals()
	else:
		printerr("No plugin found!")

func connect_signals() -> void:
	android_plugin.isUserAuthenticated.connect(func(is_authenticated: bool):
		is_user_authenticated.emit(is_authenticated)
	)
	android_plugin.isUserSignedIn.connect(func(is_signed_in: bool):
		is_user_signed_in.emit(is_signed_in)
	)

func is_authenticated() -> void:
	android_plugin.isAuthenticated()

func sign_in() -> void:
	android_plugin.signIn()
