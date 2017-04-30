/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump.presenter

import ch.abertschi.adump.AudioController
import ch.abertschi.adump.plugin.AdPlugin
import ch.abertschi.adump.plugin.PluginContet
import ch.abertschi.adump.plugin.PluginHandler
import ch.abertschi.adump.view.setting.SettingsView
import org.jetbrains.anko.AnkoLogger


/**
 * Created by abertschi on 21.04.17.
 */

class SettingsPresenter(val settingView: SettingsView) : AnkoLogger {

    private val pluginHandler: PluginHandler = PluginHandler.instance
    private val mPlugins: List<AdPlugin> = pluginHandler.getPlugins()
    private var activePlugin: AdPlugin? = null
    private var activePluginIndex: Int = 0

    // ideas for plugins:
    //        val list: List<String> = listOf("mute audio", "local music", "soundcloud", "interdimensional cable", "joke time", "meh", "suggest something ...")

    init {
        activePlugin = pluginHandler.getActivePlugin(settingView.getContext())
        var index: Int = 0
        mPlugins.forEach {
            if (it == activePlugin) {
                activePluginIndex = index
            }
            index++
        }
        if (activePluginIndex >= mPlugins.size) {
            activePluginIndex = 0
        }
    }

    fun onCreate() {
        setActivePlugin(activePluginIndex)
        setPluginView()
    }

    private fun setPluginView() {
        settingView.clearPluginView()
        if (activePlugin?.hasSettingsView() ?: false) {
            settingView.setPluginView(activePlugin?.settingsView(settingView.getContext())!!)
        }
    }

    fun onResume() {
        setActivePlugin(activePluginIndex)
        setPluginView()
    }

    fun getPlugins(): List<AdPlugin> {
        return pluginHandler.getPlugins()
    }

    fun onPluginSelected(index: Int) {
        if (index >= mPlugins.size) {
            settingView.showSuggestNewPlugin()
            setActivePlugin(activePluginIndex)
        } else {
            setActivePlugin(index)
        }
        setPluginView()
    }

    fun tryPlugin() {
        AudioController.instance.unmuteMusicAndStopActivePlugin(settingView.getContext())
        AudioController.instance.muteMusicAndRunActivePlugin(settingView.getContext())
    }

    private fun setActivePlugin(index: Int) {
        val context = PluginContet(settingView.getContext())
        val plugin = mPlugins[index]
        if (activePlugin != plugin) {
            activePlugin?.onPluginDeactivated(context)
        }
        activePlugin = plugin
        activePlugin?.onPluginActivated(PluginContet(settingView.getContext()))
        activePluginIndex = index
        settingView.setActivePlugin(activePluginIndex)
        pluginHandler.setActivePlugin(activePlugin!!)
    }

    fun getStringEntriesOfModel(): Array<String> {
        var result: ArrayList<String> = ArrayList()
        getPlugins().forEach { result.add(it.title()) }
        result.add("suggest something ...")
        return result.toTypedArray()
    }
}
