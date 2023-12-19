extends Control

@onready var back_button: Button = %Back

@onready var friends_display: VBoxContainer = %FriendsDisplay

var _friends_cache: Array[PlayersClient.Player] = []
var _friend_display := preload("res://scenes/players/FriendDisplay.tscn")

func _ready() -> void:
	if _friends_cache.is_empty():
		PlayersClient.load_friends(10, true, true)
	PlayersClient.friends_loaded.connect(
		func cache_and_display(friends: Array[PlayersClient.Player]):
			_friends_cache = friends
			if not _friends_cache.is_empty() and friends_display.get_child_count() == 0:
				for friend: PlayersClient.Player in _friends_cache:
					var container := _friend_display.instantiate() as Control
					container.friend = friend
					friends_display.add_child(container)
	)
	
	back_button.pressed.connect(func():
		get_tree().change_scene_to_file("res://scenes/MainMenu.tscn")
	)
