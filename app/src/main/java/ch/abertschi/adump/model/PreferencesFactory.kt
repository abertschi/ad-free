package ch.abertschi.adump.model

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by abertschi on 15.04.17.
 */

class PreferencesFactory(context: Context) {
    private val prefsKey = "PREFS"
    private val prefIsEnabled = "IS_ENABLED"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsKey, Context.MODE_PRIVATE)

    fun isBlockingEnabled(): Boolean {
        return prefs.getBoolean(prefIsEnabled, true)
    }

    fun setBlockingEnabled(enabled: Boolean) {
        println("setting power status to " + enabled)
        prefs.edit().putBoolean(prefIsEnabled, enabled).commit()
    }

    fun getPreferences(): SharedPreferences = prefs

    companion object {
        private var instance: PreferencesFactory? = null

        fun providePrefernecesFactory(context: Context): PreferencesFactory {
            return instance ?: preferenceFactory(context)
        }

        private fun preferenceFactory(context: Context): PreferencesFactory {
            instance = PreferencesFactory(context)
            return instance!!
        }
    }
}