package ch.abertschi.adump.view.home

/**
 * Created by abertschi on 15.04.17.
 */

interface ControlView {
    fun showNotificationPermissionSettings()
    fun showPermissionRequired()
    fun showEnjoyAdFree()
    fun setPowerState(state: Boolean)
}
