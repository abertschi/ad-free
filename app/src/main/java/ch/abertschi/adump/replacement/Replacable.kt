package ch.abertschi.adump.replacement

import android.view.View

/**
 * Created by abertschi on 21.04.17.
 */
interface Replacable {

    fun play(context: ReplacementContext): Unit
    fun requestStop(contet: ReplacementContext, onStoped: () -> Unit)
    fun forceStop(context: ReplacementContext)

    fun title(): String
    fun hasSettingsView(): Boolean
    fun settingsView(): View
}

class ReplacementContext {

}