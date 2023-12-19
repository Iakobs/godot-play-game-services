extends Control

@onready var id_label: Label = %IdLabel
@onready var name_label: Label = %NameLabel
@onready var title_label: Label = %TitleLabel
@onready var status_label: Label = %StatusLabel
@onready var level_label: Label = %LevelLabel
@onready var xp_label: Label = %XpLabel

@onready var compare_button: Button = %CompareButton

var player: PlayersClient.Player

func _ready() -> void:
	if player:
		_set_up_display()
		compare_button.pressed.connect(func():
			PlayersClient.compare_profile(player.player_id)
		)

func _set_up_display() -> void:
	id_label.text = player.player_id
	name_label.text = player.display_name
	title_label.text = player.title
	status_label.text = PlayersClient.PlayerFriendStatus.find_key(player.friend_status)
	level_label.text = str(player.level_info.current_level.level_number)
	xp_label.text = str(player.level_info.current_xp_total)
