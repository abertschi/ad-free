package ch.abertschi.adump.replacement

/**
 * Created by abertschi on 21.04.17.
 */
class MuteReplacer: AdReplacable {

    override fun title() = "MUTE AUDIO"
    override fun hasSettingsView() = false

    override fun play(context: ReplacementContext) {}

    override fun requestStop(contet: ReplacementContext, onStoped: () -> Unit) {}

    override fun forceStop(context: ReplacementContext) {}

    override fun onReplacerActivated(context: ReplacementContext) {}

    override fun onReplacerDeactivated(context: ReplacementContext) {}

}