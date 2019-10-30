/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.presenter

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.model.RemoteManager
import ch.abertschi.adfree.util.UpdateManager
import ch.abertschi.adfree.view.home.HomeView
import com.github.javiersantos.appupdater.AppUpdater
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger

/**
 * Created by abertschi on 15.04.17.
 */
class HomePresenter(val homeView: HomeView, val preferencesFactory: PreferencesFactory)
    : AnkoLogger {

    private var isInit: Boolean = false

    fun onCreate(context: Context) {
        isInit = true
        showPermissionRequiredIfNecessary(context)
        homeView.setPowerState(preferencesFactory.isBlockingEnabled())
    }

    fun onResume(context: Context) {
        showPermissionRequiredIfNecessary(context)
    }

    fun hasNotificationPermission(context: Context): Boolean {
        val permission =
                Settings.Secure.getString(context.contentResolver,
                        "enabled_notification_listeners")
        if (permission == null || !permission.contains(context.packageName)) {
            return false
        }
        return true
    }

    fun enabledStatusChanged(status: Boolean) {
        if (status && !preferencesFactory.isBlockingEnabled()) {
            homeView.showStatusEnabled()
        }
        preferencesFactory.setBlockingEnabled(status)
    }

    private fun showPermissionRequiredIfNecessary(context: Context) {
        if (hasNotificationPermission(context)) {
            homeView.showEnjoyAdFree()
        } else {
            homeView.showPermissionRequired()
        }
    }
}
