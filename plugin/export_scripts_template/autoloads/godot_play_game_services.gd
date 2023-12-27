extends Node
## Main Autoload of the plugin, which contains a reference to the android plugin itself.
##
## This Autoload contains the entrypoint to the android code, but you don't need
## to use it directly. Some autoloads exposing the plugin functionality, as a wrapper
## for GDScript code, are also loaded with the plugin.[br]
## [br]
## This Autoload also calls the [code]initialize()[/code] method of the plugin,
## checking if the user is authenticated.

## Signal emitted after an image is downloaded and saved to the device.[br]
## [br]
## [param file_path]: The path to the stored file.
signal image_stored(file_path: String)

## Main entry point to the android plugin. With this object, you can call the 
## kotlin methods directly.
var android_plugin: Object

## A helper JSON marshaller to safely access JSON data from the plugin.
var json_marshaller := JsonMarshaller.new()

func _ready() -> void:
	var plugin_name := "GodotPlayGameServices"
	
	if not android_plugin:
		if Engine.has_singleton(plugin_name):
			print("Plugin found!")
			
			android_plugin = Engine.get_singleton(plugin_name)
			android_plugin.initialize()
		else:
			printerr("No plugin found!")
	
	if android_plugin:
		android_plugin.imageStored.connect(func(file_path: String):
			image_stored.emit(file_path)
		)

## Displays the given image in the given texture rectangle.[br]
## [br]
## [param texture_rect]: The texture rectangle control to display the image.[br]
## [param file_path]: The file path of the image, for example user://image.png.
func display_image_in_texture_rect(texture_rect: TextureRect, file_path: String) -> void:
	if FileAccess.file_exists(file_path):
		var image := Image.load_from_file(file_path)
		texture_rect.texture = ImageTexture.create_from_image(image)
	else:
		print("File %s does not exist." % file_path)
