/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree

import android.app.Notification
import android.content.Context
import android.support.v4.app.NotificationManagerCompat
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.util.NotificationUtils
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import ch.abertschi.adfree.view.mod.ModActivity


/**
 * Created by abertschi on 01.09.17.
 */
class NotificationChannel(val notificationUtils: NotificationUtils,
                          val prefs: PreferencesFactory) {

    private val defaultAdNotificationId: Int = 1000
    private val alwaysOnNotificationId: Int = 1001

    fun buildAlwaysOnNotification(): Pair<Notification, Int> {
        val not = notificationUtils.showTextNotification(alwaysOnNotificationId,
                "ad-free",
                "Enjoy ad-free music", {}, notifiy = false)

        return Pair(not , alwaysOnNotificationId)
    }

    fun hideAlwaysOnNotification() {
        notificationUtils.hideNotification(alwaysOnNotificationId)
    }

    fun hideDefaultAdNotification() {
        notificationUtils.hideNotification(defaultAdNotificationId)
    }

    fun showDefaultAdNotification(dismissCallable: () -> Unit = {}) {
        notificationUtils.showTextNotification(defaultAdNotificationId, "Advertisement detected",
                "touch to unmute", dismissCallable)
    }

    fun updateAdNotification(title: String? = null, content: String? = null ) {
        notificationUtils.updateTextNotificationIfAvailable(defaultAdNotificationId, title, content)
    }


}