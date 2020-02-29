/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.view.home

import android.content.Intent

/**
 * Created by abertschi on 15.04.17.
 */

interface HomeView {
    fun showNotificationPermissionSettings()
    fun showPermissionRequired()
    fun showEnjoyAdFree()
    fun showUpdateMessage(show: Boolean)
    fun startActivity(i: Intent)
//    fun setPowerState(state: Boolean)
//    fun showPowerEnabled()
}
