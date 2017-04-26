/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump.view.home

/**
 * Created by abertschi on 15.04.17.
 */

interface HomeView {
    fun showNotificationPermissionSettings()
    fun showPermissionRequired()
    fun showEnjoyAdFree()
    fun setPowerState(state: Boolean)
}
