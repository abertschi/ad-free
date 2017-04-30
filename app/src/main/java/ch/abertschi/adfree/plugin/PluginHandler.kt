/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin

import android.content.Context
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.plugin.interdimcable.InterdimCablePlugin
import ch.abertschi.adfree.plugin.mute.MutePlugin

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
    private var plugins: List<AdPlugin> = listOf(initPlugin, InterdimCablePlugin()) // ()
    private var activePlugin: AdPlugin? = initPlugin

    fun getActivePlugin(context: Context): AdPlugin {
        val prefs = PreferencesFactory.providePrefernecesFactory(context)
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
        val prefs = PreferencesFactory.providePrefernecesFactory()
        prefs.getPreferences().edit().putString(ACTVIE_PLUGIN_KEY, serializeActivePluginId(plugin)).commit()
        val oldPlugin = activePlugin
        activePlugin = plugin
        return oldPlugin!!
    }

    fun getPlugins(): List<AdPlugin> = plugins

    fun runPlugin(context: PluginContet) = activePlugin?.play(context)

    fun trialRunPlugin(context: PluginContet) = activePlugin?.playTrial(context)

    fun requestPluginStop(context: PluginContet, onStoped: () -> Unit) = activePlugin?.requestStop(context, onStoped)

    fun stopPlugin(context: PluginContet) = activePlugin?.forceStop(context)

    private fun serializeActivePluginId(plugin: AdPlugin): String = plugin.javaClass.canonicalName
}