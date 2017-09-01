/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.di

import android.content.Context
import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.presenter.SettingsPresenter
import ch.abertschi.adfree.view.setting.SettingsView

/**
 * Created by abertschi on 31.08.17.
 */
class SettingsModul(val context: Context, val settingsView: SettingsView) {

    fun provideSettingsPresenter(): SettingsPresenter {
        val adFree = context.applicationContext as AdFreeApplication
        return SettingsPresenter(settingsView, context, adFree.pluginHandler, adFree.adDetector)
    }

}
