/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump.presenter

import android.content.Context
import android.provider.Settings
import ch.abertschi.adump.util.UpdateManager
import ch.abertschi.adump.model.PreferencesFactory
import ch.abertschi.adump.model.RemoteManager
import ch.abertschi.adump.view.home.HomeView
import com.github.javiersantos.appupdater.AppUpdater
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger

/**
 * Created by abertschi on 15.04.17.
 */

class HomePresenter(val homeView: HomeView, val preferencesFactory: PreferencesFactory) : AnkoLogger {

    fun onCreate(context: Context) {
        showPermissionRequiredIfNecessary(context)
        homeView.setPowerState(preferencesFactory.isBlockingEnabled())

        RemoteManager(preferencesFactory)
                .getRemoteSettingsObservable()
                .subscribe({
                    if (it.useGithubReleasesForUpdateReminder && it.showSeakbarOnUpdate) {
                        Observable.create<AppUpdater> { source ->
                            val updater = UpdateManager(preferencesFactory).appUpdaterForInAppUse(context)
                            updater.start()
                            source.onComplete()
                        }.observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
                    }
                })
    }

    fun onResume(context: Context) {
        showPermissionRequiredIfNecessary(context)
    }

    private fun showPermissionRequiredIfNecessary(context: Context) {
        if (hasNotificationPermission(context)) {
            homeView.showEnjoyAdFree()
        } else {
            homeView.showPermissionRequired()
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
