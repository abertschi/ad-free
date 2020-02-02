/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import ch.abertschi.adfree.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


/**
 * Created by abertschi on 16.04.17.
 */
class NotificationUtils(val context: Context) : AnkoLogger {

    public companion object {
        val actionDismiss = "actionDismiss"
        val blockingNotificationId = 1
        val textgNotificationId = 2
        val CHANNEL_ID = "ad_channel"

        private val actionDismissCallables: ArrayList<() -> Unit> = ArrayList()
    }

    private val updateNotificationMap: MutableMap<Int, NotificationCompat.Builder> = HashMap()

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    fun updateTextNotificationIfAvailable(id: Int, title: String? = null, content: String? = null) {
        val builder = updateNotificationMap.get(id)
        builder?.let {
            if (title != null) it.setContentTitle(title)
            if (content != null) it.setContentText(content)

            val manager = NotificationManagerCompat.from(context)
            manager.notify(id, builder.build())
        }
    }


    fun showTextNotification(id: Int, title: String, content: String = "",
                             dismissCallable: () -> Unit = {},
                             priority: Int = NotificationCompat.PRIORITY_HIGH, notifiy: Boolean = true): Notification {


        if (updateNotificationMap.containsKey(id)) {
            val b = updateNotificationMap.get(id)
            var n: Notification? = null
            b?.let {
                if (title != null) it.setContentTitle(title)
                if (content != null) it.setContentText(content)
                val manager = NotificationManagerCompat.from(context)
                n = b.build()
                manager.notify(id, n)

            }
            return n!!
        } else {


            val dismissIntent = PendingIntent
                    .getService(context, 0, Intent(context
                            , NotificationInteractionService::class.java).setAction(actionDismiss)
                            , PendingIntent.FLAG_ONE_SHOT)

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.icon)
                    .setPriority(priority)
                    .setContentIntent(dismissIntent)

            if (content != "") {
                builder.setContentText(content)
            }


            updateNotificationMap[id] = builder
            val notification = builder.build()
//        notification.flags = notification.flags or (Notification.FLAG_NO_CLEAR or
//                Notification.FLAG_ONGOING_EVENT)
//            notification.flags = notification.flags or
//                    Notification.FLAG_ONGOING_EVENT



            synchronized(actionDismissCallables) {
                actionDismissCallables.add(dismissCallable)
            }

            val manager = NotificationManagerCompat.from(context)
            if (notifiy) {
                manager.notify(id, notification)
            }
            return notification
        }
    }

//    fun showBlockingNotification(dismissCallable: () -> Unit) {
//        val dismissIntent = PendingIntent
//                .getService(context, 0, Intent(context
//                        , NotificationInteractionService::class.java).setAction(actionDismiss)
//                        , PendingIntent.FLAG_ONE_SHOT)
//
//        val notification = NotificationCompat.Builder(context)
//                .setContentTitle("Ad detected")
//                .setContentText("Touch to unmute")
//                .setSmallIcon(R.mipmap.icon)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(dismissIntent)
//                .build()
//
//        notification.flags = notification.flags or (Notification.FLAG_NO_CLEAR or
//                Notification.FLAG_ONGOING_EVENT)
//
//        synchronized(actionDismissCallables) {
//            actionDismissCallables.add(dismissCallable)
//        }
//        val manager = NotificationManagerCompat.from(context)
//        manager.notify(blockingNotificationId, notification)
//    }


    fun hideNotification(id: Int) {
        val manager = NotificationManagerCompat.from(context)
        updateNotificationMap.remove(id)
        manager.cancel(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val notificationManager = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = CHANNEL_ID
        val name = "Ad blocking"
        val description = "Ad blocking notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)
        // Configure the notification channel.
        channel.description = description
//        channel.setShowBadge(false)
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationManager.createNotificationChannel(channel)
    }

    class NotificationInteractionService :
            IntentService(NotificationInteractionService::class.simpleName), AnkoLogger {
        init {
            info("NotificationInteractionService created")
        }

        override fun onHandleIntent(intent: Intent?) {
            if (intent == null || intent.action == null) {
                return
            }
            val actionKey: String = intent!!.action
            if (actionKey.equals(actionDismiss)) {
                synchronized(actionDismissCallables) {
                    actionDismissCallables.forEach {
                        it()
                    }
                    actionDismissCallables.clear()
                }
            }
        }
    }

}


