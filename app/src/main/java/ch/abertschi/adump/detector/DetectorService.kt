package ch.abertschi.adump.detector

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.support.v4.app.NotificationCompat
import ch.abertschi.adump.R

/**
 * Created by abertschi on 16.04.17.
 */
class DetectorService(val context: Context) {

    private val notificationId: Int = 1
    private var handler: Handler? = null
    private var musicStreamVolume = 0
    private var isMuted = false

    init {
    }

    val detectors: List<AdDetectable> = listOf<AdDetectable>(SpotifyTitleDetector())

    fun muteAds(payload: AdPayload) {
        var isMusic = false
        var isAd = false

        println("Active detectros: " + detectors.size)

        detectors.forEach {
            if (it.flagAsMusic(payload)) {
                isMusic = true
            }
        }
        if (!isMusic) {
            detectors.forEach {
                if (it.flagAsAdvertisement(payload)) {
                    isAd = true
                }
            }
        }
        if (isAd) {
            print("ad detected")
            muteAudio()
        }
    }

    private fun muteAudio() {
        val ignore = NotificationCompat.Action.Builder(0, "Do not block this again", null).build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context)
                .setContentTitle("Blocking advertisement")
                .setContentText("Press to unblock")
                .setSmallIcon(R.mipmap.icon)
                .addAction(ignore)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

        manager.notify(notificationId, notification)

        if (!isMuted) {
            doMute()
            isMuted = true
        }
        if (Looper.myLooper() == null) {
            Looper.prepare()
        }
        if (handler == null) {
            handler = Handler()
        }
        handler!!.postDelayed({
            manager.cancel(notificationId)
            if (isMuted) {
                isMuted = false
                doUnmute()
            }

        }, 30000)
    }

    fun doMute() {
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        musicStreamVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC)
        println("current volume " + musicStreamVolume)
        am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
    }

    fun doUnmute() {
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.setStreamVolume(AudioManager.STREAM_MUSIC, musicStreamVolume, 0)
    }
}