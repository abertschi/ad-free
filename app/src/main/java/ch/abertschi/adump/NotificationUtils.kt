package ch.abertschi.adump

import android.app.IntentService
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import ch.abertschi.adump.model.PreferencesFactory
import ch.abertschi.adump.model.TrackRepository


/**
 * Created by abertschi on 16.04.17.
 */
class NotificationUtils {

    companion object {
        val actionDismiss = "actionDismiss"
        val actionIgnore = "actionIgnore"
        val blockingNotificationId = 1
        val ignoreIntentExtraKey = "notificationTitle"
    }

    fun showBlockingNotification(context: Context, ignoreKeys: ArrayList<String> = ArrayList<String>()) {
        println("showing notification " + System.currentTimeMillis())

        ignoreKeys.forEach {
            println("showBlockingNotification with ignoreKey: " + it)
        }
        val ignoreIntent = Intent(context
                , NotificationInteractionService::class.java).setAction(actionIgnore).putExtra(ignoreIntentExtraKey, ignoreKeys)

        val ignoreAction = NotificationCompat.Action.Builder(0, "Do not block this again",
                PendingIntent.getService(context, 0, ignoreIntent
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
    private val trackRepository: TrackRepository = TrackRepository(this, PreferencesFactory.providePrefernecesFactory(this)) // TODO: singelton?

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

            intent.extras.keySet().forEach {
                println("key: " + it)
            }

            val ignoreKeys: List<String>? = intent.getStringArrayListExtra(NotificationUtils.ignoreIntentExtraKey)
            if (ignoreKeys != null && ignoreKeys.size > 0) {
                ignoreKeys.forEach {
                    trackRepository.addTrack(it)
                }
            }
        }
    }
}


