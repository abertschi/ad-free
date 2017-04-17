package ch.abertschi.adump.detector

import ch.abertschi.adump.model.TrackRepository

/**
 * Created by abertschi on 17.04.17.
 */
class ReflectionDetector(var trackRepository: TrackRepository) : AbstractStatusBarDetector() {

    private var reflectionActionKey: String = "Reflection_number_of_Buttons_in_Notification_is_ignored"

    override fun canHandle(payload: AdPayload): Boolean {
        return isEnabled()
                && payload != null && payload.statusbarNotification != null && payload.statusbarNotification.notification != null
                && payload.statusbarNotification.notification.actions != null
    }

    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        /**
         * If there are ads present, the like button disappears in the notification.
         * In that case, the notification only contains a previous, play and next button.
         */
        val success = payload.statusbarNotification.notification.actions.size <= 3
        if (success) {
            payload.ignoreKeys.add(reflectionActionKey)
        }
        return success
    }

    private fun isEnabled(): Boolean {
        return !trackRepository.getAllTracks().contains(reflectionActionKey)
    }

    override fun flagAsMusic(payload: AdPayload): Boolean {
        /**
         * TODO: if this method works reliably, SptofityTitleDetector becomes obsolete
         */
        return payload.statusbarNotification.notification.actions.size > 3
    }
}