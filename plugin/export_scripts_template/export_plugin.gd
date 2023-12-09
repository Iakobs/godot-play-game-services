@tool
extends EditorPlugin

# A class member to hold the editor export plugin during its lifecycle.
var export_plugin : AndroidExportPlugin

func _enter_tree():
	# Initialization of the plugin goes here.
	export_plugin = AndroidExportPlugin.new()
	add_export_plugin(export_plugin)

	#Add autoloads
	add_autoload_singleton("GodotPlayGameServices", "res://addons/GodotPlayGameServices/GodotPlayGameServices.gd")

func _exit_tree():
	# Clean-up of the plugin goes here.
	remove_export_plugin(export_plugin)
	export_plugin = null

	#Remove autoloads
	remove_autoload_singleton("GodotPlayGameServices")

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

	func _get_name():
		return _plugin_name
