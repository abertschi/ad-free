package ch.abertschi.adump.plugin

import android.content.Context
import android.view.View

/**
 * Created by abertschi on 21.04.17.
 */
interface AdPlugin {

    fun title(): String
    fun hasSettingsView(): Boolean = false
    fun settingsView(): View? = null

    fun play(context: PluginContet): Unit
    fun requestStop(contet: PluginContet, onStoped: () -> Unit)
    fun forceStop(context: PluginContet)

    fun onPluginActivated(context: PluginContet)
    fun onPluginDeactivated(context: PluginContet)
}

class PluginContet(val applicationContext: Context)