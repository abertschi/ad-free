package ch.abertschi.adump

import android.app.NotificationManager
import android.content.Context
import android.os.Handler
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.support.v4.app.NotificationCompat
import android.media.AudioManager
import android.os.Looper
import ch.abertschi.adump.detector.AdDetectable
import ch.abertschi.adump.detector.AdPayload
import ch.abertschi.adump.detector.DetectorService
import ch.abertschi.adump.model.PreferencesFactory


/**
 * Created by abertschi on 11.12.16.
 */
class MyNotificationListener : NotificationListenerService() {

    var detectorService: DetectorService? = null
    var preferences: PreferencesFactory? = null

    init {
        println("Spotify Ad listener online")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        println("notification appearing") // TODO: Problem with prefs, stored power value not available
//        if (preferences == null) {
//            preferences = PreferencesFactory.providePrefernecesFactory(this)
//            println(preferences?.isBlockingEnabled())
//        }
//        if (!preferences!!.isBlockingEnabled()) return

        if (detectorService == null) {
            detectorService = DetectorService(this)
        }
        val p: AdPayload = AdPayload(sbn)
        detectorService!!.muteAds(p)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        println("Notification removed")
        super.onNotificationRemoved(sbn)
    }
}