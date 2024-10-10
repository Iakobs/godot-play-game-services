class_name JsonMarshaller extends RefCounted
## A Class for encapsulating JSON parsing
##
## This class exposes methods to parse JSON arrays and dictionaries.

var _json = JSON.new()

## Safely parses a JSON array and returns the Array containing dictionaries.[br]
## [br]
## [param json_array]: The JSON array in String format.[br]
func safe_parse_array(json_array: String) -> Array[Dictionary]:
	var error := _json.parse(json_array)
	if error == OK:
		var data_received = _json.data
		if typeof(data_received) == TYPE_ARRAY:
			var safe_array: Array[Dictionary] = []
			for element: Dictionary in data_received:
				safe_array.append(element)
			return safe_array
		else:
			printerr("Unexpected data received from JSON Array:\n%s" % json_array)
	else:
		printerr("JSON Parse Error: ", _json.get_error_message(), " in ", json_array, " at line ", _json.get_error_line())
	
	return []

## Safely parses a JSON dictionary and returns it, or an empty dictionary if
## something went wrong.[br]
## [br]
## [param json_array]: The JSON dictionary in String format.[br]
func safe_parse_dictionary(json_dictionary: String) -> Dictionary:
	var error := _json.parse(json_dictionary)
	if error == OK:
		var data_received = _json.data
		if typeof(data_received) == TYPE_DICTIONARY:
			return data_received
		elif typeof(data_received) == TYPE_NIL:
			return {}
		else:
			printerr("Unexpected data received from JSON Dictionary:\n%s" % json_dictionary)
	else:
		printerr("JSON Parse Error: ", _json.get_error_message(), " in ", json_dictionary, " at line ", _json.get_error_line())
	
	return {}
