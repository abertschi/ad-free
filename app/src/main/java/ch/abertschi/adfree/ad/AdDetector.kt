/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.ad

import ch.abertschi.adfree.detector.AdDetectable
import ch.abertschi.adfree.detector.AdPayload
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import java.util.concurrent.TimeUnit

/**
 * Created by abertschi on 13.08.17.
 */
class AdDetector(val detectors: List<AdDetectable>) : AnkoLogger, AdObservable {

    private var observers: MutableList<AdObserver> = ArrayList()

    private var _pendingEvent: AdEvent? = null

    fun applyDetectors(payload: AdPayload) {
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

            val eventType = if (isAd) EventType.IS_AD else EventType.NO_AD
            val event = AdEvent(eventType)
            submitEvent(event)
        }
    }

    private fun submitEvent(event: AdEvent) {
        synchronized(this) {
            _pendingEvent = event
        }

        /*
         * Wait for a while before submit event to reduce wrong detections
         */
        Observable.just(true).delay(100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map {

            synchronized(this) {
                if (_pendingEvent != null) {
                    val e = _pendingEvent
                    _pendingEvent = null
                    notifyObservers(e!!)
                }
            }
        }.subscribe()
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