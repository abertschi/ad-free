package ch.abertschi.adump.detector

import ch.abertschi.adump.model.TrackRepository

/**
 * Created by abertschi on 17.04.17.
 */
class NotificationActionDetector(var trackRepository: TrackRepository) : AbstractStatusBarDetector() {

//    private var reflectionActionKey: String = "Reflection_number_of_Buttons_in_Notification_is_ignored"

    override fun canHandle(payload: AdPayload): Boolean {
        return super.canHandle(payload)
//                && isEnabled()
                && payload != null && payload.statusbarNotification != null && payload.statusbarNotification.notification != null
                && payload.statusbarNotification.notification.actions != null
    }

    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        /**
         * If there are ads present, the like button disappears in the notification.
         */
        return payload.statusbarNotification.notification.actions.size <= 3
    }

//    private fun isEnabled(): Boolean {
//        return !trackRepository.getAllTracks().contains(reflectionActionKey)
//    }

    override fun flagAsMusic(payload: AdPayload): Boolean {
        /**
         * TODO: if this method works reliably, SptofityTitleDetector becomes obsolete
         */
        return payload.statusbarNotification.notification.actions.size > 3
    }
}