/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.ad

import ch.abertschi.adfree.detector.AdPayload
import ch.abertschi.adfree.model.AdDetectableFactory
import ch.abertschi.adfree.model.RemoteManager
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by abertschi on 13.08.17.
 */
class AdDetector(val detectors: AdDetectableFactory,
                 val remoteManager: RemoteManager) : AnkoLogger, AdObservable {

    private var observers: MutableList<AdObserver> = ArrayList()

    private var _pendingEvent: AdEvent? = null
    private var go: Boolean = true
    private var init: Boolean = false

    fun applyDetectors(payload: AdPayload) {
        if (!go) return

        val activeDetectors = detectors.getEnabledDetectors().filter { it.canHandle(payload) }
        if (activeDetectors.isNotEmpty()) {
            info {
                "detected a spotify notification with ${activeDetectors.size} " +
                        "active ad-detectors"
            }

            var isMusic = false
            var isAd = false

            activeDetectors.filter { it.flagAsMusic(payload) }.forEach {
                isMusic = true
            }

            if (!isMusic) {
                activeDetectors.filter { it.flagAsAdvertisement(payload) }
                        .forEach { isAd = true }
            }

            if (!init) {
                fetchRemote()
                init = true
            }
            val eventType = if (isAd) EventType.IS_AD else EventType.NO_AD
            val event = AdEvent(eventType)
            submitEvent(event)
        }
    }

    private fun submitEvent(event: AdEvent) {
        synchronized(this) {
            _pendingEvent = event
        }

        synchronized(this) {
            if (_pendingEvent != null) {
                val e = _pendingEvent
                _pendingEvent = null
                notifyObservers(e!!)
            }
        }

    }

    private fun fetchRemote() {
        remoteManager.getRemoteSettingsObservable()
                .subscribe({ go = it.enabled })
    }

    fun notifyObservers(event: AdEvent) {
        observers.forEach { it.onAdEvent(event, this) }
    }

    override fun requestNoAd() {
        notifyObservers(AdEvent(EventType.NO_AD))
    }

    override fun requestIgnoreAd() {
        notifyObservers(AdEvent(EventType.IGNORE_AD))
    }

    override fun requestShowcase() {
        notifyObservers(AdEvent(EventType.SHOWCASE))
    }

    override fun requestAd() {
        notifyObservers(AdEvent(EventType.IS_AD))
    }

    override fun addObserver(obs: AdObserver) {
        observers.add(obs)
    }

    override fun deleteObserver(obs: AdObserver) {
        observers.remove(obs)
    }

}