/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.interdimcable

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.View
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.model.YamlRemoteConfigFactory
import ch.abertschi.adfree.plugin.AdPlugin
import ch.abertschi.adfree.view.ViewSettings
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import java.util.concurrent.TimeUnit

/**
 * Created by abertschi on 21.04.17.
 */
class InterdimCablePlugin(val prefs: PreferencesFactory, val context: Context)
    : AdPlugin, AnkoLogger {

    val AD_FREE_RESOURCE_ADRESS: String
            = "https://github.com/abertschi/ad-free-resources/blob/master/"

    val GITHUB_RAW_SUFFIX: String = "?raw=true"

    private val BASE_URL: String = AD_FREE_RESOURCE_ADRESS + "plugins/interdimensional-cable/"
    private val PLUGIN_FILE_PATH: String = BASE_URL + "plugin.yaml" + GITHUB_RAW_SUFFIX

    lateinit var configFactory: YamlRemoteConfigFactory<InterdimCableModel>

    private var model: InterdimCableModel? = null
    private var player: MediaPlayer? = null
    private var isPlaying: Boolean = false
    private var onStopCallables: ArrayList<() -> Unit> = ArrayList()
    private var interdimCableView: InterdimCableView? = null

    override fun title(): String = "interdimensional cable"
    override fun hasSettingsView(): Boolean = true

    override fun settingsView(context: Context): View? {
        if (interdimCableView == null) {
            interdimCableView = InterdimCableView(context)
        }
        return interdimCableView?.onCreate(this)
    }

    override fun onPluginActivated() {
        configFactory = YamlRemoteConfigFactory(PLUGIN_FILE_PATH,
                InterdimCableModel::class.java,
                prefs)
        model = configFactory.loadFromLocalStore()
        updatePluginSettings(context.applicationContext)
    }

    override fun onPluginDeactivated() {}

    private fun updatePluginSettings(context: Context) {
        configFactory.downloadObservable().subscribe(
                { pair ->
                    model = pair.first
                    info("Interdimensional cable plugin settings updated")
                    info("downloaded meta data for " + model?.channels?.size + " channels")
                    configFactory.storeToLocalStore(model!!)
                },
                { error ->
                    context.applicationContext.longToast(
                            "Can not load interdimensional cable commercials. Did you check your internet?")
                }
        )
    }

    override fun play() {
        if (model == null) {
            updatePluginSettings(context.applicationContext)
            return
        }
        if (model?.channels?.size!! > 0) {
            val url = BASE_URL +
                    model!!.channels!![(Math.random() *
                            model!!.channels!!.size).toInt()].path + GITHUB_RAW_SUFFIX
            playAudio(url, context.applicationContext)
        } else {
            context.applicationContext
                    .longToast("No channels to play. You can not listen to interdimensional tv :(")
            error("No channels to play. You can not watch interdimensional tv")
        }
    }

    override fun playTrial() = play()

    fun configureAudioVolume(context: Context) {
        val am = context.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, loadAudioVolume(), AudioManager.FLAG_SHOW_UI)
        Observable.just(true).delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
            val volume = am.getStreamVolume(AudioManager.STREAM_VOICE_CALL)

            info("Storing audio volume with value " + volume)
            storeAudioVolume(volume)
        }
    }

    private fun playAudio(url: String, context: Context) {
        runAndCatchException({
            val proxy = ViewSettings.instance(context).getHttpProxy()
            val proxyUrl = proxy.getProxyUrl(url)
            initializeMediaPlayerObservable(context, proxyUrl).subscribe { player ->
                this.player = player
                player.setOnErrorListener { _, what, _ -> throw RuntimeException("Problem with audio player, code: " + what) }
                player.start()
                isPlaying = true
            }
        })
    }

    override fun requestStop(onStoped: () -> Unit) {
        if (!isPlaying) onStoped()
        else onStopCallables.add(onStoped)
    }

    override fun forceStop() {
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
        player?.prepareAsync()
        Observable.just(true)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (!asyncPreparationDone) {
                context.runOnUiThread {
                    longToast("Downloading interdimensional cable ads ...")
                }
            }
        }
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
            source.onNext(player)
        }
    }

    private fun runAndCatchException(function: () -> Unit): Unit {
        try {
            function()
        } catch(e: Throwable) {
            context.applicationContext.runOnUiThread {
                longToast("Whooops, there was an error with audio")
                error(e)
            }
        }
    }

    private fun closePlayer() {
        runAndCatchException({
            isPlaying = false
            player?.stop()
            player?.release()
            player = null
        })
    }

    private fun storeAudioVolume(volume: Int)
            = prefs.storeAudioVolume(volume)

    private fun loadAudioVolume(): Int =
            prefs.loadAudioVolume()
}