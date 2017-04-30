/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump.plugin.interdimcable

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.View
import ch.abertschi.adump.model.PreferencesFactory
import ch.abertschi.adump.model.YamlRemoteConfigFactory
import ch.abertschi.adump.plugin.AdPlugin
import ch.abertschi.adump.plugin.PluginContet
import ch.abertschi.adump.view.AppSettings
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import java.util.concurrent.TimeUnit

/**
 * Created by abertschi on 21.04.17.
 */
class InterdimCablePlugin : AdPlugin, AnkoLogger {
    private val PLUGIN_STORED_AUDIO_KEY: String = "INTERDIM_CABLE_PLUGIN_AUDIO"

    private val BASE_URL: String = AppSettings.AD_FREE_RESOURCE_ADRESS + "plugins/interdimensional-cable/"
    private val PLUGIN_FILE_PATH: String = BASE_URL + "plugin.yaml" + AppSettings.GITHUB_RAW_SUFFIX

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

    override fun onPluginActivated(context: PluginContet) {
        configFactory = YamlRemoteConfigFactory(PLUGIN_FILE_PATH, InterdimCableModel::class.java, PreferencesFactory.providePrefernecesFactory(context.applicationContext))
        model = configFactory.loadFromLocalStore()
        updatePluginSettings(context.applicationContext)
    }

    override fun onPluginDeactivated(context: PluginContet) {}

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

    override fun play(context: PluginContet) {
        if (model == null) {
            updatePluginSettings(context.applicationContext)
            return
        }
        if (model?.channels?.size!! > 0) {
            val url = BASE_URL + model!!.channels!![(Math.random() * model!!.channels!!.size).toInt()].path + AppSettings.GITHUB_RAW_SUFFIX
            playAudio(url, context.applicationContext)
        } else {
            context.applicationContext.longToast("No channels to play. You can not listen to interdimensional tv :(")
            error("No channels to play. You can not watch interdimensional tv")
        }
    }

    override fun playTrial(context: PluginContet) = play(context)

    fun configureAudioVolume(context: Context) {
        val am = context.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, loadAudioVolume(context), AudioManager.FLAG_SHOW_UI)
        Observable.just(true).delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
            val volume = am.getStreamVolume(AudioManager.STREAM_VOICE_CALL)

            info("Storing audio volume with value " + volume)
            storeAudioVolume(volume, context)
        }
    }

    private fun playAudio(url: String, context: Context) {
        runAndCatchException(context, {
            val proxy = AppSettings.instance(context).getHttpProxy()
            val proxyUrl = proxy.getProxyUrl(url)
            initializeMediaPlayerObservable(context, proxyUrl).subscribe { player ->
                this.player = player
                player.setOnErrorListener { _, what, _ -> throw RuntimeException("Problem with audio player, code: " + what) }
                player.start()
                isPlaying = true
            }
        })
    }

    override fun requestStop(contet: PluginContet, onStoped: () -> Unit) {
        if (!isPlaying) onStoped()
        else onStopCallables.add(onStoped)
    }

    override fun forceStop(context: PluginContet) {
        closePlayer(context.applicationContext)
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
            am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, loadAudioVolume(context), AudioManager.FLAG_SHOW_UI)
            player?.setOnCompletionListener {
                closePlayer(context)
                synchronized(onStopCallables) {
                    onStopCallables?.forEach { it() }
                    onStopCallables.clear()
                }
            }
            source.onNext(player)
        }
    }

    private fun runAndCatchException(context: Context, function: () -> Unit): Unit {
        try {
            function()
        } catch(e: Throwable) {
            context.applicationContext.runOnUiThread {
                longToast("Whooops, there was an error with audio")
                error(e)
            }
        }
    }

    private fun closePlayer(context: Context) {
        runAndCatchException(context, {
            isPlaying = false
            player?.stop()
            player?.release()
            player = null
        })
    }

    private fun storeAudioVolume(volume: Int, context: Context)
            = PreferencesFactory.providePrefernecesFactory(context).getPreferences().edit().putInt(PLUGIN_STORED_AUDIO_KEY, volume).commit()

    private fun loadAudioVolume(context: Context): Int =
            PreferencesFactory.providePrefernecesFactory(context).getPreferences().getInt(PLUGIN_STORED_AUDIO_KEY, 100)
}