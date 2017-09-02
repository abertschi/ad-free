/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.di

import android.content.Context
import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.presenter.AboutPresenter
import ch.abertschi.adfree.view.about.AboutView

/**
 * Created by abertschi on 02.09.17.
 */

class AboutModul(val context: Context, val aboutView: AboutView) {

    fun provideAboutPresenter(): AboutPresenter {
        val adFree = context.applicationContext as AdFreeApplication
        return AboutPresenter(aboutView, adFree.prefs)
    }

}