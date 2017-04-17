package ch.abertschi.adump.detector

/**
 * Created by abertschi on 15.04.17.
 */
interface AdDetectable {

    fun canHandle(p: AdPayload): Boolean

    fun flagAsAdvertisement(payload: AdPayload): Boolean = false

    fun flagAsMusic(payload: AdPayload): Boolean = false
}
