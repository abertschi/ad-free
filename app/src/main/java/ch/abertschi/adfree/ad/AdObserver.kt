/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.ad

import ch.abertschi.adfree.detector.AdPayload

/**
 * Created by abertschi on 13.08.17.
 */
interface AdObserver {

    fun onAdEvent(event: AdEvent, observable: AdObservable)
}

data class AdEvent(val eventType: EventType, val adPayload: AdPayload?)

enum class EventType {
    IS_AD, NO_ADD, IGNORE_AD
}