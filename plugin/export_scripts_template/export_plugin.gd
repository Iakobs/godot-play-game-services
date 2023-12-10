@tool
extends EditorPlugin

const PLUGIN_AUTOLOAD := "GodotPlayGameServices"
const SIGN_IN_AUTOLOAD := "SignInClient"

# A class member to hold the editor export plugin during its lifecycle.
var export_plugin : AndroidExportPlugin
# A class member to hold the dock during the plugin life cycle.
var dock : Node

func _enter_tree():
	# Initialization of the plugin goes here.
	export_plugin = AndroidExportPlugin.new()
	add_export_plugin(export_plugin)
	
	# Load the dock scene and instantiate it.
	dock = preload("res://addons/GodotPlayGameServices/godot_play_game_services_dock.tscn").instantiate()
	# Add the loaded scene to the docks.
	add_control_to_bottom_panel(dock, "Godot Play Game Services")
	
	#Add autoloads
	add_autoload_singleton(PLUGIN_AUTOLOAD, "res://addons/GodotPlayGameServices/autoloads/godot_play_game_services.gd")
	add_autoload_singleton(SIGN_IN_AUTOLOAD, "res://addons/GodotPlayGameServices/autoloads/sign_in_client.gd")

func _exit_tree():
	# Clean-up of the plugin goes here.
	remove_export_plugin(export_plugin)
	export_plugin = null
	
	# Remove the dock.
	remove_control_from_bottom_panel(dock)
	# Erase the control from the memory.
	dock.free()
	
	#Remove autoloads
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
			"com.google.android.gms:play-services-games-v2:19.0.0"
			])
	
	func _get_android_manifest_application_element_contents(platform: EditorExportPlatform, debug: bool) -> String:
		if not _supports_platform(platform):
			return ""
		
		return "<meta-data android:name=\"com.google.android.gms.games.APP_ID\" android:value=\"@string/game_services_project_id\"/>"

	func _get_name():
		return _plugin_name
