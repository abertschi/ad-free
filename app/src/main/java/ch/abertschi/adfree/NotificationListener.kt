/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import ch.abertschi.adfree.detector.AdPayload
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


/**
 * Created by abertschi on 11.12.16.
 */
class NotificationListener : NotificationListenerService(), AnkoLogger {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        info("notification detected")

        val context = applicationContext as AdFreeApplication
        context.adDetector.applyDetectors(AdPayload(sbn))
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }
}