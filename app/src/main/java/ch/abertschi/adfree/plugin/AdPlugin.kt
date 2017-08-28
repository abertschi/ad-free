/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin

import android.content.Context
import android.view.View

/**
 * Created by abertschi on 21.04.17.
 */
interface AdPlugin {

    fun title(): String
    fun hasSettingsView(): Boolean = false
    fun settingsView(context: Context): View? = null

    fun play(): Unit
    fun playTrial(): Unit
    fun requestStop(onStoped: () -> Unit)
    fun forceStop()

    fun onPluginActivated()
    fun onPluginDeactivated()
}
