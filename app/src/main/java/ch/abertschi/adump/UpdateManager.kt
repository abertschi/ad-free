package ch.abertschi.adump

import android.content.Context
import com.github.javiersantos.appupdater.AppUpdater
import com.github.javiersantos.appupdater.enums.Display
import com.github.javiersantos.appupdater.enums.Duration
import com.github.javiersantos.appupdater.enums.UpdateFrom

/**
 * Created by abertschi on 24.04.17.
 */

class UpdateManager {

    fun appUpdaterForInAppUse(context: Context): AppUpdater {
        return AppUpdater(context)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setDuration(Duration.INDEFINITE)
                .setGitHubUserAndRepo("abertschi", "ad-free")
                .setDisplay(Display.SNACKBAR)
    }

    // Meant to be called if used in background service
    fun appUpdaterForInServiceUse(context: Context): AppUpdater {
        return AppUpdater(context)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo("abertschi", "ad-free")
                .setDisplay(Display.NOTIFICATION)
    }
}
