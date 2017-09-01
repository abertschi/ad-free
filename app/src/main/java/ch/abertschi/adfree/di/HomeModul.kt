/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.di

import android.content.Context
import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.presenter.HomePresenter
import ch.abertschi.adfree.view.home.HomeView

/**
 * Created by abertschi on 15.04.17.
 */

class HomeModul(val context: Context, val homeView: HomeView) {

    fun provideSettingsPresenter(): HomePresenter {
        val adFree = context.applicationContext as AdFreeApplication
        return HomePresenter(homeView, adFree.prefs)
    }

}
