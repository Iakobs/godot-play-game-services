// TODO: Update to match your plugin's package name.
package org.godotengine.plugin.android.template

import android.util.Log
import android.widget.Toast
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.UsedByGodot

class GodotAndroidPlugin(godot: Godot): GodotPlugin(godot) {

    override fun getPluginName() = BuildConfig.GODOT_PLUGIN_NAME

    /**
     * Example showing how to declare a method that's used by Godot.
     *
     * Shows a 'Hello World' toast.
     */
    @UsedByGodot
    private fun helloWorld() {
        runOnUiThread {
            Toast.makeText(activity, "Hello World", Toast.LENGTH_LONG).show()
            Log.v(pluginName, "Hello World")
        }
    }
}
