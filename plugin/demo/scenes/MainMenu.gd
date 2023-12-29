extends Control

@onready var title_label: Label = %TitleLabel
@onready var achievements_button: Button = %Achievements
@onready var leaderboards_button: Button = %Leaderboards
@onready var players_button: Button = %Players
@onready var snapshots_button: Button = %Snapshots

var _sign_in_retries := 5

func _ready() -> void:
	if not GodotPlayGameServices.android_plugin:
		title_label.text = "Plugin Not Found!"
	
	SignInClient.user_authenticated.connect(func(is_authenticated: bool):
		if _sign_in_retries > 0 and not is_authenticated:
			title_label.text = "Trying to sign in!"
			SignInClient.sign_in()
			_sign_in_retries -= 1
		
		if _sign_in_retries == 0:
			title_label.text = "Sign in attemps expired!"
		
		if is_authenticated:
			title_label.text = "Main Menu"
	)
	achievements_button.pressed.connect(func():
		get_tree().change_scene_to_file("res://scenes/achievements/Achievements.tscn")
	)
	leaderboards_button.pressed.connect(func():
		get_tree().change_scene_to_file("res://scenes/leaderboards/Leaderboards.tscn")
	)
	players_button.pressed.connect(func():
		get_tree().change_scene_to_file("res://scenes/players/Players.tscn")
	)
	snapshots_button.pressed.connect(func():
		get_tree().change_scene_to_file("res://scenes/snapshots/Snapshots.tscn")
	)
