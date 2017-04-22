package ch.abertschi.adump.plugin.interdimcable

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import ch.abertschi.adump.model.PreferencesFactory
import ch.abertschi.adump.plugin.AdPlugin
import ch.abertschi.adump.plugin.PluginContet
import ch.abertschi.adump.view.AppSettings
import com.github.kittinunf.fuel.httpGet
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.representer.Representer


/**
 * Created by abertschi on 21.04.17.
 */
class InterdimCablePlugin : AdPlugin, AnkoLogger {

    private val PLUGIN_PERSISTED_LOCALLY_KEY: String = "INTERDIM_CABLE_PLUGIN"
    private val RAW_SUFFIX: String = "?raw=true"
    private val BASE_URL: String = AppSettings.AD_FREE_RESOURCE_ADRESS + "plugins/interdimensional-cable/"
    private val PLUGIN_FILE_PATH: String = BASE_URL + "plugin.yaml" + RAW_SUFFIX
    private var mModel: InterdimCableModel? = null
    private var mPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false
    private var onStopCallables: ArrayList<() -> Unit> = ArrayList()

    override fun title(): String = "interdimensional cable"

    override fun onPluginActivated(context: PluginContet) {
        context.applicationContext.toast("It's time to get schwifty")
        mModel = loadPluginSettingsFromLocalStorage(context.applicationContext)
        updatePluginSettings(context.applicationContext)
    }

    private fun updatePluginSettings(context: Context) {
        getPluginObservable().subscribe {
            info("Interdimensional cable plugin settings updated")
            mModel = it.first
            storePluginSettingsInLocalStorage(context.applicationContext, it.first)
        }
    }

    override fun onPluginDeactivated(context: PluginContet) {
    }

    override fun play(context: PluginContet) {
        if (isPlaying) return
        updatePluginSettings(context.applicationContext)
        if (mModel == null) {
            return
        }
        if (mModel?.channels?.size!! > 0) {
            val url = BASE_URL + mModel!!.channels!![(Math.random() * mModel!!.channels!!.size).toInt()].path + RAW_SUFFIX
            playAudio(url, context.applicationContext)
        } else {
            context.applicationContext.longToast("No channels to play. You can not listen to interdimensional tv :(")
            error("No channels to play. You can not watch interdimensional tv")
        }
    }

    override fun playTrial(context: PluginContet) {
        play(context)
    }
    
    private fun playAudio(url: String, context: Context) {
        // TODO: add feature to download everyting at once when of WIFI
        val proxy = AppSettings.instance(context).getHttpProxy()
        val proxyUrl = proxy.getProxyUrl(url)

        val am = context.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.mode = AudioManager.MODE_NORMAL
        am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, am.getStreamVolume(AudioManager.STREAM_MUSIC), 0)
        mPlayer = MediaPlayer()
        mPlayer?.setDataSource(proxyUrl)
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
                    val yaml = createYamlInstance() as Yaml
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

    private fun loadPluginSettingsFromLocalStorage(context: Context): InterdimCableModel? {
        val prefs = PreferencesFactory.providePrefernecesFactory(context)
        val yaml = createYamlInstance()
        val content = prefs.getPreferences().getString(PLUGIN_PERSISTED_LOCALLY_KEY, null)
        if (content == null) {
            return null
        } else {
            return yaml.loadAs(content, InterdimCableModel::class.java)
        }
    }

    private fun storePluginSettingsInLocalStorage(context: Context, model: InterdimCableModel) {
        val prefs = PreferencesFactory.providePrefernecesFactory(context)
        val yaml = createYamlInstance()
        prefs.getPreferences().edit().putString(PLUGIN_PERSISTED_LOCALLY_KEY, yaml.dump(model)).commit()
    }

    private fun createYamlInstance(): Yaml {
        val representer = Representer()
        representer.propertyUtils.setSkipMissingProperties(true)
        val yaml: Yaml = Yaml(representer)
        return yaml
    }
}