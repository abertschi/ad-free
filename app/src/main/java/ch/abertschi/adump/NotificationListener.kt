/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import ch.abertschi.adump.detector.AdDetectable
import ch.abertschi.adump.detector.AdPayload
import ch.abertschi.adump.detector.NotificationActionDetector
import ch.abertschi.adump.model.PreferencesFactory
import ch.abertschi.adump.model.TrackRepository
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info


/**
 * Created by abertschi on 11.12.16.
 */
class NotificationListener : NotificationListenerService(), AnkoLogger {

    lateinit var preferences: PreferencesFactory
    private var audioController: AudioController = AudioController.instance

    lateinit var detectors: List<AdDetectable>
    private var init: Boolean = false
    lateinit var trackRepository: TrackRepository

    init {
        info("Spotify Ad listener online")
    }

    private fun intiVars() {
        preferences = PreferencesFactory.providePrefernecesFactory(applicationContext)
        trackRepository = TrackRepository(applicationContext, preferences)
        detectors = listOf<AdDetectable>(NotificationActionDetector())
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (!init) {
            init = true
            intiVars()
        }
        if (audioController.isMusicStreamMuted() || !preferences.isBlockingEnabled()) {
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

            audioController.muteMusicAndRunActivePlugin(this)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }
}