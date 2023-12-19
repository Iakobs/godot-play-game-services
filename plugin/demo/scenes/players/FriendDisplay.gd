extends Control

@onready var id_label: Label = %IdLabel
@onready var name_label: Label = %NameLabel
@onready var title_label: Label = %TitleLabel
@onready var status_label: Label = %StatusLabel
@onready var level_label: Label = %LevelLabel
@onready var xp_label: Label = %XpLabel

@onready var compare_button: Button = %CompareButton

var friend: PlayersClient.Player

func _ready() -> void:
	if friend:
		_set_up_display()
		compare_button.pressed.connect(func():
			PlayersClient.compare_profile_with(friend.player_id)
		)

func _set_up_display() -> void:
	id_label.text = friend.player_id
	name_label.text = friend.display_name
	title_label.text = friend.title
	status_label.text = PlayersClient.PlayerFriendStatus.find_key(friend.friend_status)
	level_label.text = str(friend.level_info.current_level.level_number)
	xp_label.text = str(friend.level_info.current_xp_total)
