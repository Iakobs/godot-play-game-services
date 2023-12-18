extends Node
## Client with sign in functionality.
##
## This autoload exposes methods and signals to control the player sign in process.[br]
## [br]
## If the player is already signed in into Google Play Games, the plugin does
## a check at startup, so usually you don't have to use these methods. Use them only 
## to provide a manual way for the user to sign in.

## Signal emitted after calling the [method is_authenticated] method.[br]
## [br]
## [param is_authenticated]: Indicates if the user is authenticated or not.
signal user_authenticated(is_authenticated: bool)
## Signal emitted after calling the [method sign_in] method.[br]
## [br]
## [param is_signed_in]: Indicates if the user is signed in or not.
signal user_signed_in(is_signed_in: bool)

func _ready() -> void:
	_connect_signals()

func _connect_signals() -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.userAuthenticated.connect(func(is_authenticated: bool):
			user_authenticated.emit(is_authenticated)
		)
		GodotPlayGameServices.android_plugin.userSignedIn.connect(func(is_signed_in: bool):
			user_signed_in.emit(is_signed_in)
		)

## Use this method to check if the user is already authenticated. If the user is authenticated,
## a popup will be shown on screen.[br]
## [br]
## The method emits the [signal user_authenticated] signal.
func is_authenticated() -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.isAuthenticated()

## Use this method to provide a manual way to the user for signing in.[br]
## [br]
## The method emits the [signal user_signed_in] signal.
func sign_in() -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.signIn()
