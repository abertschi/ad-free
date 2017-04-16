package ch.abertschi.adump

import android.content.Context
import android.media.AudioManager
import android.os.Handler

/**
 * Created by abertschi on 16.04.17.
 */
class MuteManager private constructor() {

    private object Holder {
        val INSTANCE = MuteManager()
    }

    companion object {
        val instance: MuteManager by lazy { Holder.INSTANCE }
    }

    private var musicStreamVolume = 0
    private var audioIsMuted = false

    fun doMute(context: Context) {
        if (audioIsMuted) {
            return
        }

        audioIsMuted = true
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        musicStreamVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC)
        println("current volume " + musicStreamVolume)
        am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
    }

    fun doUnmute(context: Context) {
        if (!audioIsMuted) {
            return
        }
        print("Unmuting audio....")
        audioIsMuted = false
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.setStreamVolume(AudioManager.STREAM_MUSIC, musicStreamVolume, 0)
    }

    fun isAudioMuted(): Boolean = audioIsMuted
}
