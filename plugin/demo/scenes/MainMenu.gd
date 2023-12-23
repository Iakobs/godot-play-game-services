extends Control

@onready var achievements_button: Button = %Achievements
@onready var leaderboards_button: Button = %Leaderboards
@onready var players_button: Button = %Players
@onready var snapshots_button: Button = %Snapshots

func _ready() -> void:
	SignInClient.user_authenticated.connect(func(is_authenticated: bool):
		if not is_authenticated:
			SignInClient.sign_in()
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
