/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin

import android.content.Context
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.plugin.mute.MutePlugin

/**
 * Created by abertschi on 21.04.17.
 */
class PluginHandler(val context: Context, val prefs: PreferencesFactory,
                    val plugins: List<AdPlugin>) {

    private val ACTVIE_PLUGIN_KEY: String = "ACTIVE_PLUGIN"

//    private object Holder {
//        val INSTANCE = PluginHandler()
//    }
//
//    companion object {
//        val instance: PluginHandler by lazy { Holder.INSTANCE }
//    }

    private var activePlugin: AdPlugin? = MutePlugin()

    fun getActivePlugin(context: Context): AdPlugin {
        val activeKey = prefs.getPreferences().getString(ACTVIE_PLUGIN_KEY, null)
        var active: AdPlugin? = activePlugin
        if (activeKey != null) {
            plugins.forEach {
                // TODO: this is hacky
                if (serializeActivePluginId(it).equals(activeKey)) {
                    active = it
                }
            }
        }
        activePlugin = active
        return activePlugin!!
    }

    fun setActivePlugin(plugin: AdPlugin): AdPlugin {
        prefs.getPreferences().edit().putString(ACTVIE_PLUGIN_KEY, serializeActivePluginId(plugin)).commit()
        val oldPlugin = activePlugin
        activePlugin = plugin
        return oldPlugin!!
    }

    fun runPlugin() = activePlugin?.play()

    fun trialRunPlugin() = activePlugin?.playTrial()

    fun requestPluginStop(onStoped: () -> Unit) = activePlugin?.requestStop(onStoped)

    fun stopPlugin() = activePlugin?.forceStop()

    private fun serializeActivePluginId(plugin: AdPlugin): String
            = plugin.javaClass.canonicalName
}