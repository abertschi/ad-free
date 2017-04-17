package ch.abertschi.adump

import android.os.Handler
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import ch.abertschi.adump.detector.AdDetectable
import ch.abertschi.adump.detector.AdPayload
import ch.abertschi.adump.detector.ReflectionDetector
import ch.abertschi.adump.detector.SpotifyTitleDetector
import ch.abertschi.adump.model.PreferencesFactory
import ch.abertschi.adump.model.TrackRepository
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info


/**
 * Created by abertschi on 11.12.16.
 */
class MyNotificationListener : NotificationListenerService(), AnkoLogger {

    lateinit var preferences: PreferencesFactory
    private var muteManager: MuteManager = MuteManager.instance

    lateinit var detectors: List<AdDetectable>
    private var init: Boolean = false
    private var handler: Handler? = Handler()
    lateinit var trackRepository: TrackRepository

    private var notificationUtils: NotificationUtils = NotificationUtils()

    init {
        info("Spotify Ad listener online")
    }

    private fun intiVars() {
        preferences = PreferencesFactory.providePrefernecesFactory(applicationContext)
        trackRepository = TrackRepository(applicationContext, preferences)
        detectors = listOf<AdDetectable>(SpotifyTitleDetector(trackRepository), ReflectionDetector(trackRepository))
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (!init) {
            init = true
            intiVars()
        }

        if (muteManager.isAudioMuted() || !preferences.isBlockingEnabled()) {
            return
        }

        debug("Spotify Ad Listener is active")
        applyDetectors(AdPayload(sbn))
    }

    fun applyDetectors(payload: AdPayload) {
        var isMusic = false
        var isAd = false

        val activeDetectors: ArrayList<AdDetectable> = ArrayList()
        detectors.forEach {
            if (it.canHandle(payload)) {
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
            muteAudio(payload)
        }
    }

    private fun muteAudio(payload: AdPayload) {
        muteManager.doMute(this)
        notificationUtils.showBlockingNotification(this, payload.ignoreKeys)

        handler!!.postDelayed({
            notificationUtils.hideBlockingNotification(this)
            muteManager.doUnmute(this)
        }, 30000)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }
}