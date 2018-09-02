/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import ch.abertschi.adfree.detector.AdPayload
import com.thoughtworks.xstream.XStream
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.warn
import java.io.File
import java.io.FileOutputStream


/**
 * Created by abertschi on 11.12.16.
 */
class NotificationsListeners : NotificationListenerService(), AnkoLogger {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        info("notification detected")
        val context = applicationContext as AdFreeApplication
        context.adDetector.applyDetectors(AdPayload(sbn))
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }

    @Deprecated("for testing only")
    private fun recordNotification(sbn: StatusBarNotification) {
        val path = this.getExternalFilesDir(null)
        val file = File(path, "adfree.txt")
        val ids = File(path, "adfree-ids.txt")

        warn { XStream().toXML(sbn) }
        val stream = FileOutputStream(file, true)
        try {
            stream.write(XStream().toXML(sbn).toByteArray())
        } finally {
            stream.close()
        }
        val stream2 = FileOutputStream(ids, true)
        try {
            stream2.write((sbn.id.toString() + " / " + sbn.groupKey + "\n").toByteArray())
        } finally {
            stream2.close()
        }
    }
}