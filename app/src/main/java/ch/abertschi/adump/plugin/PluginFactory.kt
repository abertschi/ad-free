package ch.abertschi.adump.plugin

import ch.abertschi.adump.plugin.mute.MutePlugin

/**
 * Created by abertschi on 21.04.17.
 */
class PluginFactory() {

    fun getPlugins(): List<AdPlugin> {
        return listOf(MutePlugin()) // InterdimCablePlugin()
    }

//    fun getActivePlugin(): AdPlugin {
//
//    }
//
//    fun setActivePlugin(plugin: AdPlugin): AdPlugin {
//
//    }
}