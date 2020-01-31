package ch.abertschi.adfree.view.mod

import android.content.Context
import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.model.PreferencesFactory
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class ModPresenter(val view: ModActivity, val prefs: PreferencesFactory): AnkoLogger {

    private lateinit var context: Context

    fun onCreate(context: Context) {
        view.updateEnableToggle()
        view.updateAlwaysOnToggle()
        view.updateDelaySeekbar(prefs.getDelaySeconds())
        this.context = context
    }

    fun isEnabled(): Boolean = prefs.isBlockingEnabled()

    fun isAlwaysOnNotification() = prefs.isAlwaysOnNotificationEnabled()

    fun toggleEnabled() {
        prefs.setBlockingEnabled(!prefs.isBlockingEnabled())
        view.updateEnableToggle()
    }

    fun toggleAlwaysOn() {
        prefs.setAlwaysOnNotification(!prefs.isAlwaysOnNotificationEnabled())
        view.updateAlwaysOnToggle()
    }

    fun delayUmute() {
        view.showDelayUnmute()
    }
    fun delayChanged(delay: Int) {
        prefs.setDelaySeconds(delay)
        view.updateDelaySeekbar(delay)
        info { delay }
    }

    fun configureDetectors() {

    }
    //


    fun onDelayChanged(delay: Int) {

    }

    fun onEnableToggleChanged(enable: Boolean) {

    }

    fun onShowNotificationChanged(enable: Boolean) {

    }

}