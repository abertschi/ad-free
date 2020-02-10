package ch.abertschi.adfree.view.mod

import android.content.Context
import android.content.Intent
import ch.abertschi.adfree.model.AdDetectableFactory
import ch.abertschi.adfree.model.PreferencesFactory
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.runOnUiThread
import android.os.AsyncTask
import android.app.AlarmManager
import android.app.PendingIntent
import ch.abertschi.adfree.*


class ModPresenter(val view: ModActivity, val prefs: PreferencesFactory) : AnkoLogger,
        NotificationStatusObserver {

    private lateinit var context: Context
    private lateinit var notificationStatusManager: NotificationStatusManager
    private lateinit var detectorFactory: AdDetectableFactory
    private lateinit var googleCastManager: GoogleCastManager


    override fun onStatusChanged(status: ListenerStatus) {
        context.runOnUiThread {
            info { "notification listener changed status: $status" }
            if (status == ListenerStatus.CONNECTED) {
                view.showNotifiationListenerConnected()
            } else {
                view.showNotificationListenerDisconnected()
            }
        }
    }

    fun onCreate(context: Context) {
        info { "new presenter" }
        this.context = context
        val app = context.applicationContext as AdFreeApplication
        detectorFactory = app.adDetectors
        notificationStatusManager = app.notificationStatus
        googleCastManager = app.googleCast

        view.setEnableToggle(detectorFactory.isAdfreeEnabled())
        view.setNotificationEnabled(prefs.isAlwaysOnNotificationEnabled())
        view.setDelayValue(prefs.getDelaySeconds())
        view.setGoogleCastToggle(googleCastManager.isEnabled())

        notificationStatusManager.addObserver(this)
        notificationStatusManager.restartNotificationListener() // always restart on launch

        showDetectorCount()
        showDeveloperMode()

        AsyncTask.execute {
            onStatusChanged(notificationStatusManager.getStatus())
        }
    }

    private fun showDeveloperMode() {
        if (prefs.isDeveloperModeEnabled()) {
            view.showDeveloperModeFeatures()
        } else {
            view.hideDeveloperModeFeatures()
        }
    }

    private fun showDetectorCount() {
        val enabled = detectorFactory.getEnabledDetectors().size
        val visible = detectorFactory.getVisibleDetectors().size
        val total = detectorFactory.getAllDetectors().size
        view.showDetectorCount(enabled,
                if (enabled <= visible) visible else total)
    }

    fun onToggleAlwaysOnChanged() {
        val newVal = !prefs.isAlwaysOnNotificationEnabled()
        prefs.setAlwaysOnNotification(newVal)
        view.setNotificationEnabled(newVal)
        notificationStatusManager.restartNotificationListener()
        if (!newVal) {
            (view.applicationContext as AdFreeApplication)
                    .notificationChannel.hideAlwaysOnNotification()
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
        detectorFactory.setAdfreeEnabled(newVal)
        view.setEnableToggle(newVal)
        if (newVal) {
            view.showPowerEnabled()
        }
    }

    fun onLaunchActiveDetectorsView() {
        val myIntent = Intent(this.context, ActiveDetectorActivity::class.java)
        this.context.startActivity(myIntent)
    }

    fun onLaunchNotificationListenerSystemSettings() {
        context.startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }

    fun onResume() {
        showDetectorCount()
        showDeveloperMode()
    }

    fun onGoogleCastToggle() {
        val toggle = !googleCastManager.isEnabled()
        googleCastManager.setEnabled(toggle)
        view.setGoogleCastToggle(toggle)
    }
}