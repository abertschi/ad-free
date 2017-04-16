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
import ch.abertschi.adump.model.TrackRepository


/**
 * Created by abertschi on 11.12.16.
 */
class MyNotificationListener : NotificationListenerService() {

    lateinit var preferences: PreferencesFactory
    private var muteManager: MuteManager = MuteManager.instance

    lateinit var detectors: List<AdDetectable>
    private var init: Boolean = false
    private var handler: Handler? = Handler()

    private var notificationUtils: NotificationUtils = NotificationUtils()

    init {
        println("Spotify Ad listener online")
    }

    private fun intiVars() {
        preferences = PreferencesFactory.providePrefernecesFactory(applicationContext)
        detectors = listOf<AdDetectable>(SpotifyTitleDetector(TrackRepository(applicationContext, preferences)))
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

        val activeDetectors: ArrayList<AdDetectable> = ArrayList()
        detectors.forEach {
            if (it.canHandle(payload)){
                activeDetectors.add(it)
            }
        }
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
            muteAudio(payload)
        }
    }

    private fun muteAudio(payload: AdPayload) {
        muteManager.doMute(this)
        notificationUtils.showBlockingNotification(this, payload.spotifyTitleKey!!)

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