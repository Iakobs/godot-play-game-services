@tool
extends EditorPlugin

const JSON_MARSHALLER_SCRIPT := "res://addons/GodotPlayGameServices/marshalling/json_marshaller.gd"
const PLUGIN_AUTOLOAD := "GodotPlayGameServices"
const SIGN_IN_AUTOLOAD := "SignInClient"
const ACHIEVEMENTS_AUTOLOAD := "AchievementsClient"
const LEADERBOARDS_AUTOLOAD := "LeaderboardsClient"
const PLAYERS_AUTOLOAD := "PlayersClient"
const SNAPSHOTS_AUTOLOAD := "SnapshotsClient"
const EVENTS_AUTOLOAD := "EventsClient"

var _export_plugin : AndroidExportPlugin
var _dock : Node

func _enter_tree():
	add_custom_type("JsonMarshaller", "RefCounted", preload(JSON_MARSHALLER_SCRIPT), null)
	_add_plugin()
	_add_docks()
	_add_autoloads()

func _exit_tree():
	_remove_plugin()
	_remove_docks()
	_remove_autoloads()
	remove_custom_type("JsonMarshaller")

func _add_plugin() -> void:
	_export_plugin = AndroidExportPlugin.new()
	add_export_plugin(_export_plugin)

func _remove_plugin() -> void:
	remove_export_plugin(_export_plugin)
	_export_plugin = null

func _add_docks() -> void:
	var dock_name := "Godot Play Game Services"
	_dock = preload("res://addons/GodotPlayGameServices/godot_play_game_services_dock.tscn").instantiate()
	add_control_to_bottom_panel(_dock, dock_name)

func _remove_docks() -> void:
	remove_control_from_bottom_panel(_dock)
	_dock.free()

func _add_autoloads() -> void:
	add_autoload_singleton(PLUGIN_AUTOLOAD, "res://addons/GodotPlayGameServices/autoloads/godot_play_game_services.gd")
	add_autoload_singleton(SIGN_IN_AUTOLOAD, "res://addons/GodotPlayGameServices/autoloads/sign_in_client.gd")
	add_autoload_singleton(PLAYERS_AUTOLOAD, "res://addons/GodotPlayGameServices/autoloads/players_client.gd")
	add_autoload_singleton(ACHIEVEMENTS_AUTOLOAD, "res://addons/GodotPlayGameServices/autoloads/achievements_client.gd")
	add_autoload_singleton(LEADERBOARDS_AUTOLOAD, "res://addons/GodotPlayGameServices/autoloads/leaderboards_client.gd")
	add_autoload_singleton(SNAPSHOTS_AUTOLOAD, "res://addons/GodotPlayGameServices/autoloads/snapshots_client.gd")
	add_autoload_singleton(EVENTS_AUTOLOAD, "res://addons/GodotPlayGameServices/autoloads/events_client.gd")

func _remove_autoloads() -> void:
	remove_autoload_singleton(EVENTS_AUTOLOAD)
	remove_autoload_singleton(SNAPSHOTS_AUTOLOAD)
	remove_autoload_singleton(LEADERBOARDS_AUTOLOAD)
	remove_autoload_singleton(ACHIEVEMENTS_AUTOLOAD)
	remove_autoload_singleton(PLAYERS_AUTOLOAD)
	remove_autoload_singleton(SIGN_IN_AUTOLOAD)
	remove_autoload_singleton(PLUGIN_AUTOLOAD)

class AndroidExportPlugin extends EditorExportPlugin:
	var _plugin_name = "GodotPlayGameServices"

	func _supports_platform(platform):
		if platform is EditorExportPlatformAndroid:
			return true
		return false

	func _get_android_libraries(platform, debug):
		if debug:
			return PackedStringArray([_plugin_name + "/bin/debug/" + _plugin_name + "-debug.aar"])
		else:
			return PackedStringArray([_plugin_name + "/bin/release/" + _plugin_name + "-release.aar"])
	
	func _get_android_dependencies(platform: EditorExportPlatform, debug: bool) -> PackedStringArray:
		if not _supports_platform(platform):
			return PackedStringArray()
		
		return PackedStringArray([
			"com.google.code.gson:gson:2.10.1", 
			"com.google.android.gms:play-services-games-v2:20.1.2"
			])
	
	func _get_android_manifest_application_element_contents(platform: EditorExportPlatform, debug: bool) -> String:
		if not _supports_platform(platform):
			return ""
		
		return "<meta-data android:name=\"com.google.android.gms.games.APP_ID\" android:value=\"@string/game_services_project_id\"/>"

	func _get_name():
		return _plugin_name
