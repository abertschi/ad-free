/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump.setting

import android.content.Context
import ch.abertschi.adump.model.PreferencesFactory
import com.github.kittinunf.fuel.httpGet
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.representer.Representer

/**
 * Created by abertschi on 26.04.17.
 */

class YamlConfigFactory<MODEL>(val downloadUrl: String, val modelType: Class<MODEL>, val context: Context) {

    private val SETTING_PERSISTENCE_LOCAL_KEY: String = "YAML_CONFIG_FACTORY_PERSISTENCE_"

    init {
        SETTING_PERSISTENCE_LOCAL_KEY + modelType.canonicalName
    }

    fun downloadObservable(): Observable<Pair<MODEL, String>>
            = Observable.create<Pair<MODEL, String>> { source ->
        downloadUrl.httpGet().responseString { _, _, result ->
            val (data, error) = result
            if (error == null) {
                try {
                    val yaml = createYamlInstance()
                    val model = yaml.loadAs(data, modelType)
                    source.onNext(Pair<MODEL, String>(model, yaml.dump(model)))
                } catch (exception: Exception) {
                    source.onError(exception)
                }
            } else {
                source.onError(error)
            }
            source.onComplete()
        }
    }.observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    fun loadFromLocalStore(): MODEL? {
        val prefs = PreferencesFactory.providePrefernecesFactory(context)
        val yaml = createYamlInstance()
        val content = prefs.getPreferences().getString(SETTING_PERSISTENCE_LOCAL_KEY, null)
        if (content == null) {
            return null
        } else {
            return yaml.loadAs(content, modelType)
        }
    }

    fun storeToLocalStore(model: MODEL) {
        val prefs = PreferencesFactory.providePrefernecesFactory(context)
        val yaml = createYamlInstance()
        prefs.getPreferences().edit().putString(SETTING_PERSISTENCE_LOCAL_KEY, yaml.dump(model)).commit()
    }

    private fun createYamlInstance(): Yaml {
        val representer = Representer()
        representer.propertyUtils.setSkipMissingProperties(true)
        return Yaml(representer)
    }
}