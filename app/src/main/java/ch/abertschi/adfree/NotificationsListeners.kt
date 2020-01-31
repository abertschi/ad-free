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
import android.content.Context.NOTIFICATION_SERVICE
import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues.TAG
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.v4.app.NotificationCompat
import org.jetbrains.anko.toast


/**
 * Created by abertschi on 11.12.16.
 */
class NotificationsListeners : NotificationListenerService(), AnkoLogger {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
//        info("notification detected")
        val context = applicationContext as AdFreeApplication
        context.adDetector.applyDetectors(AdPayload(sbn))
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        toast("onListenerDisconnected")
    }

    override fun onListenerHintsChanged(hints: Int) {
        super.onListenerHintsChanged(hints)
        toast("onListenerHintsChanged")
    }

    override fun onListenerConnected() {

        super.onListenerConnected()
        toast("onListenerConnected")
        info {"Service Reader Connected"}
        val context = applicationContext as AdFreeApplication
        val id = 2
        val not = context.notificationUtils
                .showTextNotification(id, "Ad-Free is running",
                        "ads are monitored", {})
        startForeground(id, not)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        toast("onStartCommand")
        return Service.START_STICKY
    }

    @Deprecated("for testing only")
    private fun recordNotification(sbn: StatusBarNotification) {
        val path = this.getExternalFilesDir(null)
        val file = File(path, "adfree-new.txt")
        val ids = File(path, "adfree-ids-new.txt")

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