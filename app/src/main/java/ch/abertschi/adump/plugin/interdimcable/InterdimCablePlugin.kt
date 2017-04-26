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
import ch.abertschi.adump.plugin.AdPlugin
import ch.abertschi.adump.plugin.PluginContet
import ch.abertschi.adump.setting.YamlConfigFactory
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

    private val PLUGIN_PERSISTED_LOCALLY_KEY: String = "INTERDIM_CABLE_PLUGIN"
    private val PLUGIN_STORED_AUDIO_KEY: String = "INTERDIM_CABLE_PLUGIN_AUDIO"

    private val RAW_SUFFIX: String = "?raw=true"
    private val BASE_URL: String = AppSettings.AD_FREE_RESOURCE_ADRESS + "plugins/interdimensional-cable/"
    private val PLUGIN_FILE_PATH: String = BASE_URL + "plugin.yaml" + RAW_SUFFIX

    private lateinit var configFactory: YamlConfigFactory<InterdimCableModel>
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
        configFactory = YamlConfigFactory(PLUGIN_FILE_PATH, InterdimCableModel::class.java, context.applicationContext)
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
                    context.applicationContext.longToast("Can not load interdimensional cable commercials. Did you check your internet?")
                }
        )
    }

    override fun play(context: PluginContet) {
        if (model == null) {
            updatePluginSettings(context.applicationContext)
            return
        }
        if (model?.channels?.size!! > 0) {
            val url = BASE_URL + model!!.channels!![(Math.random() * model!!.channels!!.size).toInt()].path + RAW_SUFFIX
            playAudio(url, context.applicationContext)
        } else {
            context.applicationContext.longToast("No channels to play. You can not listen to interdimensional tv :(")
            error("No channels to play. You can not watch interdimensional tv")
        }
    }

    override fun playTrial(context: PluginContet) {
        play(context)
        context.applicationContext.runOnUiThread {
            longToast("It's time to get schwifty")
        }
    }

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
        // TODO: add feature to download everyting at once when of WIFI
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

//    private fun getPluginObservable(): Observable<Pair<InterdimCableModel, String>>
//            = Observable.create<Pair<InterdimCableModel, String>> { source ->
//
//        PLUGIN_FILE_PATH.httpGet().responseString { _, _, result ->
//            val (data, error) = result
//            if (error == null) {
//                try {
//                    val yaml = createYamlInstance()
//                    val model = yaml.loadAs(data, InterdimCableModel::class.java)
//                    source.onNext(Pair<InterdimCableModel, String>(model, yaml.dump(model)))
//                } catch (exception: Exception) {
//                    source.onError(exception)
//                }
//            } else {
//                source.onError(error)
//            }
//            source.onComplete()
//        }
//    }.observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

//    private fun loadPluginSettingsFromLocalStorage(context: Context): InterdimCableModel? {
//        val prefs = PreferencesFactory.providePrefernecesFactory(context)
//        val yaml = createYamlInstance()
//        val content = prefs.getPreferences().getString(PLUGIN_PERSISTED_LOCALLY_KEY, null)
//        if (content == null) {
//            return null
//        } else {
//            return yaml.loadAs(content, InterdimCableModel::class.java)
//        }
//    }
//
//    private fun storePluginSettingsInLocalStorage(context: Context, model: InterdimCableModel) {
//        val prefs = PreferencesFactory.providePrefernecesFactory(context)
//        val yaml = createYamlInstance()
//        prefs.getPreferences().edit().putString(PLUGIN_PERSISTED_LOCALLY_KEY, yaml.dump(model)).commit()
//    }

//    private fun createYamlInstance(): Yaml {
//        val representer = Representer()
//        representer.propertyUtils.setSkipMissingProperties(true)
//        val yaml: Yaml = Yaml(representer)
//        return yaml
//    }

    private fun storeAudioVolume(volume: Int, context: Context)
            = PreferencesFactory.providePrefernecesFactory(context).getPreferences().edit().putInt(PLUGIN_STORED_AUDIO_KEY, volume).commit()

    private fun loadAudioVolume(context: Context): Int =
            PreferencesFactory.providePrefernecesFactory(context).getPreferences().getInt(PLUGIN_STORED_AUDIO_KEY, 100)
}