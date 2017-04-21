package ch.abertschi.adump.presenter

import android.content.Context
import android.provider.Settings
import ch.abertschi.adump.model.PreferencesFactory
import ch.abertschi.adump.view.home.ControlView
import com.github.javiersantos.appupdater.AppUpdater
import com.github.javiersantos.appupdater.enums.Display
import com.github.javiersantos.appupdater.enums.Duration
import com.github.javiersantos.appupdater.enums.UpdateFrom
import org.jetbrains.anko.AnkoLogger

/**
 * Created by abertschi on 15.04.17.
 */

class ControlPresenter(val controlView: ControlView, val preferencesFactory: PreferencesFactory) : AnkoLogger {

    lateinit private var mAppUpdater: AppUpdater

    fun onCreate(context: Context) {
        showPermissionRequiredIfNecessary(context)
        controlView.setPowerState(preferencesFactory.isBlockingEnabled())
        mAppUpdater = AppUpdater(context)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setDuration(Duration.INDEFINITE)
                .setGitHubUserAndRepo("abertschi", "ad-free")
                .setDisplay(Display.SNACKBAR)
        mAppUpdater.start()

    }

    fun onResume(context: Context) {
        showPermissionRequiredIfNecessary(context)
    }

    private fun showPermissionRequiredIfNecessary(context: Context) {
        if (hasNotificationPermission(context)) {
            controlView.showEnjoyAdFree()
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

    fun enabledStatusChanged(status: Boolean) {
        preferencesFactory.setBlockingEnabled(status)
    }
}
