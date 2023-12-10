extends Control

@onready var button: Button = %Button

func _ready() -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.is_user_authenticated.connect(func(is_authenticated: bool):
			print("Is user authenticated?: %s" % is_authenticated)
		)
		button.pressed.connect(func():
			GodotPlayGameServices.android_plugin.showAllLeaderboards()
		)
