/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump

import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


/**
 * Created by abertschi on 16.04.17.
 */
class NotificationUtils : AnkoLogger {

    companion object {
        val actionDismiss = "actionDismiss"
        val blockingNotificationId = 1
        val textgNotificationId = 2

        private val actionDismissCallables: ArrayList<() -> Unit> = ArrayList()
    }

    fun showTextNotification(context: Context, title: String, content: String = "") {
        val notification = NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.icon)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build()

        val manager = NotificationManagerCompat.from(context)
        manager.notify(textgNotificationId, notification)
    }

    fun showBlockingNotification(context: Context, dismissCallable: () -> Unit) {
        val dismissIntent = PendingIntent
                .getService(context, 0, Intent(context
                        , NotificationInteractionService::class.java).setAction(actionDismiss)
                        , PendingIntent.FLAG_ONE_SHOT)

        val notification = NotificationCompat.Builder(context)
                .setContentTitle("Ad detected")
                .setContentText("Touch to unmute")
                .setSmallIcon(R.mipmap.icon)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(dismissIntent)
                .build()

        notification.flags = notification.flags or (Notification.FLAG_NO_CLEAR or Notification.FLAG_ONGOING_EVENT)

        synchronized(actionDismissCallables) {
            actionDismissCallables.add(dismissCallable)
        }
        val manager = NotificationManagerCompat.from(context)
        manager.notify(blockingNotificationId, notification)
    }

    fun hideBlockingNotification(context: Context) {
        val manager = NotificationManagerCompat.from(context)
        manager.cancel(blockingNotificationId)
    }

    class NotificationInteractionService : IntentService(NotificationInteractionService::class.simpleName), AnkoLogger {
        init {
            info("NotificationInteractionService created")
        }

        override fun onHandleIntent(intent: Intent?) {
            if (intent == null || intent.action == null) {
                return
            }
            val actionKey: String = intent!!.action
            if (actionKey.equals(NotificationUtils.actionDismiss)) {
                synchronized(NotificationUtils.actionDismissCallables) {
                    actionDismissCallables.forEach {
                        it()
                    }
                    actionDismissCallables.clear()
                }
            }
        }
    }

//    else if (actionKey.equals(NotificationUtils.actionIgnore)) {
//        AudioController.instance.unmuteMusicStream(this)
//        utils.hideBlockingNotification(this)
//
//        val ignoreKeys: List<String>? = intent.getStringArrayListExtra(NotificationUtils.ignoreIntentExtraKey)
//        if (ignoreKeys != null && ignoreKeys.size > 0) {
//            ignoreKeys.forEach {
//                trackRepository.addTrack(it)
//            }
//        }
//    }

// As long as NotificationActionDetector works reliably, no need to filter out false positives
//        val ignoreIntent = Intent(context
//                , NotificationInteractionService::class.java).setAction(actionIgnore).putExtra(ignoreIntentExtraKey, ignoreKeys)
//
//        val ignoreAction = NotificationCompat.Action.Builder(0, "Do not block this again",
//                PendingIntent.getService(context, 0, ignoreIntent
//                        , PendingIntent.FLAG_ONE_SHOT)).build()
}


