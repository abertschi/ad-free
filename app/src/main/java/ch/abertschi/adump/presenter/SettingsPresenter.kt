package ch.abertschi.adump.presenter

import ch.abertschi.adump.AudioController
import ch.abertschi.adump.plugin.AdPlugin
import ch.abertschi.adump.plugin.PluginContet
import ch.abertschi.adump.plugin.PluginHandler
import ch.abertschi.adump.view.setting.SettingsView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


/**
 * Created by abertschi on 21.04.17.
 */

class SettingsPresenter(val settingView: SettingsView) : AnkoLogger {
    //        val list: List<String> = listOf("mute audio", "local music", "soundcloud", "interdimensional cable", "joke time", "meh", "suggest something ...")

    private val pluginHandler: PluginHandler = PluginHandler.instance
    private val mPlugins: List<AdPlugin> = pluginHandler.getPlugins()
    private var mActivePlugin: AdPlugin? = null
    private var mActivePluginIndex: Int = 0

    init {
        mActivePlugin = pluginHandler.getActivePlugin(settingView.getContext())
        var index: Int = 0
        mPlugins.forEach {
            info { it.toString() }
            if (it == mActivePlugin) {
                mActivePluginIndex = index
            }
            index++
        }
        if (mActivePluginIndex >= mPlugins.size) {
            mActivePluginIndex = 0
        }
    }

    fun onCreate() {
        setActivePlugin(mActivePluginIndex)
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

    fun tryPlugin() {
        AudioController.instance.muteMusicAndRunActivePlugin(settingView.getContext())
    }

    private fun setActivePlugin(index: Int) {
        val context = PluginContet(settingView.getContext())
        val plugin = mPlugins[index]
        if (mActivePlugin != plugin) {
            mActivePlugin?.onPluginDeactivated(context)
        }
        mActivePlugin = plugin
        mActivePlugin?.onPluginActivated(PluginContet(settingView.getContext()))
        mActivePluginIndex = index
        settingView.setActivePlugin(mActivePluginIndex)
        pluginHandler.setActivePlugin(mActivePlugin!!)
    }

    fun getStringEntriesOfModel(): Array<String> {
        var result: ArrayList<String> = ArrayList<String>()
        getPlugins().forEach { result.add(it.title()) }
//        result.add("suggest something ...")
        result.add("more soon to come ...")
        return result.toTypedArray()
    }
}
