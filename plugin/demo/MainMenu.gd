extends Control

@onready var achievements_button: Button = %Achievements

func _ready() -> void:
	achievements_button.pressed.connect(func():
		get_tree().change_scene_to_file("res://Achievements.tscn")
	)
