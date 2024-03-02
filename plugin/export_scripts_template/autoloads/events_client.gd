extends Node

## Signal emitted after calling the [method load_events] method.[br]
## [br]
## [param events]: The list of events.
signal events_loaded(events)

## Signal emitted after calling the [method load_events_by_ids] method.[br]
## [br]
## [param events]: The list of events.
signal events_loaded_by_ids(events)

func _ready() -> void:
	_connect_signals()

func _connect_signals() -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.eventsLoaded.connect(func(json_data: String):
			events_loaded.emit(_parse_events(json_data))
		)
		GodotPlayGameServices.android_plugin.eventsLoadedByIds.connect(func(json_data: String):
			events_loaded.emit(_parse_events(json_data))
		)

## Increments an event specified by eventId by the given number of steps.[br]
## [br]
## This is the fire-and-forget API. Event increments are cached locally and flushed
## to the server in batches.[br]
## [br]
## [param event_id]: The event ID to increment.[br]
## [param increment_amount]: The amount increment by. Must be greater than or equal to 0.
func increment_event(event_id: String, increment_amount: int) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.incrementEvent(event_id, increment_amount)

## Loads a list of events for the currently signed-in player.[br]
## [br]
## [param force_reload]: If true, this call will clear any locally cached 
## data and attempt to fetch the latest data from the server. Send it set to [code]true[/code]
## the first time, and [code]false[/code] in subsequent calls, or when you want
## to clear the cache.
func load_events(force_reload: bool) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.loadEvents(force_reload)

## Loads a specific list of events for the currently signed-in player.[br]
## [br]
## [param force_reload]: If true, this call will clear any locally cached 
## data and attempt to fetch the latest data from the server. Send it set to [code]true[/code]
## the first time, and [code]false[/code] in subsequent calls, or when you want
## to clear the cache.[br]
## [param event_ids]: The IDs of the events to load.
func load_events_by_ids(force_reload: bool, event_ids: Array[String]) -> void:
	if GodotPlayGameServices.android_plugin:
		GodotPlayGameServices.android_plugin.loadEventsByIds(force_reload, event_ids)

func _parse_events(json_data: String) -> Array[PlayGamesEvent]:
	var safe_array := GodotPlayGameServices.json_marshaller.safe_parse_array(json_data)
	var events: Array[PlayGamesEvent] = []
	for dictionary: Dictionary in safe_array:
		events.append(PlayGamesEvent.new(dictionary))
	return events

## A class representing an Event from Google Play Games Services.
class PlayGamesEvent:
	var description: String ## The description for this event.
	var event_id: String ## The ID of this event.
	var formatted_value: String ## The sum of increments have been made to this event (formatted for the user's locale).
	var icon_image_uri: String ## The URI to the event's image icon.
	var name: String ## The name of this event.
	var player: PlayersClient.Player ## The player information associated with this event.
	var value: int ## The number of increments this user has made to this event.
	var is_visible: bool ## Whether the event should be displayed to the user in any event related UIs.
	
	## Constructor that creates a PlayGamesEvent from a [Dictionary] containing the properties.
	func _init(dictionary: Dictionary) -> void:
		print(dictionary)
		if dictionary.has("description"): description = dictionary.description
		if dictionary.has("eventId"): event_id = dictionary.eventId
		if dictionary.has("formattedValue"): formatted_value = dictionary.formattedValue
		if dictionary.has("iconImageUri"): icon_image_uri = dictionary.iconImageUri
		if dictionary.has("name"): name = dictionary.name
		if dictionary.has("player"): player = PlayersClient.Player.new(dictionary.player)
		if dictionary.has("value"): value = dictionary.value
		if dictionary.has("isVisible"): is_visible = dictionary.isVisible
	
	func _to_string() -> String:
		var result := PackedStringArray()
		
		result.append("description: %s" % description)
		result.append("event_id: %s" % event_id)
		result.append("formatted_value: %s" % formatted_value)
		result.append("icon_image_uri: %s" % icon_image_uri)
		result.append("name: %s" % name)
		result.append("player: {%s}" % str(player))
		result.append("value: %s" % value)
		result.append("is_visible: %s" % is_visible)
		
		return ", ".join(result)
