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
    
    private var activePlugin: AdPlugin? = MutePlugin()

    fun getActivePlugin(): AdPlugin {
        val key = prefs.getActivePlugin()
        val active = plugins.filter { serializeActivePluginId(it).equals(key) }
                .firstOrNull()
        active.let {
            activePlugin = it
        }
        return activePlugin!!
    }

    fun setActivePlugin(plugin: AdPlugin): AdPlugin {
        prefs.setActivePlugin(serializeActivePluginId(plugin))
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