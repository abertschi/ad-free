package ch.abertschi.adump.plugin

/**
 * Created by abertschi on 21.04.17.
 */
class PluginFactory() {

    fun getPlugins(): List<AdPlugin> {
        val mute: MutePlugin = MutePlugin()


        return listOf(mute)
    }

//    fun getActivePlugin(): AdPlugin {
//
//    }
//
//    fun setActivePlugin(plugin: AdPlugin): AdPlugin {
//
//    }
}