package ch.abertschi.adump

import android.content.Context
import android.media.AudioManager
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info

/**
 * Created by abertschi on 16.04.17.
 */
class MuteManager private constructor() : AnkoLogger {

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
        debug("current volume " + musicStreamVolume)
        info("muting audio")
//        am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
//        am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE);
//        am.adjustVolume(AudioManager.ADJUST_LOWER,
//                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
        am.setStreamMute(AudioManager.STREAM_MUSIC, true)
    }


    fun doUnmute(context: Context) {
        if (!audioIsMuted) {
            return
        }
        info("Unmuting audio....")
        audioIsMuted = false
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//        am.setStreamVolume(AudioManager.STREAM_MUSIC, musicStreamVolume, 0)
        am.setStreamMute(AudioManager.STREAM_MUSIC, false)
//        am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE);
    }

    fun isAudioMuted(): Boolean = audioIsMuted
}
