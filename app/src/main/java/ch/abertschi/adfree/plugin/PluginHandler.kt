/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin

import ch.abertschi.adfree.ad.AdObservable
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.plugin.mute.MutePlugin
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by abertschi on 21.04.17.
 */
class PluginHandler(val prefs: PreferencesFactory,
                    val plugins: List<AdPlugin>,
                    val adDetector: AdObservable) : AnkoLogger {

    private var activePlugin: AdPlugin = loadActivePlugin()

    fun getActivePlugin(): AdPlugin {
        return activePlugin!!
    }

    private fun loadActivePlugin(): AdPlugin {
        val key: String? = prefs.getActivePlugin()
        var active = plugins.firstOrNull { serializeActivePluginId(it).equals(key) }
        return if (active != null) active
        else MutePlugin() // default plugin
    }

    fun setActivePlugin(plugin: AdPlugin): AdPlugin {
        prefs.setActivePlugin(serializeActivePluginId(plugin))
        val oldPlugin = activePlugin
        activePlugin = plugin
        return oldPlugin!!
    }

    fun runPlugin() = activePlugin?.play()

    fun trialRunPlugin() {
        activePlugin?.playTrial()
    }

    fun requestPluginStop(onStoped: () -> Unit) = activePlugin?.requestStop(onStoped)

    fun stopPlugin(onStoped: () -> Unit) {
        info { "Stopping plugin " + activePlugin?.javaClass.canonicalName }
        activePlugin?.stop(onStoped)
    }

    fun forceStopPlugin(onStoped: () -> Unit) = activePlugin?.forceStop(onStoped)

    private fun serializeActivePluginId(plugin: AdPlugin): String
            = plugin.javaClass.canonicalName
}