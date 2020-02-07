/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.detector

/**
 * Created by abertschi on 15.04.17.
 */
interface AdDetectable {

    fun getMeta(): AdDetectorMeta = AdDetectorMeta(javaClass.simpleName, "not available")

    fun canHandle(p: AdPayload): Boolean

    fun flagAsAdvertisement(payload: AdPayload): Boolean = false

    fun flagAsMusic(payload: AdPayload): Boolean = false
}

data class AdDetectorMeta(val title: String, val description: String, var enabledByDef: Boolean = true,
                          var debugOnly: Boolean = false)
