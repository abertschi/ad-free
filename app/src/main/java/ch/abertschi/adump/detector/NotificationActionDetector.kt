package ch.abertschi.adump.detector

/**
 * Created by abertschi on 17.04.17.
 */
class NotificationActionDetector : AbstractStatusBarDetector() {

    override fun canHandle(payload: AdPayload): Boolean
            = super.canHandle(payload) && payload?.statusbarNotification?.notification?.actions != null

    /*
     * If there are ads present, the like button disappears in the notification.
     */
    override fun flagAsAdvertisement(payload: AdPayload): Boolean
            = payload.statusbarNotification.notification.actions.size <= 3

    /**
     * TODO: if this method works reliably, SptofityTitleDetector becomes obsolete
     */
    override fun flagAsMusic(payload: AdPayload): Boolean
            = payload.statusbarNotification.notification.actions.size > 3
}