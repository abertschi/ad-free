package ch.abertschi.adfree

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.service.notification.ConditionProviderService
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn
import android.support.v4.app.NotificationManagerCompat
import org.jetbrains.anko.info


class NotificationStatusManager(val context: Context) : AnkoLogger {

    private var lastStatus: ListenerStatus = ListenerStatus.UNKNOWN

    private var observers: MutableList<NotificationStatusObserver> = ArrayList()

    fun addObserver(o: NotificationStatusObserver) {
        observers.add(o)
    }

    fun notifyStatusChanged(s: ListenerStatus) {
        info { "Notification Listener Status Changed: ${s}" }
        info { observers }
        lastStatus = s
        observers.forEach { it.onStatusChanged(s) }
    }

    fun getStatus(): ListenerStatus {
        val names = NotificationManagerCompat.getEnabledListenerPackages(context)
        if (names.contains(context.packageName)) {
            lastStatus = ListenerStatus.CONNECTED
        } else {
            lastStatus = ListenerStatus.DISCONNECTED
        }

        info { "Notification Listener Status : ${lastStatus}" }
        return lastStatus
    }


    fun restartNotificationListener() {
        restartComponentService()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val componentName = ComponentName(context.applicationContext,
                    NotificationsListeners::class.java!!)

            ConditionProviderService.requestRebind(componentName)
        } else {
            warn { "restart notification listener is not supported for current v. of android" }
        }

    }

    private fun restartComponentService() {
        val pm = context.packageManager
        pm.setComponentEnabledSetting(ComponentName(this.context, NotificationsListeners::class.java!!),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
        pm.setComponentEnabledSetting(ComponentName(this.context, NotificationsListeners::class.java!!),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
    }
}

enum class ListenerStatus {
    CONNECTED, DISCONNECTED, UNKNOWN
}

interface NotificationStatusObserver {
    fun onStatusChanged(status: ListenerStatus)
}
