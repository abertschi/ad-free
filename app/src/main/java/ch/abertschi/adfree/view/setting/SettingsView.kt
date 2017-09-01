/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.view.setting

import android.content.Context
import android.view.View
import ch.abertschi.adfree.plugin.PluginActivityAction

/**
 * Created by abertschi on 21.04.17.
 */

interface SettingsView : PluginActivityAction {

    fun setActivePlugin(index: Int)
    fun getContext(): Context
    fun showSuggestNewPlugin()
    fun setPluginView(view: View)
    fun clearPluginView()
    fun showTryOutMessage()
    fun signalizeTryOut()
}
