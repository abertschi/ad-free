/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree

import android.content.Context
import android.media.AudioManager
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info

/**
 * Created by abertschi on 16.04.17.
 */
class AudioController(val context: Context) : AnkoLogger {

    private var musicStreamVolume = 0
    private var musicStreamIsMuted = false

    fun isMusicStreamMuted(): Boolean = musicStreamIsMuted

    fun muteMusicStream() {
        debug("current volume " + musicStreamVolume)
        info("muting audio")

        if (musicStreamIsMuted) {
            return
        }
        musicStreamIsMuted = true
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        musicStreamVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC)
        
        am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
    }

    fun unmuteMusicStream() {
        info("Unmuting audio....")
        if (!musicStreamIsMuted) {
            return
        }
        musicStreamIsMuted = false
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0)
    }
}

