extends Control

@onready var back_button: Button = %Back
@onready var show_achievements_button: Button = %ShowAchievements
@onready var reveal_achievements_button: Button = %RevealAchievements

var _achievements_cache: Array[AchievementsClient.Achievement] = []

func _ready() -> void:
	if _achievements_cache.is_empty():
		AchievementsClient.load_achievements(true)
	AchievementsClient.achievements_loaded.connect(
		func cache(achievements: Array[AchievementsClient.Achievement]):
			_achievements_cache = achievements
	)
	
	back_button.pressed.connect(func():
		get_tree().change_scene_to_file("res://scenes/MainMenu.tscn")
	)
	show_achievements_button.pressed.connect(func():
		AchievementsClient.show_achievements()
	)
	reveal_achievements_button.pressed.connect(func():
		for achievement: AchievementsClient.Achievement in _achievements_cache:
			if achievement.state == AchievementsClient.State.STATE_HIDDEN:
				AchievementsClient.reveal_achievement(achievement.achievement_id)
	)
