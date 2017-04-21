package ch.abertschi.adump.replacement

import android.view.View

/**
 * Created by abertschi on 21.04.17.
 */
interface AdReplacable {

    fun play(context: ReplacementContext): Unit
    fun requestStop(contet: ReplacementContext, onStoped: () -> Unit)
    fun forceStop(context: ReplacementContext)

    fun onReplacerActivated(context: ReplacementContext)
    fun onReplacerDeactivated(context: ReplacementContext)

    fun title(): String
    fun hasSettingsView(): Boolean
    fun settingsView(): View? = null
}

class ReplacementContext {

}