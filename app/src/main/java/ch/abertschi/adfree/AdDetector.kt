/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree

import ch.abertschi.adfree.ad.AdEvent
import ch.abertschi.adfree.ad.AdObservable
import ch.abertschi.adfree.ad.AdObserver
import ch.abertschi.adfree.ad.EventType
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
    private var pendingEvent: AdEvent? = null
    
    fun applyDetectors(payload: AdPayload) {
        val activeDetectors = detectors.filter { it.canHandle(payload) }

        if (activeDetectors.isNotEmpty()) {
            debug("detected a spotify notification with ${activeDetectors.size} " +
                    "active ad-detectors")

            var isMusic = false
            var isAd = false

            activeDetectors.filter { it.flagAsMusic(payload) }
                    .first().let { isMusic = true }

            if (!isMusic) {
                activeDetectors.filter { it.flagAsAdvertisement(payload) }
                        .first().let { isAd = true }
            }

            val eventType = if (isAd) EventType.IS_AD else EventType.NO_ADD
            val event = AdEvent(eventType, payload)

            synchronized(this) {
                pendingEvent = event
            }
            Observable.just(true).delay(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).map {
                synchronized(this) {
                    if (pendingEvent != null) {
                        val e = pendingEvent
                        pendingEvent = null
                        notifyObservers(e!!)
                    }
                }
            }.subscribe()
        }
    }

    fun notifyObservers(event: AdEvent) {
        observers.forEach { it.onAdEvent(event, this) }
    }

    override fun addObserver(obs: AdObserver) {
        observers.add(obs)
    }

    override fun deleteObserver(obs: AdObserver) {
        observers.remove(obs)
    }

}