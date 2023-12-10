extends Node

signal is_user_authenticated
signal is_user_signed_in

func _ready() -> void:
	connect_signals()

func connect_signals() -> void:
	GodotPlayGameServices.android_plugin.isUserAuthenticated.connect(func(is_authenticated: bool):
		is_user_authenticated.emit(is_authenticated)
	)
	GodotPlayGameServices.android_plugin.isUserSignedIn.connect(func(is_signed_in: bool):
		is_user_signed_in.emit(is_signed_in)
	)

func is_authenticated() -> void:
	GodotPlayGameServices.android_plugin.isAuthenticated()

func sign_in() -> void:
	GodotPlayGameServices.android_plugin.signIn()
