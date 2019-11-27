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
import android.content.Intent
import ch.abertschi.adfree.view.mod.ModActivity


/**
 * Created by abertschi on 02.09.17.
 */
class AboutPresenter(val view: AboutView, val preferencesFactory: PreferencesFactory, val context: Context)
    : AnkoLogger {

    private var isInit: Boolean = false


    fun onCreate(context: Context) {
        isInit = true
    }

    fun onResume(context: Context) {
    }

    fun showMoreSettings() {
        val myIntent = Intent(context, ModActivity::class.java)
        this.context!!.startActivity(myIntent)

    }
    
}