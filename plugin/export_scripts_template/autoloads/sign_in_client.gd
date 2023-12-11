extends Node

signal user_authenticated
signal user_signed_in

func _ready() -> void:
	connect_signals()

func connect_signals() -> void:
	GodotPlayGameServices.android_plugin.userAuthenticated.connect(func(is_authenticated: bool):
		user_authenticated.emit(is_authenticated)
	)
	GodotPlayGameServices.android_plugin.userSignedIn.connect(func(is_signed_in: bool):
		user_signed_in.emit(is_signed_in)
	)

func is_authenticated() -> void:
	GodotPlayGameServices.android_plugin.isAuthenticated()

func sign_in() -> void:
	GodotPlayGameServices.android_plugin.signIn()
