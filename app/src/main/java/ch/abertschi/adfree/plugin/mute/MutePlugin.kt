/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.mute

import ch.abertschi.adfree.plugin.AdPlugin
import ch.abertschi.adfree.plugin.PluginContet

/**
 * Created by abertschi on 21.04.17.
 */
class MutePlugin : AdPlugin {
    override fun playTrial(context: PluginContet) {}

    override fun title() = "mute audio"

    override fun hasSettingsView() = false

    override fun play(context: PluginContet) {}

    override fun requestStop(contet: PluginContet, onStoped: () -> Unit) {}

    override fun forceStop(context: PluginContet) {}

    override fun onPluginActivated(context: PluginContet) {}

    override fun onPluginDeactivated(context: PluginContet) {}

}