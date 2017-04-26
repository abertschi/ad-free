/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump.view

import android.content.Context
import android.graphics.Typeface
import com.danikula.videocache.HttpProxyCacheServer

/**
 * Created by abertschi on 21.04.17.
 */

class AppSettings private constructor(val context: Context) {

    var typeFace: Typeface = Typeface.createFromAsset(context.assets, "fonts/Raleway-ExtraLight.ttf")

    init {
        httpProxy = HttpProxyCacheServer(context)
    }

    companion object {
        val AD_FREE_RESOURCE_ADRESS: String = "https://github.com/abertschi/ad-free-resources/blob/master/"

        private lateinit var httpProxy: HttpProxyCacheServer

        var _instance: AppSettings? = null

        fun instance(context: Context): AppSettings {
            if (_instance == null) {
                _instance = AppSettings(context)
            }
            return _instance!!
        }
    }

    fun getHttpProxy(): HttpProxyCacheServer = httpProxy
}
