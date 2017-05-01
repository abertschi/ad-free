/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree

import android.app.Application
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.util.UsageFeedback


/**
 * Created by abertschi on 21.04.17.
 */

class AdFreeApplication : Application() {

    lateinit private var prefs: PreferencesFactory
    lateinit private var feedback: UsageFeedback

    override fun onCreate() {
        super.onCreate()
        prefs = PreferencesFactory.providePrefernecesFactory(applicationContext)
        feedback = UsageFeedback(applicationContext, prefs)
        feedback.trackFirstRun()
    }

    fun getPreferencesFactory(): PreferencesFactory = prefs

    fun getFeedback(): UsageFeedback = feedback
}
