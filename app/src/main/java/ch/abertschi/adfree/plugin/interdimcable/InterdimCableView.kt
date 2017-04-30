/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.interdimcable

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import ch.abertschi.adfree.R
import ch.abertschi.adfree.view.AppSettings

/**
 * Created by abertschi on 22.04.17.
 */

class InterdimCableView(val context: Context) {

    private var viewInstance: View? = null

    fun onCreate(presenter: InterdimCablePlugin): View? {
        val inflater = LayoutInflater.from(context)
        viewInstance = inflater.inflate(R.layout.plugin_interdim_cable, null, false)

        var text = viewInstance?.findViewById(R.id.plugin_interdim_cable_audio_volume_text) as TextView
        text?.typeface = AppSettings.instance(context).typeFace
        val t = "configure <font color=#FFFFFF>audio volume</font>"
        text?.text = Html.fromHtml(t)
        text.setOnClickListener {
            presenter.configureAudioVolume(context)
        }

        return viewInstance
    }

}
