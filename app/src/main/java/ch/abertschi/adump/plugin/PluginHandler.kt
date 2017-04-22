package ch.abertschi.adump.plugin

import ch.abertschi.adump.plugin.interdimcable.InterdimCablePlugin
import ch.abertschi.adump.plugin.mute.MutePlugin

/**
 * Created by abertschi on 21.04.17.
 */
class PluginHandler private constructor() {

    private object Holder {
        val INSTANCE = PluginHandler()
    }

    companion object {
        val instance: PluginHandler by lazy { Holder.INSTANCE }
    }

    private var mPlugins: List<AdPlugin> = listOf(MutePlugin(), InterdimCablePlugin()) // ()
    private var mActivePlugin: AdPlugin? = mPlugins[1]

    fun getPlugins(): List<AdPlugin> {
        return mPlugins
    }

    fun getActivePlugin(): AdPlugin {
        return mActivePlugin!!
    }

    fun setActivePlugin(plugin: AdPlugin): AdPlugin {
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