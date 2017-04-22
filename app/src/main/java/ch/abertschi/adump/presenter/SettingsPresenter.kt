package ch.abertschi.adump.presenter

import ch.abertschi.adump.plugin.AdPlugin
import ch.abertschi.adump.plugin.PluginContet
import ch.abertschi.adump.plugin.PluginHandler
import ch.abertschi.adump.view.setting.SettingsView
import org.jetbrains.anko.AnkoLogger


/**
 * Created by abertschi on 21.04.17.
 */

class SettingsPresenter(val settingView: SettingsView) : AnkoLogger {
    //        val list: List<String> = listOf("mute audio", "local music", "soundcloud", "interdimensional cable", "joke time", "meh", "suggest something ...")

    val pluginHandler: PluginHandler = PluginHandler.instance

    val mPlugins: List<AdPlugin> = pluginHandler.getPlugins()
    var mActivePlugin: AdPlugin? = null
    var mActivePluginIndex: Int = 0

    fun onCreate() {
        setActivePlugin(mActivePluginIndex)
        println("created")
    }

    fun onResume() {
        setActivePlugin(mActivePluginIndex)
    }

    fun getPlugins(): List<AdPlugin> {
        return pluginHandler.getPlugins()
    }

    fun onPluginSelected(index: Int) {
        if (index >= mPlugins.size) {
            settingView.showSuggestNewPlugin()
            setActivePlugin(mActivePluginIndex)
        } else {
            setActivePlugin(index)
        }
    }

    private fun setActivePlugin(index: Int) {
        val context = PluginContet(settingView.getContext())
        val plugin = mPlugins[index]
        if (mActivePlugin != plugin) {
            mActivePlugin?.onPluginDeactivated(context)
        }
        plugin.onPluginActivated(PluginContet(settingView.getContext()))
        mActivePlugin = plugin
        settingView.setActivePlugin(index)
        mActivePluginIndex = index
    }

    fun getStringEntriesOfModel(): Array<String> {
        var result: ArrayList<String> = ArrayList<String>()
        getPlugins().forEach { result.add(it.title()) }
//        result.add("suggest something ...")
        result.add("more soon to come ...")
        return result.toTypedArray()
    }
}
