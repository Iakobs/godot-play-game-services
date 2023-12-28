extends Control

@onready var avatar_rect: TextureRect = %AvatarRect

@onready var id_label: Label = %IdLabel
@onready var name_label: Label = %NameLabel
@onready var title_label: Label = %TitleLabel
@onready var status_label: Label = %StatusLabel
@onready var level_label: Label = %LevelLabel
@onready var xp_label: Label = %XpLabel

@onready var compare_holder: VBoxContainer = %CompareHolder
@onready var compare_button: Button = %CompareButton

var player: PlayersClient.Player
var is_comparable =  false

func _ready() -> void:
	if player:
		_set_up_display()
		compare_button.pressed.connect(func():
			PlayersClient.compare_profile(player.player_id)
		)

func _set_up_display() -> void:
	GodotPlayGameServices.image_stored.connect(func(file_path: String):
		if file_path == player.hi_res_image_uri and not avatar_rect.texture:
			_display_avatar()
	)
	_display_avatar()
	id_label.text = player.player_id
	name_label.text = player.display_name
	title_label.text = player.title
	status_label.text = PlayersClient.PlayerFriendStatus.find_key(player.friend_status)
	level_label.text = str(player.level_info.current_level.level_number)
	xp_label.text = str(player.level_info.current_xp_total)
	compare_holder.visible = is_comparable

func _load_and_retry(image_uri: String) -> Image:
	var image = Image.new()
	var retries := 3
	var error := ERR_FILE_NOT_FOUND
	while retries > 0 or error == ERR_FILE_NOT_FOUND:
		if retries != 3:
			await get_tree().create_timer(1.0).timeout
		error = image.load_png_from_buffer(Image.load_from_file(image_uri).get_data())
		retries -= 1
		if retries == 0:
			print("Error loading file!!")
			image = null
	return image

func _display_avatar() -> void:
	GodotPlayGameServices.display_image_in_texture_rect(
		avatar_rect,
		player.hi_res_image_uri
	)
