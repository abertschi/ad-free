package ch.abertschi.adump.plugin.interdimcable

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import ch.abertschi.adump.plugin.AdPlugin
import ch.abertschi.adump.plugin.PluginContet
import ch.abertschi.adump.view.AppSettings
import com.github.kittinunf.fuel.httpGet
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.representer.Representer


/**
 * Created by abertschi on 21.04.17.
 */
class InterdimCablePlugin : AdPlugin {

    private val RAW_SUFFIX: String = "?raw=true"
    private val BASE_URL: String = AppSettings.AD_FREE_RESOURCE_ADRESS + "plugins/interdimensional-cable/"
    private val PLUGIN_FILE_PATH: String = BASE_URL + "plugin.yaml" + RAW_SUFFIX
    private var mModel: InterdimCableModel? = null

    private var mPlayer: MediaPlayer? = null

    var isPlaying: Boolean = false
    private var onStopCallables: ArrayList<() -> Unit> = ArrayList()

    override fun title(): String = "interdimensional cable"

    override fun onPluginActivated(context: PluginContet) {
        context.applicationContext.toast("It's time to get schwifty")

        val observable = getPluginObservable()
        observable.subscribe {
            println("RUNNING")
            mModel = it.first
            println(it.second)
//            play(context)
        }
    }

    override fun onPluginDeactivated(context: PluginContet) {
    }

    override fun play(context: PluginContet) {
        if (isPlaying) return
        if (mModel == null) {
            onPluginActivated(context)
            return
        }

        val url = BASE_URL + mModel!!.channels!![(Math.random() * mModel!!.channels!!.size).toInt()].path + RAW_SUFFIX
        println(url)

        val am = context.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.mode = AudioManager.MODE_NORMAL
        am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, am.getStreamVolume(AudioManager.STREAM_MUSIC), 0)
        mPlayer = MediaPlayer()
        mPlayer?.setDataSource(url)
        mPlayer?.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        mPlayer?.prepare()
        mPlayer?.start()
        isPlaying = true
        mPlayer?.setOnCompletionListener {
            closePlayer()
            synchronized(onStopCallables) {
                onStopCallables?.forEach { it() }
                onStopCallables.clear()
            }
        }
    }

    override fun requestStop(contet: PluginContet, onStoped: () -> Unit) {
        if (!isPlaying) onStoped()
        else onStopCallables.add(onStoped)
    }

    override fun forceStop(context: PluginContet) {
        closePlayer()
    }

    private fun closePlayer() {
        isPlaying = false
        mPlayer?.stop()
        mPlayer?.release()
        mPlayer = null
    }

    private fun getPluginObservable(): Observable<Pair<InterdimCableModel, String>>
            = Observable.create<Pair<InterdimCableModel, String>> { source ->

        PLUGIN_FILE_PATH.httpGet().responseString { _, _, result ->
            val (data, error) = result
            if (error == null) {
                try {
                    val representer = Representer()
                    representer.propertyUtils.setSkipMissingProperties(true)
                    val yaml: Yaml = Yaml(representer)
                    val model = yaml.loadAs(data, InterdimCableModel::class.java)
                    source.onNext(Pair<InterdimCableModel, String>(model, yaml.dump(model)))
                } catch (exception: Exception) {
                    source.onError(exception)
                }
            } else {
                source.onError(error)
            }
            source.onComplete()
        }
    }.observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}