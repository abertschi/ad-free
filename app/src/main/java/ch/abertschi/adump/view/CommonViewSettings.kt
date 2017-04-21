package ch.abertschi.adump.view

import android.content.Context
import android.graphics.Typeface

/**
 * Created by abertschi on 21.04.17.
 */

class CommonViewSettings private constructor(val context: Context) {

    var typeFace: Typeface = Typeface.createFromAsset(context.assets, "fonts/Raleway-ExtraLight.ttf")


    companion object {
        var _instance: CommonViewSettings? = null

        fun instance(context: Context): CommonViewSettings {
            if (_instance == null) {
                _instance = CommonViewSettings(context)
            }
            return _instance!!
        }
    }
}
