/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.presenter


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import ch.abertschi.adfree.BuildConfig
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.model.RemoteManager
import ch.abertschi.adfree.model.RemoteSetting

import ch.abertschi.adfree.view.home.HomeView



import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by abertschi on 15.04.17.
 */
class HomePresenter(val homeView: HomeView, val preferencesFactory: PreferencesFactory,
                    val remoteManager: RemoteManager)
    : AnkoLogger {

    private var isInit: Boolean = false
    private var remoteSetting: RemoteSetting? = null

    fun onCreate(context: Context) {
        isInit = true
        showPermissionRequiredIfNecessary(context)
        remoteManager.getRemoteSettingsObservable()
                .subscribe { onRemoteSettingUpdate(it)}
    }

    private fun onRemoteSettingUpdate(s: RemoteSetting) {
        remoteSetting = s
        info { "current version code: " + BuildConfig.VERSION_CODE }
        info { "setting version code: " + s.versionCode }
//        info { s.toString() }
        if (s.versionCode > BuildConfig.VERSION_CODE && s.versionNotify) {
            info { "new version available. showing ui element to update" }
            homeView.showUpdateMessage(true)
        }
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

    private fun showPermissionRequiredIfNecessary(context: Context) {
        if (hasNotificationPermission(context)) {
            homeView.showEnjoyAdFree()
        } else {
            homeView.showPermissionRequired()
        }
    }

    fun onUpdateMessageClicked() {
        val browserIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse(remoteSetting?.versionUrl))
        this.homeView.startActivity(browserIntent)
    }

    fun onTroubleshooting() {
        val url = "https://abertschi.github.io/ad-free/troubleshooting/troubleshooting.html"
        val browserIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse(url))
        this.homeView.startActivity(browserIntent)

    }
}