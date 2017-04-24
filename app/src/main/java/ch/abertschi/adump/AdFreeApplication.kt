/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump

import android.app.Application
import ch.abertschi.adump.model.PreferencesFactory


/**
 * Created by abertschi on 21.04.17.
 */

class AdFreeApplication : Application() {

    private val prefs: PreferencesFactory = PreferencesFactory.providePrefernecesFactory(this)

    override fun onCreate() {
        super.onCreate()
    }
}
