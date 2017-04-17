package ch.abertschi.adump.detector

import ch.abertschi.adump.detector.AdPayload

/**
 * Created by abertschi on 13.12.16.
 */
abstract class AbstractStatusBarDetector() : AdDetectable {

    companion object {
        private val SPOTIFY_PACKAGE = "com.spotify"
    }

    override fun canHandle(payload: AdPayload): Boolean {
        return payload != null &&
                payload.statusbarNotification != null
                && payload.statusbarNotification.key != null
                && payload.statusbarNotification.key.toLowerCase().contains(SPOTIFY_PACKAGE)

    }
}
