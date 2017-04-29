package ch.abertschi.adump

import android.content.Context
import ch.abertschi.adump.model.PreferencesFactory
import com.github.javiersantos.appupdater.AppUpdater
import com.github.javiersantos.appupdater.enums.Display
import com.github.javiersantos.appupdater.enums.Duration
import com.github.javiersantos.appupdater.enums.UpdateFrom
import java.util.*

/**
 * Created by abertschi on 24.04.17.
 */

class UpdateManager(val prefs: PreferencesFactory) {

    fun appUpdaterForInAppUse(context: Context): AppUpdater {
        return AppUpdater(context)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setDuration(Duration.NORMAL)
                .setGitHubUserAndRepo("abertschi", "ad-free")
                .setDisplay(Display.SNACKBAR)
    }

    // Meant to be called if used in background service
    fun appUpdaterForInServiceUse(context: Context): AppUpdater {
        prefs.setLastUpdateInServiceDate(Date())
        return AppUpdater(context)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo("abertschi", "ad-free")
                .setDisplay(Display.NOTIFICATION)

    }

    fun isAppUpdaterForInServiceUseFrequencyDue(): Boolean {
        val c = Calendar.getInstance()
        var i = c.get(Calendar.WEEK_OF_MONTH)
        c.set(Calendar.WEEK_OF_MONTH, --i)
        return c.time > prefs.getLastUpdateInServiceDate()
    }
}
