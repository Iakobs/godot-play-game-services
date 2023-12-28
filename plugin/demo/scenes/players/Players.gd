extends Control

@onready var back_button: Button = %Back

@onready var search_button: Button = %SearchButton
@onready var search_display: VBoxContainer = %SearchDisplay

@onready var current_player_display: VBoxContainer = %CurrentPlayerDisplay

@onready var friends_display: VBoxContainer = %FriendsDisplay

var _current_player: PlayersClient.Player
var _friends_cache: Array[PlayersClient.Player] = []
var _player_display := preload("res://scenes/players/PlayerDisplay.tscn")

func _ready() -> void:
	if not _current_player:
		PlayersClient.load_current_player(true)
	PlayersClient.current_player_loaded.connect(func(current_player: PlayersClient.Player):
		var container := _player_display.instantiate() as Control
		container.player = current_player
		current_player_display.add_child(container)
	)
	if _friends_cache.is_empty():
		PlayersClient.load_friends(10, true, true)
	PlayersClient.friends_loaded.connect(
		func cache_and_display(friends: Array[PlayersClient.Player]):
			_friends_cache = friends
			if not _friends_cache.is_empty() and friends_display.get_child_count() == 0:
				for friend: PlayersClient.Player in _friends_cache:
					var container := _player_display.instantiate() as Control
					container.player = friend
					friends_display.add_child(container)
	)
	PlayersClient.player_searched.connect(func(player: PlayersClient.Player):
		for child in search_display.get_children():
			child.queue_free()
		var container := _player_display.instantiate() as Control
		container.player = player
		container.is_comparable = true
		search_display.add_child(container)
	)
	
	back_button.pressed.connect(func():
		get_tree().change_scene_to_file("res://scenes/MainMenu.tscn")
	)
	search_button.pressed.connect(func():
		PlayersClient.search_player()
	)
