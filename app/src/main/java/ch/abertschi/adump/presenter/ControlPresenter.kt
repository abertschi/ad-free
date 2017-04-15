package ch.abertschi.adump.presenter

import android.content.Context
import android.provider.Settings
import ch.abertschi.adump.model.PreferencesFactory
import ch.abertschi.adump.view.ControlView

/**
 * Created by abertschi on 15.04.17.
 */

class ControlPresenter(val controlView: ControlView, val preferencesFactory: PreferencesFactory) {

    fun onCreate(context: Context) {
        showPermissionRequiredIfNecessary(context)
    }

    fun onResume(context: Context) {
        println("on resume called")
        showPermissionRequiredIfNecessary(context)
    }

    private fun showPermissionRequiredIfNecessary(context: Context) {
        if (hasNotificationPermission(context)) {
            controlView.showEnjoyAdFree()
            if (preferencesFactory.isBlockingEnabled()) {
                controlView.showDisabledText()
            } else {
                controlView.showEnabledText()
            }
        } else {
            controlView.showPermissionRequired()
        }
    }

    fun hasNotificationPermission(context: Context): Boolean {
        val permission = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        if (permission == null || !permission.contains(context.packageName)) {
            return false
        }
        return true
    }

    fun enabledStatusChanged() {
        if (preferencesFactory.isBlockingEnabled()) {
            controlView.showDisabledText()
        } else {
            controlView.showEnabledText()
        }
        preferencesFactory.setBlockingEnabled(!preferencesFactory.isBlockingEnabled())
    }

}
