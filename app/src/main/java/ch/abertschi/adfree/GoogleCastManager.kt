package ch.abertschi.adfree

import android.app.Notification
import android.service.notification.StatusBarNotification
import ch.abertschi.adfree.model.PreferencesFactory
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.warn
import java.lang.Exception

class GoogleCastManager(val prefs: PreferencesFactory) : AnkoLogger {

    companion object {
        private val ID = "com.google.android.gms|g:com.google.android.gms.cast.rcn.NOTIFICATIONS"
    }

    private var enabled: Boolean = false
    private var action: Notification.Action? = null

    init {
        enabled = prefs.isGoogleCastEnabled()
    }

    fun setEnabled(e: Boolean) {
        enabled = e
        prefs.setGoogleCastEnabled(e)
    }

    fun isEnabled(): Boolean {
        return enabled
    }

//            val act = sbn.notification.actions.get(1)
//            info { act.title }
//            act?.run {
//                if (act.title.contains("Unmute")) {
//                    act.actionIntent.send()
//                    info { "send intent" }
//                }
//            }
//            recordNotification(sbn)
//            info { "debug this" }

    fun updateNotification(sbn: StatusBarNotification) {
        if (sbn.groupKey.contains(ID)) {
            info { sbn.groupKey }
            if (sbn.notification?.actions?.size == 4) {
                val act = sbn.notification.actions[1]
                info { "updating action for chromecast manager"}
                info { act.title }
                info { act }
                action = act
            }
        }
    }

    fun muteAudio() {
        if (!enabled) return
        try {
            info { "muting google cast audio with action $action" }
            action?.run { action?.actionIntent?.send() }
        } catch (e: Exception) {
            warn { "muting failed" }
            warn { e }
        }

    }

    fun unmuteAudio() {
        if (!enabled) return
        try {
            info { "unmuting google cast audio with action $action" }
            action?.run { action?.actionIntent?.send() }
        } catch (e: Exception) {
            warn { "unmuting failed" }
            warn { e }
        }
    }
}