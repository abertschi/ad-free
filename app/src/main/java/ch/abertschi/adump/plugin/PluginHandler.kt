package ch.abertschi.adump.plugin

import android.content.Context
import ch.abertschi.adump.model.PreferencesFactory
import ch.abertschi.adump.plugin.interdimcable.InterdimCablePlugin
import ch.abertschi.adump.plugin.mute.MutePlugin

/**
 * Created by abertschi on 21.04.17.
 */
class PluginHandler private constructor() {

    private val ACTVIE_PLUGIN_KEY: String = "ACTIVE_PLUGIN"

    private object Holder {
        val INSTANCE = PluginHandler()
    }

    companion object {
        val instance: PluginHandler by lazy { Holder.INSTANCE }
    }

    private var initPlugin: AdPlugin = MutePlugin()
    private var mPlugins: List<AdPlugin> = listOf(initPlugin, InterdimCablePlugin()) // ()
    private var mActivePlugin: AdPlugin? = initPlugin

    fun getPlugins(): List<AdPlugin> {
        return mPlugins
    }

    fun getActivePlugin(context: Context): AdPlugin {
        val prefs = PreferencesFactory.providePrefernecesFactory(context)
        val activeKey = prefs.getPreferences().getString(ACTVIE_PLUGIN_KEY, null)
        println("active KEY: " + activeKey)
        var active: AdPlugin? = mActivePlugin
        if (activeKey != null) {
            mPlugins.forEach {
                // TODO: this is hacky
                if (serializeActivePluginId(it).equals(activeKey)) {
                    active = it
                }
            }
        }
        mActivePlugin = active
        return mActivePlugin!!
    }

    private fun serializeActivePluginId(plugin: AdPlugin): String = plugin.javaClass.canonicalName

    fun setActivePlugin(plugin: AdPlugin): AdPlugin {
        val prefs = PreferencesFactory.providePrefernecesFactory()
        prefs.getPreferences().edit().putString(ACTVIE_PLUGIN_KEY, serializeActivePluginId(plugin)).commit()
        val oldPlugin = mActivePlugin
        mActivePlugin = plugin
        return oldPlugin!!
    }

    fun runPlugin(context: PluginContet) {
        mActivePlugin?.play(context)
    }

    fun requestPluginStop(context: PluginContet, onStoped: () -> Unit) {
        mActivePlugin?.requestStop(context, onStoped)
    }

    fun stopPlugin(context: PluginContet) {
        mActivePlugin?.forceStop(context)
    }
}