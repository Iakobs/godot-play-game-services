extends Control

@onready var back_button: Button = %Back
@onready var show_leaderboards_button: Button = %ShowLeaderboards
@onready var leaderboard_displays: VBoxContainer = %LeaderboardDisplays

var _leaderboards_cache: Array[LeaderboardsClient.Leaderboard] = []
var _leaderboard_display := preload("res://scenes/leaderboards/LeaderboardDisplay.tscn")

func _ready() -> void:
	if _leaderboards_cache.is_empty():
		LeaderboardsClient.load_all_leaderboards(true)
	LeaderboardsClient.all_leaderboards_loaded.connect(
		func cache_and_display(leaderboards: Array[LeaderboardsClient.Leaderboard]):
			_leaderboards_cache = leaderboards
			if not _leaderboards_cache.is_empty():
				for leaderboard: LeaderboardsClient.Leaderboard in _leaderboards_cache:
					var container := _leaderboard_display.instantiate() as Control
					container.leaderboard = leaderboard
					leaderboard_displays.add_child(container)
	)
	
	back_button.pressed.connect(func():
		get_tree().change_scene_to_file("res://scenes/MainMenu.tscn")
	)
	show_leaderboards_button.pressed.connect(func():
		LeaderboardsClient.show_all_leaderboards()
	)
	
