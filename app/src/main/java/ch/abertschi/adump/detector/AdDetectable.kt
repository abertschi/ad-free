package ch.abertschi.adump.detector

import ch.abertschi.adump.detector.AdPayload

/**
 * Created by abertschi on 15.04.17.
 */
interface AdDetectable {

    fun canHandle(p: AdPayload): Boolean

    fun flagAsAdvertisement(payload: AdPayload): Boolean {
        return false
    }

    fun flagAsMusic(payload: AdPayload): Boolean {
        return false
    }
}
