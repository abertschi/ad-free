/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.model

import com.github.kittinunf.fuel.httpGet

/**
 * Created by abertschi on 26.04.17.
 */

class YamlRemoteConfigFactory<MODEL>(val downloadUrl: String, val modelType: Class<MODEL>, val preferences: ch.abertschi.adfree.model.PreferencesFactory) {

    private val SETTING_PERSISTENCE_LOCAL_KEY: String = "YAML_CONFIG_FACTORY_PERSISTENCE_"

    init {
        SETTING_PERSISTENCE_LOCAL_KEY + modelType.canonicalName
    }

    fun downloadObservable(): io.reactivex.Observable<Pair<MODEL, String>>
            = io.reactivex.Observable.create<Pair<MODEL, String>> { source ->
        downloadUrl.httpGet().responseString { _, _, result ->
            val (data, error) = result
            if (error == null) {
                try {
                    val yaml = createYamlInstance()
                    val model = yaml.loadAs(data, modelType)
                    source.onNext(Pair<MODEL, String>(model, data ?: ""))
                } catch (exception: Exception) {
                    source.onError(exception)
                }
            } else {
                source.onError(error)
            }
            source.onComplete()
        }
    }.observeOn(io.reactivex.schedulers.Schedulers.io()).observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())

    fun loadFromLocalStore(defaultReturn: MODEL? = null): MODEL? {
        val yaml = createYamlInstance()
        val content = preferences.getPreferences().getString(SETTING_PERSISTENCE_LOCAL_KEY, null)
        if (content == null) {
            return defaultReturn
        } else {
            return yaml.loadAs(content, modelType)
        }
    }

    fun storeToLocalStore(model: MODEL) {
        val yaml = createYamlInstance()
        preferences.getPreferences().edit().putString(SETTING_PERSISTENCE_LOCAL_KEY, yaml.dump(model)).commit()
    }

    private fun createYamlInstance(): org.yaml.snakeyaml.Yaml {
        val representer = org.yaml.snakeyaml.representer.Representer()
        representer.propertyUtils.setSkipMissingProperties(true)
        return org.yaml.snakeyaml.Yaml(representer)
    }
}