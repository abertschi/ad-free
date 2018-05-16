/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.ad

import ch.abertschi.adfree.detector.AdDetectable
import ch.abertschi.adfree.detector.AdPayload
import ch.abertschi.adfree.model.RemoteManager
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

/**
 * Created by abertschi on 13.08.17.
 */
class AdDetector(val detectors: List<AdDetectable>,
                 val remoteManager: RemoteManager) : AnkoLogger, AdObservable {

    private var observers: MutableList<AdObserver> = ArrayList()

    private var _pendingEvent: AdEvent? = null
    private var go: Boolean = true
    private var init: Boolean = false

    fun applyDetectors(payload: AdPayload) {
        if (!go) return

        val activeDetectors = detectors.filter { it.canHandle(payload) }
        if (activeDetectors.isNotEmpty()) {
            debug {
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

//            if (isAd) {
//                info { "ad-detected ###" }
////                info { XStream().toXML(payload) }
//                var str = XStream().toXML(payload).trim()
//                str = str.replace("\n\r", "")
//                val i = str.length / 2
//                System.out.println(str.substring(0, i))
//                System.out.flush()
//                System.out.println(str.substring(i + 1))
//                System.out.flush()
//                DevelopUtils().serializeAndWriteToFile(payload, "ad")
//            } else {
//                DevelopUtils().serializeAndWriteToFile(payload, "no_ad")
//            }

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

//        /*
//         * Wait for a while before submit event to reduce wrong detections
//         */
//        Observable.just(true).delay(0, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).map {
//
//
//        }.subscribe()
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