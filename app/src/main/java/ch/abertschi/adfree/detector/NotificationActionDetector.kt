/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.detector

/**
 * Created by abertschi on 17.04.17.
 *
 * Detector which checks for number of control buttons
 */
class NotificationActionDetector : AbstractStatusBarDetector() {

    override fun canHandle(payload: AdPayload): Boolean
            = super.canHandle(payload) && payload?.statusbarNotification?.notification?.actions != null

    override fun flagAsAdvertisement(payload: AdPayload): Boolean
            = payload.statusbarNotification.notification.actions.size <= 3 || true

}