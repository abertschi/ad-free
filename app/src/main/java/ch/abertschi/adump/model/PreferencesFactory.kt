package ch.abertschi.adump.model

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by abertschi on 15.04.17.
 */

class PreferencesFactory() {
    private val prefs = "PREFS"
    val prefIsEnabled = "IS_ENABLED"
    lateinit var context: Context

    private constructor(context: Context) : this() {
        this.context = context
    }


    fun getPreferences(): SharedPreferences = context.getSharedPreferences(prefs, Context.MODE_PRIVATE)

    fun isBlockingEnabled(): Boolean {
        return getPreferences().getBoolean(prefIsEnabled, true)
    }

    fun setBlockingEnabled(enabled: Boolean) {
        getPreferences().edit().putBoolean(prefIsEnabled, enabled).commit()
    }

    companion object {
        private var instance: PreferencesFactory? = null

        fun providePrefernecesFactory(context: Context): PreferencesFactory {
            return instance ?: preferenceFactory(context)
        }

        private fun preferenceFactory(context: Context): PreferencesFactory {
            return PreferencesFactory(context)
        }
    }
}