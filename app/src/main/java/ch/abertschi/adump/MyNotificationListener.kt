package ch.abertschi.adump

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.support.annotation.MainThread
import ch.abertschi.adump.detector.AdDetectable
import ch.abertschi.adump.detector.AdPayload
import ch.abertschi.adump.detector.SpotifyTitleDetector
import ch.abertschi.adump.model.PreferencesFactory


/**
 * Created by abertschi on 11.12.16.
 */
class MyNotificationListener : NotificationListenerService() {

    lateinit var preferences: PreferencesFactory
    private var muteManager: MuteManager = MuteManager.instance

    val detectors: List<AdDetectable> = listOf<AdDetectable>(SpotifyTitleDetector())
    private var init: Boolean = false
    private var handler: Handler? = Handler()

    private var notificationUtils: NotificationUtils = NotificationUtils()

    init {
        println("Spotify Ad listener online")
    }

    private fun intiVars() {
        preferences = PreferencesFactory.providePrefernecesFactory(applicationContext)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (!init) {
            init = true
            intiVars()
        }
        println("status: " + preferences?.isBlockingEnabled())

        if (muteManager.isAudioMuted() || !preferences.isBlockingEnabled()) {
            return
        }

        applyDetectors(AdPayload(sbn))
    }

    fun applyDetectors(payload: AdPayload) {
        var isMusic = false
        var isAd = false

        println("Active detectros: " + detectors.size)

        detectors.forEach {
            if (it.flagAsMusic(payload)) {
                isMusic = true
            }
        }
        if (!isMusic) {
            detectors.forEach {
                if (it.flagAsAdvertisement(payload)) {
                    isAd = true
                }
            }
        }
        if (isAd) {
            print("ad detected")
            muteAudio()
        }
    }

    private fun muteAudio() {
        muteManager.doMute(this)
        notificationUtils.showBlockingNotification(this)

        handler!!.postDelayed({
            println("post delayed over")
            notificationUtils.hideBlockingNotification(this)
            muteManager.doUnmute(this)
        }, 30000)
    }


    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }
}