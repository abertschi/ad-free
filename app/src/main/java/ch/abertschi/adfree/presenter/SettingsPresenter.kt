/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.presenter

import android.content.Context
import ch.abertschi.adfree.ad.AdObservable
import ch.abertschi.adfree.plugin.AdPlugin
import ch.abertschi.adfree.plugin.PluginHandler
import ch.abertschi.adfree.view.setting.SettingsView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.collections.forEachWithIndex


/**
 * Created by abertschi on 21.04.17.
 */

class SettingsPresenter(val settingView: SettingsView,
                        val context: Context,
                        val pluginHandler: PluginHandler,
                        val adObserver: AdObservable) : AnkoLogger {


    private val plugins: List<AdPlugin> = pluginHandler.plugins
    private var activePlugin: AdPlugin? = null
    private var activePluginIndex: Int = 0

    init {
        activePlugin = pluginHandler.getActivePlugin()
        activePluginIndex = 0
        plugins.forEachWithIndex { i, adPlugin ->
            if (adPlugin == activePlugin) activePluginIndex = i
        }
    }

    fun onCreate() {
        setActivePlugin(activePluginIndex)
        setPluginView()
    }

    private fun setPluginView() {
        settingView.clearPluginView()
        if (activePlugin?.hasSettingsView() == true) {
            settingView.setPluginView(activePlugin?.
                    settingsView(settingView.getContext(), settingView)!!)
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
        adObserver.requestShowcase()
        settingView.showTryOutMessage()
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
