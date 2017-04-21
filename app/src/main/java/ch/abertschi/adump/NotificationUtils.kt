package ch.abertschi.adump

import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import ch.abertschi.adump.model.PreferencesFactory
import ch.abertschi.adump.model.TrackRepository
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info


/**
 * Created by abertschi on 16.04.17.
 */
class NotificationUtils : AnkoLogger {

    companion object {
        val actionDismiss = "actionDismiss"
        val actionIgnore = "actionIgnore"
        val blockingNotificationId = 1
        val ignoreIntentExtraKey = "notificationTitle"
    }

    fun showBlockingNotification(context: Context, ignoreKeys: ArrayList<String> = ArrayList<String>()) {
        ignoreKeys.forEach {
            debug("ignore keys:" + it)
        }

// As long as NotificationActionDetector works reliably, no need to filter out false positives
//        val ignoreIntent = Intent(context
//                , NotificationInteractionService::class.java).setAction(actionIgnore).putExtra(ignoreIntentExtraKey, ignoreKeys)
//
//        val ignoreAction = NotificationCompat.Action.Builder(0, "Do not block this again",
//                PendingIntent.getService(context, 0, ignoreIntent
//                        , PendingIntent.FLAG_ONE_SHOT)).build()

        val dismissIntent = PendingIntent
                .getService(context, 0, Intent(context
                        , NotificationInteractionService::class.java).setAction(actionDismiss)
                        , PendingIntent.FLAG_ONE_SHOT)


        val notification = NotificationCompat.Builder(context)
                .setContentTitle("Blocking advertisement")
                .setContentText("Touch to unmute")
                .setSmallIcon(R.mipmap.icon)
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

class NotificationInteractionService : IntentService(NotificationInteractionService::class.simpleName), AnkoLogger {

    private val utils: NotificationUtils = NotificationUtils()
    private val trackRepository: TrackRepository = TrackRepository(this, PreferencesFactory.providePrefernecesFactory(this)) // TODO: singelton?

    init {
        info("NotificationInteractionService created")
    }

    override fun onHandleIntent(intent: Intent?) {
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

            val ignoreKeys: List<String>? = intent.getStringArrayListExtra(NotificationUtils.ignoreIntentExtraKey)
            if (ignoreKeys != null && ignoreKeys.size > 0) {
                ignoreKeys.forEach {
                    trackRepository.addTrack(it)
                }
            }
        }
    }
}


