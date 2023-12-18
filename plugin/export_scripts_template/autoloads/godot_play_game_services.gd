extends Node
## Main Autoload of the plugin, which contains a reference to the android plugin itself.
##
## This Autoload contains the entrypoint to the android code, but you don't need
## to use it directly. Some autoloads exposing the plugin functionality, as a wrapper
## for GDScript code, are also loaded with the plugin.[br]
## [br]
## This Autoload also calls the [code]initialize()[/code] method of the plugin,
## checking if the user is authenticated.

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
