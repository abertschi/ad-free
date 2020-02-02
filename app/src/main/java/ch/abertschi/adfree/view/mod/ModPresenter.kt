package ch.abertschi.adfree.view.mod

import android.content.Context
import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.model.PreferencesFactory
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class ModPresenter(val view: ModActivity, val prefs: PreferencesFactory): AnkoLogger {

    private lateinit var context: Context

    fun onCreate(context: Context) {
        view.setEnableToggle(prefs.isBlockingEnabled())
        view.setNotificationEnabled(prefs.isAlwaysOnNotificationEnabled())
        view.setDelayValue(prefs.getDelaySeconds())
        this.context = context
    }

    fun onToggleAlwaysOnChanged() {
        val newVal = !prefs.isAlwaysOnNotificationEnabled()
        prefs.setAlwaysOnNotification(newVal)
        view.setNotificationEnabled(newVal)
        (view.applicationContext as AdFreeApplication).restartNotificationListener()
    }

    fun onDelayUnmute() {
        view.showDelayUnmute()
    }
    fun onDelayChanged(delay: Int) {
        prefs.setDelaySeconds(delay)
        view.setDelayValue(delay)
    }


    fun onEnableToggleChanged() {
        val newVal = !prefs.isBlockingEnabled()
        prefs.setBlockingEnabled(newVal)
        view.setEnableToggle(newVal)
        if (newVal) {
            view.showPowerEnabled()
        }
    }

    fun configureDetectors() {

    }

}