/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump

import android.content.Context
import android.media.AudioManager
import ch.abertschi.adump.plugin.PluginContet
import ch.abertschi.adump.plugin.PluginHandler
import ch.abertschi.adump.util.NotificationUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info
import java.util.concurrent.TimeUnit

/**
 * Created by abertschi on 16.04.17.
 */
class AudioController private constructor() : AnkoLogger {

    private object Holder {
        val INSTANCE = AudioController()
    }

    companion object {
        val instance: ch.abertschi.adump.AudioController by lazy { Holder.INSTANCE }
    }

    private var musicStreamVolume = 0
    private var musicStreamIsMuted = false
    private var mPluginHandler: PluginHandler = PluginHandler.instance
    private var mNotificationUtils: NotificationUtils = NotificationUtils()

    fun isMusicStreamMuted(): Boolean = musicStreamIsMuted

    fun muteMusicStream(context: Context) {
        if (musicStreamIsMuted) {
            return
        }
        musicStreamIsMuted = true
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        musicStreamVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC)
        debug("current volume " + musicStreamVolume)
        info("muting audio")
        // TODO: Dont use dep. API to mute audio
//        am.setStreamVolume(AudioController.STREAM_MUSIC, 0, 0)
//        am.adjustStreamVolume(AudioController.STREAM_MUSIC, AudioController.STREAM_MUSIC, AudioController.ADJUST_MUTE);
//        am.adjustVolume(AudioController.ADJUST_LOWER,
//                AudioController.FLAG_REMOVE_SOUND_AND_VIBRATE)
        am.setStreamMute(AudioManager.STREAM_MUSIC, true)
    }

    fun unmuteMusicStream(context: Context) {
        if (!musicStreamIsMuted) {
            return
        }
        info("Unmuting audio....")
        musicStreamIsMuted = false
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.setStreamMute(AudioManager.STREAM_MUSIC, false)
        // TODO: Dont use dep. API to mute audio
//        am.setStreamVolume(AudioController.STREAM_MUSIC, musicStreamVolume, 0)
//        am.adjustStreamVolume(AudioController.STREAM_MUSIC, AudioController.STREAM_MUSIC, AudioController.ADJUST_UNMUTE);
    }

    fun muteMusicAndRunActivePlugin(context: Context, showNotification: Boolean = true, delayUntilUnmuteInMillis: Long = 30000) {
        val pluginContext = PluginContet(context)
        muteMusicStream(context)
        mPluginHandler.runPlugin(pluginContext)
        if (showNotification) {
            mNotificationUtils.showBlockingNotification(context, dismissCallable = {
                unmuteMusicAndStopActivePlugin(context)
            })
        }
        getDelayedObservable(delayUntilUnmuteInMillis).map {
            mPluginHandler.requestPluginStop(pluginContext, {
                unmuteMusicAndStopActivePlugin(context)
            })
        }.subscribe()
    }

    fun unmuteMusicAndStopActivePlugin(context: Context) {
        mNotificationUtils.hideBlockingNotification(context)
        unmuteMusicStream(context)
        mPluginHandler.stopPlugin(PluginContet(context))
    }

    private fun getDelayedObservable(millis: Long) =
            Observable.just(true).delay(millis, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

}

