/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump.plugin

import android.content.Context
import android.view.View

/**
 * Created by abertschi on 21.04.17.
 */
interface AdPlugin {

    fun title(): String
    fun hasSettingsView(): Boolean = false
    fun settingsView(context: Context): View? = null

    fun play(context: PluginContet): Unit
    fun playTrial(context: PluginContet): Unit
    fun requestStop(contet: PluginContet, onStoped: () -> Unit)
    fun forceStop(context: PluginContet)

    fun onPluginActivated(context: PluginContet)
    fun onPluginDeactivated(context: PluginContet)
}

data class PluginContet(val applicationContext: Context)