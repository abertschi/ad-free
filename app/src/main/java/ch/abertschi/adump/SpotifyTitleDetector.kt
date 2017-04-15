package ch.abertschi.adump

/**
 * Created by abertschi on 15.04.17.
 */
class SpotifyTitleDetector : AbstractStatusBarDetector() {

    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        val notification = payload.statusbarNotification.notification
        return notification.tickerText != null && notification.tickerText
                .toString().toLowerCase()
                .trim().contains("spotify")
    }

    override fun flagAsMusic(payload: AdPayload): Boolean {
        return super.flagAsMusic(payload)

    }
}