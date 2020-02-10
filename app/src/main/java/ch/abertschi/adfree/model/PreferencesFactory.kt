/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.model

import android.content.Context
import android.content.SharedPreferences
import ch.abertschi.adfree.detector.AdDetectable
import org.jetbrains.anko.AnkoLogger
import java.util.*

/**
 * Created by abertschi on 15.04.17.
 */

class PreferencesFactory(context: Context) : AnkoLogger {
    private val prefsKey = "PREFS"
    private val prefIsEnabled = "IS_ENABLED"
    private val prefsLastUpdateInServiceDate = "LAST_UPDATE_IN_SERVICE"
    private val prefsFirstRun = "FIRST_RUN"
    private val prefsAudioVolume: String = "AUDIO_KEY"
    private val prefsStreamMusicAudioVolume: String = "AUDIO_STREAM_MUSIC_KEY"
    private val prefsActivePlugin: String = "ACTIVE_PLUGIN"
    private val prefsLocalMusic: String = "location_local_music"
    private val prefsPlayUntilEnd: String = "location_local_music_play_until_end"
    private val prefsAdDetectableMetaPrefix: String = "detectable_"

    private val prefsDelaySound = "DELAY_SOUND"
    private val prefsAlwaysOnNoti = "ALWAYS_ON_NOTI"
    private val prefsIsDebugDetectors = "DEBUG_DETECTORS_ENABLED"
    private val prefsGoogleCast = "CAST_ENABLED"
    private val prefsLoopPlayback: String = "location_local_music_loop"

    private val prefs: SharedPreferences = context.getSharedPreferences(prefsKey, Context.MODE_PRIVATE)

    fun isBlockingEnabled(): Boolean {
        return prefs.getBoolean(prefIsEnabled, true)
    }

    fun setBlockingEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(prefIsEnabled, enabled).commit()
    }

    fun getLastUpdateInServiceDate(): Date {
        return Date(prefs.getLong(prefsLastUpdateInServiceDate, Date().time) * 1000)
    }

    fun setLastUpdateInServiceDate(date: Date) {
        prefs.edit().putLong(prefsLastUpdateInServiceDate, date.time).commit()
    }

    fun setFirstRun() {
        prefs.edit().putBoolean(prefsFirstRun, true).commit()
    }

    fun isFirstRun(): Boolean = prefs.getBoolean(prefsFirstRun, false)

    fun isGoogleCastEnabled(): Boolean = prefs.getBoolean(prefsGoogleCast, false)

    fun setGoogleCastEnabled(e: Boolean) = prefs.edit().putBoolean(prefsGoogleCast, e).commit()

    fun storeVoiceCallAudioVolume(volume: Int)
            = prefs.edit().putInt(prefsAudioVolume, volume).commit()

    fun loadVoiceCallAudioVolume(): Int =
            prefs.getInt(prefsAudioVolume, 100)


    fun setPlayUntilEnd(flag: Boolean)
            = prefs.edit().putBoolean(prefsPlayUntilEnd, flag).commit()

    fun getPlayUntilEnd(): Boolean =
            prefs.getBoolean(prefsPlayUntilEnd, false)

    fun setLoopMusicPlayback(flag: Boolean)
            = prefs.edit().putBoolean(prefsLoopPlayback, flag).commit()

    fun getLoopMusicPlayback(): Boolean =
            prefs.getBoolean(prefsLoopPlayback, false)

    fun storeStreamMusicAudioVolume(volume: Int)
            = prefs.edit().putInt(prefsStreamMusicAudioVolume, volume).commit()

    fun loadStreaMusicAudioVolume(): Int =
            prefs.getInt(prefsStreamMusicAudioVolume, 100)

    fun getLocalMusicDirectory(): String =
            prefs.getString(prefsLocalMusic, "not set yet")!!

    fun setLocalMusicDirectory(value: String) =
            prefs.edit().putString(prefsLocalMusic, value).commit()

    @Deprecated("Dont use shared prefs outside this class anymore")
    fun getPreferences(): SharedPreferences = prefs

    fun getActivePlugin(): String? {
        return prefs.getString(prefsActivePlugin, null)
    }

    fun setActivePlugin(plugin: String) {
        prefs.edit().putString(prefsActivePlugin, plugin).apply()
    }

    fun isAlwaysOnNotificationEnabled() =
            prefs.getBoolean(prefsAlwaysOnNoti, false)

    fun setAlwaysOnNotification(enable: Boolean) =
            prefs.edit().putBoolean(prefsAlwaysOnNoti, enable).commit()

    fun getDelaySeconds(): Int =
            prefs.getInt(prefsDelaySound, 0)

    fun setDelaySeconds(s: Int) =
            prefs.edit().putInt(prefsDelaySound, s).commit()


    fun isAdDetectableEnabled(d: AdDetectable) =
            prefs.getBoolean(prefsAdDetectableMetaPrefix + d.javaClass.canonicalName,
                    d.getMeta().enabledByDef)

    fun saveAdDetectableEnable(enable: Boolean, d: AdDetectable) {
        prefs.edit().putBoolean(prefsAdDetectableMetaPrefix + d.javaClass.canonicalName, enable).apply()
    }

    fun isDeveloperModeEnabled() = prefs.getBoolean(prefsIsDebugDetectors, false)

    fun setDeveloperMode(isDebug: Boolean) =
            prefs.edit().putBoolean(prefsIsDebugDetectors, isDebug).commit()
}