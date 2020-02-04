package ch.abertschi.adfree.view.mod

import android.app.Activity
import android.content.Context
import android.content.Intent
import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.ListenerStatus
import ch.abertschi.adfree.NotificationStatusManager
import ch.abertschi.adfree.NotificationStatusObserver
import ch.abertschi.adfree.model.PreferencesFactory
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.runOnUiThread

class ModPresenter(val view: ModActivity, val prefs: PreferencesFactory): AnkoLogger,
        NotificationStatusObserver {

    override fun onStatusChanged(status: ListenerStatus) {
        context.runOnUiThread {
            info { status }
            info { "from presenter" }
            if (status == ListenerStatus.CONNECTED) {
                view.showNotifiationListenerConnected()
            } else{
                view.showNotificationListenerDisconnected()
            }
        }
    }

    private lateinit var context: Context

    private lateinit var notificationStatusManager: NotificationStatusManager

    fun onCreate(context: Context) {
        info { "new presenter" }
        view.setEnableToggle(prefs.isBlockingEnabled())
        view.setNotificationEnabled(prefs.isAlwaysOnNotificationEnabled())
        view.setDelayValue(prefs.getDelaySeconds())
        this.context = context
        notificationStatusManager = (context.applicationContext as AdFreeApplication).notificationStatus
        notificationStatusManager.addObserver(this)
        notificationStatusManager.restartNotificationListener() // always restart on launch
        onStatusChanged(notificationStatusManager.getStatus())
    }

    fun onToggleAlwaysOnChanged() {
        val newVal = !prefs.isAlwaysOnNotificationEnabled()
        prefs.setAlwaysOnNotification(newVal)
        view.setNotificationEnabled(newVal)
        notificationStatusManager.restartNotificationListener()
        if (!newVal) {
            (view.applicationContext as AdFreeApplication).notificationChannel.hideAlwaysOnNotification()
        }
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

    fun onLaunchActiveDetectorsView() {
        val myIntent = Intent(this.context, ActiveDetectorsActivity::class.java)
        this.context.startActivity(myIntent)
    }

    fun onLaunchNotificationListenerSystemSettings() {
        context.startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }
}