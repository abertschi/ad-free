/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.interdimcable

import android.content.Context
import android.media.AudioManager
import android.view.View
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.model.YamlRemoteConfigFactory
import ch.abertschi.adfree.plugin.AdPlugin
import ch.abertschi.adfree.plugin.AudioPlayer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import java.util.concurrent.TimeUnit

/**
 * Created by abertschi on 21.04.17.
 */
class InterdimCablePlugin(val prefs: PreferencesFactory, val context: Context)
    : AdPlugin, AnkoLogger {

    private val GITHUB_RAW_SUFFIX: String = "?raw=true"
    private val AD_FREE_RESOURCE_ADRESS: String
            = "https://github.com/abertschi/ad-free-resources/blob/master/"

    private val BASE_URL: String = AD_FREE_RESOURCE_ADRESS + "plugins/interdimensional-cable/"
    private val PLUGIN_FILE_PATH: String = BASE_URL + "plugin.yaml" + GITHUB_RAW_SUFFIX

    private var configFactory: YamlRemoteConfigFactory<InterdimCableModel> =
            YamlRemoteConfigFactory(PLUGIN_FILE_PATH, InterdimCableModel::class.java, prefs)

    private var model: InterdimCableModel? = null
    private var interdimCableView: InterdimCableView? = null

    private var player: AudioPlayer = AudioPlayer(context, prefs)

    override fun title(): String = "interdimensional cable"

    override fun hasSettingsView(): Boolean = true

    override fun settingsView(context: Context): View? {
        if (interdimCableView == null) {
            interdimCableView = InterdimCableView(context)
            player.trackPreparationDelayCallable = {
                interdimCableView?.showDownloadingTrack()
            }
        }
        return interdimCableView?.onCreate(this)
    }

    override fun onPluginActivated() {
        model = configFactory.loadFromLocalStore()
        updatePluginSettings()
    }

    override fun onPluginDeactivated() {}

    private fun updatePluginSettings() {
        configFactory.downloadObservable()
                .subscribe(
                        { pair ->
                            model = pair.first
                            info("Interdimensional cable plugin settings updated")
                            info("downloaded meta data for " + model?.channels?.size + " channels")
                            configFactory.storeToLocalStore(model!!)
                        },
                        { error ->
                            interdimCableView?.showInternetError()
                        }
                )
    }

    override fun play() {
        if (model == null) {
            updatePluginSettings()
            return
        }
        if (model?.channels?.size!! > 0) {
            val url = BASE_URL + model!!.channels!![(Math.random() *
                    model!!.channels!!.size).toInt()].path + GITHUB_RAW_SUFFIX
            runAndCatchException({
                player.playWithCachingProxy(url)
            })
        } else {
            interdimCableView?.showNoChannelsError()
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

    override fun requestStop(onStoped: () -> Unit) {
        runAndCatchException({ player.requestStop(onStoped) })
    }

    override fun forceStop() {
        runAndCatchException({ player.forceStop() })
    }

    private fun runAndCatchException(function: () -> Unit): Unit {
        try {
            function()
        } catch (e: Throwable) {
            interdimCableView?.showAudioError()
            error(e)
        }
    }

    private fun storeAudioVolume(volume: Int)
            = prefs.storeAudioVolume(volume)

    private fun loadAudioVolume(): Int =
            prefs.loadAudioVolume()
}