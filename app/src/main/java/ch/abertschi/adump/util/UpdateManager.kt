/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump.util

/**
 * Created by abertschi on 24.04.17.
 */

class UpdateManager(val prefs: ch.abertschi.adump.model.PreferencesFactory) {

    fun appUpdaterForInAppUse(context: android.content.Context): com.github.javiersantos.appupdater.AppUpdater {
        return com.github.javiersantos.appupdater.AppUpdater(context)
                .setUpdateFrom(com.github.javiersantos.appupdater.enums.UpdateFrom.GITHUB)
                .setDuration(com.github.javiersantos.appupdater.enums.Duration.INDEFINITE)
                .setGitHubUserAndRepo("abertschi", "ad-free")
                .setDisplay(com.github.javiersantos.appupdater.enums.Display.SNACKBAR)
    }

    // Meant to be called if used in background service
    fun appUpdaterForInServiceUse(context: android.content.Context): com.github.javiersantos.appupdater.AppUpdater {
        prefs.setLastUpdateInServiceDate(java.util.Date())
        return com.github.javiersantos.appupdater.AppUpdater(context)
                .setUpdateFrom(com.github.javiersantos.appupdater.enums.UpdateFrom.GITHUB)
                .setGitHubUserAndRepo("abertschi", "ad-free")
                .setDisplay(com.github.javiersantos.appupdater.enums.Display.NOTIFICATION)

    }

    fun isAppUpdaterForInServiceUseFrequencyDue(): Boolean {
        val c = java.util.Calendar.getInstance()
        var i = c.get(java.util.Calendar.WEEK_OF_MONTH)
        c.set(java.util.Calendar.WEEK_OF_MONTH, --i)
        return c.time > prefs.getLastUpdateInServiceDate()
    }
}
