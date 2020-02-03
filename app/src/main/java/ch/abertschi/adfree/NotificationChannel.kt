/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree

import android.app.Notification
import android.support.v4.app.NotificationManagerCompat
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.util.NotificationUtils


/**
 * Created by abertschi on 01.09.17.
 */
class NotificationChannel(val notificationUtils: NotificationUtils,
                          val prefs: PreferencesFactory) {

    private val defaultAdNotificationId: Int = 1000
    private val alwaysOnNotificationId: Int = 1001

    fun buildAlwaysOnNotification(): Pair<Notification, Int> {
        val not = notificationUtils.showTextNotification(alwaysOnNotificationId,
                "ad-free is running",
                "ads are monitored", {}, notifiy = false)

        return Pair(not , alwaysOnNotificationId)
    }

    fun hideAlwaysOnNotification() {
        notificationUtils.hideNotification(alwaysOnNotificationId)
    }

    fun hideDefaultAdNotification() {
        notificationUtils.hideNotification(defaultAdNotificationId)
    }

    fun showDefaultAdNotification(dismissCallable: () -> Unit = {}) {
        notificationUtils.showTextNotification(defaultAdNotificationId, "ad detected",
                "touch to unmute", dismissCallable)
    }

    fun updateAdNotification(title: String? = null, content: String? = null ) {
        notificationUtils.updateTextNotificationIfAvailable(defaultAdNotificationId, title, content)
    }


}