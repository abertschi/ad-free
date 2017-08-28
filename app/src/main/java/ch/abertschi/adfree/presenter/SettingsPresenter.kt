/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.presenter

import android.content.Context
import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.plugin.AdPlugin
import ch.abertschi.adfree.plugin.PluginHandler
import ch.abertschi.adfree.view.setting.SettingsView
import org.jetbrains.anko.AnkoLogger


/**
 * Created by abertschi on 21.04.17.
 */

class SettingsPresenter(val settingView: SettingsView, val context: Context) : AnkoLogger {

    private val pluginHandler: PluginHandler =
            (context.applicationContext as AdFreeApplication).pluginHandler

    private val plugins: List<AdPlugin> = pluginHandler.plugins
    private var activePlugin: AdPlugin? = null
    private var activePluginIndex: Int = 0

    // ideas for plugins:
    // val list: List<String> = listOf("mute audio", "local music", "soundcloud",
    // "interdimensional cable", "joke time", "meh", "suggest something ...")

    init {
        activePlugin = pluginHandler.getActivePlugin(settingView.getContext())
        var index: Int = 0
        plugins.forEach {
            if (it == activePlugin) {
                activePluginIndex = index
            }
            index++
        }
        if (activePluginIndex >= plugins.size) {
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


    fun onPluginSelected(index: Int) {
        if (index >= plugins.size) {
            settingView.showSuggestNewPlugin()
            setActivePlugin(activePluginIndex)
        } else {
            setActivePlugin(index)
        }
        setPluginView()
    }

    fun tryPlugin() {
//        AudioController.instance.unmuteMusicAndStopActivePlugin(settingView.getContext())
//        AudioController.instance.muteMusicAndRunActivePlugin(settingView.getContext())
    }

    private fun setActivePlugin(index: Int) {
        val plugin = plugins[index]
        if (activePlugin != plugin) {
            activePlugin?.onPluginDeactivated()
        }
        activePlugin = plugin
        activePlugin?.onPluginActivated()
        activePluginIndex = index
        settingView.setActivePlugin(activePluginIndex)
        pluginHandler.setActivePlugin(activePlugin!!)
    }

    fun getStringEntriesOfModel(): Array<String> {
        var result: ArrayList<String> = ArrayList()
        plugins.forEach { result.add(it.title()) }
        result.add("suggest something ...")
        return result.toTypedArray()
    }
}
