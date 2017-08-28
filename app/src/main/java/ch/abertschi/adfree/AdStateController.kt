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
import ch.abertschi.adfree.plugin.PluginHandler
import ch.abertschi.adfree.util.NotificationUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.concurrent.TimeUnit


/**
 * Created by abertschi on 14.08.17.
 */
class AdStateController(val audioController: AudioController,
                        val adPluginHandler: PluginHandler,
                        val notificationUtils: NotificationUtils) : AdObserver, AnkoLogger {

    private var activeState: EventType? = EventType.NO_AD
    private val timeoutInMs: Long = 90_000
    private var timeoutDisposable: Disposable? = null

    override fun onAdEvent(event: AdEvent, observable: AdObservable) {
        if (activeState != EventType.IS_AD && event.eventType == EventType.IS_AD) {
            activeState = EventType.IS_AD
            onAd(observable)
        }
        if (activeState != EventType.NO_AD && event.eventType == EventType.NO_AD) {
            onNoAd(observable)
        }
    }

    fun onNoAd(observable: AdObservable) {
        info { "AdEvent Change: NO_ADD" }
        activeState = EventType.NO_AD

        audioController.unmuteMusicStream()
        adPluginHandler.stopPlugin()
        notificationUtils.hideBlockingNotification()
    }

    fun onAd(observable: AdObservable) {
        info { "AdEvent Change: IS_ADD" }
        resetTimeout()
        startTimeout({
            observable.requestNoAd()
        })
        
        audioController.muteMusicStream()
        adPluginHandler.runPlugin()
        notificationUtils.showBlockingNotification(dismissCallable = {
            observable.requestNoAd()
        })
    }

    private fun startTimeout(callable: () -> Unit) {
        timeoutDisposable = Observable.just(true)
                .delay(timeoutInMs, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map {
            callable()
        }.subscribe()
    }

    private fun resetTimeout() {
        if (timeoutDisposable?.isDisposed == false) {
            timeoutDisposable!!.dispose()
        }
    }
}