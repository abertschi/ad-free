/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.view.ViewSettings
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by abertschi on 28.08.17.
 */
open class AudioPlayer(val context: Context, val prefs: PreferencesFactory) {

    private var isPlaying: Boolean = false
    private var onStopCallables: ArrayList<() -> Unit> = ArrayList()
    private var player: MediaPlayer? = null

    /**
     * a callable that is called when a track takes longer to load.
     */
    var trackPreparationDelayCallable: (() -> Unit)? = null

    fun play(url: String) {
        playAudio(url)
    }

    fun playWithCachingProxy(url: String) {
        val proxy = ViewSettings.instance(context).getHttpProxy()
        val proxyUrl = proxy.getProxyUrl(url)
        playAudio(proxyUrl)
    }

    private fun playAudio(url: String) {
        initializeMediaPlayerObservable(context, url).subscribe { player ->
            this.player = player
            player.setOnErrorListener { _, what, _ ->
                throw RuntimeException("Problem with audio player, code: " + what)
            }
            player.start()
            isPlaying = true
        }
    }

    fun requestStop(onStoped: () -> Unit) {
        if (!isPlaying) onStoped()
        else onStopCallables.add(onStoped)
    }

    fun forceStop() {
        closePlayer()
    }

    private fun initializeMediaPlayerObservable(context: Context, url: String): Observable<MediaPlayer>
            = Observable.create<MediaPlayer> { source ->
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.mode = AudioManager.MODE_RINGTONE
        player = MediaPlayer()
        player?.setDataSource(url)
        player?.setAudioStreamType(AudioManager.STREAM_VOICE_CALL)

        var asyncPreparationDone = false
        trackPreparationDelayCallable?.let {
            Observable.just(true)
                    .delay(1000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (!asyncPreparationDone) {
                    trackPreparationDelayCallable?.invoke()
                }
            }
        }
        player?.prepareAsync()
        player?.setOnPreparedListener {
            asyncPreparationDone = true
            am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, loadAudioVolume(), AudioManager.FLAG_SHOW_UI)
            player?.setOnCompletionListener {
                closePlayer()
                synchronized(onStopCallables) {
                    onStopCallables?.forEach { it() }
                    onStopCallables.clear()
                }
            }
            source.onNext(player!!)
        }
    }

    fun closePlayer() {
        isPlaying = false
        player?.stop()
        player?.reset()
        player?.release()
        player = null
    }

    fun storeAudioVolume(volume: Int)
            = prefs.storeAudioVolume(volume)

    fun loadAudioVolume(): Int =
            prefs.loadAudioVolume()
}