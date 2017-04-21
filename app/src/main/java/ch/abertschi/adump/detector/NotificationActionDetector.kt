package ch.abertschi.adump.detector

/**
 * Created by abertschi on 17.04.17.
 */
class NotificationActionDetector : AbstractStatusBarDetector() {

    override fun canHandle(payload: AdPayload): Boolean
            = super.canHandle(payload) && payload?.statusbarNotification?.notification?.actions != null

    override fun flagAsAdvertisement(payload: AdPayload): Boolean
            = payload.statusbarNotification.notification.actions.size <= 3

    override fun flagAsMusic(payload: AdPayload): Boolean
            = payload.statusbarNotification.notification.actions.size > 3
}