package ch.abertschi.adump

import android.app.IntentService
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat


/**
 * Created by abertschi on 16.04.17.
 */
class NotificationUtils {

    companion object {
        val actionDismiss = "actionDismiss"
        val actionIgnore = "actionIgnore"
        val blockingNotificationId = 1
    }

    fun showBlockingNotification(context: Context) {
        println("showing notification " + System.currentTimeMillis())
        val ignoreAction = NotificationCompat.Action.Builder(0, "Do not block this again",
                PendingIntent.getService(context, 0, Intent(context
                        , NotificationInteractionService::class.java).setAction(actionIgnore)
                        , PendingIntent.FLAG_ONE_SHOT)).build()

        val dismissIntent = PendingIntent
                .getService(context, 0, Intent(context
                        , NotificationInteractionService::class.java).setAction(actionDismiss)
                        , PendingIntent.FLAG_ONE_SHOT)

        val notification = NotificationCompat.Builder(context)
                .setContentTitle("Blocking advertisement")
                .setContentText("Touch to unmute")
                .setSmallIcon(R.mipmap.icon)
                .addAction(ignoreAction)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(dismissIntent)
                .build()

        val manager = NotificationManagerCompat.from(context)
        manager.notify(blockingNotificationId, notification)
    }

    fun hideBlockingNotification(context: Context) {
        val manager = NotificationManagerCompat.from(context)
        manager.cancel(blockingNotificationId)
    }
}

class NotificationInteractionService : IntentService(NotificationInteractionService::class.simpleName) {

    private val utils: NotificationUtils = NotificationUtils()

    init {
        print("Notification action service created")
    }

    override fun onHandleIntent(intent: Intent?) {
        print("actionKey: " + intent.toString())
        if (intent == null || intent.action == null) {
            return
        }
        val actionKey: String = intent!!.action
        if (actionKey.equals(NotificationUtils.actionDismiss)) {
            MuteManager.instance.doUnmute(this)
            utils.hideBlockingNotification(this)
        } else if (actionKey.equals(NotificationUtils.actionIgnore)) {
            MuteManager.instance.doUnmute(this)
            utils.hideBlockingNotification(this)
        }
    }
}


