package ch.abertschi.adump.plugin.mute

import ch.abertschi.adump.plugin.AdPlugin
import ch.abertschi.adump.plugin.PluginContet

/**
 * Created by abertschi on 21.04.17.
 */
class MutePlugin : AdPlugin {

    override fun title() = "mute audio"
    override fun hasSettingsView() = false

    override fun play(context: PluginContet) {}

    override fun requestStop(contet: PluginContet, onStoped: () -> Unit) {}

    override fun forceStop(context: PluginContet) {}

    override fun onPluginActivated(context: PluginContet) {}

    override fun onPluginDeactivated(context: PluginContet) {}

}