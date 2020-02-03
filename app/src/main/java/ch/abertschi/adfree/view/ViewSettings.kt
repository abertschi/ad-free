/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.view

import android.content.Context
import android.graphics.Typeface


/**
 * Created by abertschi on 21.04.17.
 */

class ViewSettings private constructor(val context: Context) {

    var typeFace: Typeface = Typeface.createFromAsset(context.assets, "fonts/Raleway-ExtraLight.ttf")


    companion object {
        val AD_FREE_RESOURCE_ADRESS: String
                = "https://github.com/abertschi/ad-free-resources/blob/master/"

        val GITHUB_RAW_SUFFIX: String = "?raw=true"

        var _instance: ViewSettings? = null

        fun instance(context: Context): ViewSettings {
            if (_instance == null) {
                _instance = ViewSettings(context)
            }
            return _instance!!
        }
    }
}
