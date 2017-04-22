package ch.abertschi.adump.presenter

import ch.abertschi.adump.plugin.AdPlugin
import ch.abertschi.adump.plugin.PluginFactory
import ch.abertschi.adump.view.setting.SettingsView
import org.jetbrains.anko.AnkoLogger


/**
 * Created by abertschi on 21.04.17.
 */

class SettingsPresenter(val settingView: SettingsView) : AnkoLogger {
    //        val list: List<String> = listOf("mute audio", "local music", "soundcloud", "interdimensional cable", "joke time", "meh", "suggest something ...")

    val pluginFactory: PluginFactory = PluginFactory()

    val mPlugins: List<AdPlugin> = pluginFactory.getPlugins()
    var mActivePlugin: AdPlugin? = null

    fun onCreate() {
    }

    fun onResume() {
    }

    fun getPlugins(): List<AdPlugin> {
        return pluginFactory.getPlugins()
    }

    fun onPluginSelected(index: Int) {
        if (index >= mPlugins.size) settingView.showSuggestNewPlugin()
        else {
            setActivePlugin(index)
        }
    }

    private fun setActivePlugin(index: Int) {
        settingView.setActivePlugin(index)
    }

    fun getStringEntriesOfModel(): Array<String> {
        var result: ArrayList<String> = ArrayList<String>()
        getPlugins().forEach { result.add(it.title()) }
//        result.add("suggest something ...")
        result.add("more soon to come ...")
        return result.toTypedArray()
    }
}
