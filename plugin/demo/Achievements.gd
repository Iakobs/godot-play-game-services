extends Control

@onready var back_button: Button = %Back
@onready var show_achievements_button: Button = %ShowAchievements
@onready var reveal_achievements_button: Button = %RevealAchievements

var achievements: Array[AchievementsClient.Achievement] = []

func _ready() -> void:
	AchievementsClient.achievements_loaded.connect(func(_achievements: Array[AchievementsClient.Achievement]):
		self.achievements = _achievements
	)
	if achievements.is_empty():
		AchievementsClient.load_achievements(true)
	
	back_button.pressed.connect(func():
		get_tree().change_scene_to_file("res://MainMenu.tscn")
	)
	show_achievements_button.pressed.connect(func():
		AchievementsClient.show_achievements()
	)
	reveal_achievements_button.pressed.connect(func():
		for achievement: AchievementsClient.Achievement in achievements:
			if achievement.state == AchievementsClient.Achievement.State.STATE_HIDDEN:
				AchievementsClient.reveal_achievement(achievement.achievement_id)
	)
