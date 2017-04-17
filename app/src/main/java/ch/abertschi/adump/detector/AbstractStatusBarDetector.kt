package ch.abertschi.adump.detector

/**
 * Created by abertschi on 13.12.16.
 */
abstract class AbstractStatusBarDetector : AdDetectable {

    companion object {
        private val SPOTIFY_PACKAGE = "com.spotify"
    }

    override fun canHandle(payload: AdPayload): Boolean
            = payload?.statusbarNotification?.key?.toLowerCase()?.contains(SPOTIFY_PACKAGE) ?: false

}
