/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree

import ch.abertschi.adfree.util.NotificationUtils

/**
 * Created by abertschi on 01.09.17.
 */
class NotificationChannel(val notificationUtils: NotificationUtils) {

    private val defaultAdNotificationId: Int = 1000

    fun hideDefaultAdNotification() {
        notificationUtils.hideNotification(defaultAdNotificationId)
    }

    fun showDefaultAdNotification(dismissCallable: () -> Unit = {}) {
        notificationUtils.showTextNotification(defaultAdNotificationId, "Ad detected",
                "touch to unmute", dismissCallable)
    }

    fun updateAdNotification(title: String? = null, content: String? = null ) {
        notificationUtils.updateTextNotificationIfAvailable(defaultAdNotificationId, title, content)
    }
}