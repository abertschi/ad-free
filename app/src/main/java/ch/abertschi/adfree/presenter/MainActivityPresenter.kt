/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.presenter

import android.content.Context
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.view.about.AboutView
import org.jetbrains.anko.AnkoLogger

/**
 * Created by abertschi on 02.09.17.
 */
class MainActivityPresenter(val view: AboutView, val preferencesFactory: PreferencesFactory)
    : AnkoLogger {

    private var isInit: Boolean = false

    fun onCreate(context: Context) {
        isInit = true
    }

    fun onResume(context: Context) {
    }

}