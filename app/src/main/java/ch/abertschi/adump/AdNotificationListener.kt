package ch.abertschi.adump

import android.app.NotificationManager
import android.content.Context
import android.os.Handler
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.support.v4.app.NotificationCompat
import android.media.AudioManager
import ch.abertschi.adump.detector.AdDetectable
import ch.abertschi.adump.detector.AdPayload
import ch.abertschi.adump.detector.SpotifyTitleDetector
import ch.abertschi.adump.model.PreferencesFactory


/**
 * Created by abertschi on 11.12.16.
 */
class AdNotificationListener : NotificationListenerService() {

    private val handler: Handler = Handler()
    private val notificationId = 1
    private var musicStreamVolume = 0
    private var isMuted = false
    private val preferences: PreferencesFactory = PreferencesFactory.providePrefernecesFactory(this)


    init {
        println("created")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (!preferences.isBlockingEnabled()) return

        println("new notification")

        val payload: AdPayload = AdPayload(sbn)
        var isMusic = false
        var isAd = false

        val activeDetectors = getDetectors()
        println("Active detectros: " + activeDetectors.size)
//        val isSpotify = activeDetectors.size > 0

        activeDetectors.forEach {
            if (it.flagAsMusic(payload)) {
                isMusic = true
            }
        }
        if (!isMusic) {
            activeDetectors.forEach {
                if (it.flagAsAdvertisement(payload)) {
                    isAd = true
                }
            }
        }

        if (isAd) {
            print("ad detected")
            muteAudio()
        }
        super.onNotificationPosted(sbn)
    }

    fun getDetectors(): List<AdDetectable> {
        return listOf<AdDetectable>(SpotifyTitleDetector())
    }

    private fun muteAudio() {
        val ignore = NotificationCompat.Action.Builder(0, "Do not block this again", null).build()

        val notification = NotificationCompat.Builder(this)
                .setContentTitle("Blocking advertisement")
                .setContentText("Press to unblock")
                .setSmallIcon(R.mipmap.icon)
                .addAction(ignore)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
        if (!isMuted) {
            doMute()
            isMuted = true
        }

        handler.postDelayed({
            manager.cancel(notificationId)
            if (isMuted) {
                isMuted = false
                doUnmute()
            }
        }, 30000)
    }

    private fun doMute() {
        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        musicStreamVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC)
        println("current volume " + musicStreamVolume)
        am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
    }

    private fun doUnmute() {
        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.setStreamVolume(AudioManager.STREAM_MUSIC, musicStreamVolume, 0)
    }

    private fun cancelOngoingNotifications() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(notificationId)

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
        println("notification removed")
    }
}