/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.localmusic

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import ch.abertschi.adfree.R
import ch.abertschi.adfree.view.ViewSettings


/**
 * Created by abertschi on 29.08.17.
 */
class LocalMusicView(val context: Context) {

    fun onCreate(presenter: LocalMusicPlugin): View? {
        val inflater = LayoutInflater.from(context)
        var viewInstance = inflater.inflate(R.layout.plugin_localmusic, null, false)

        val typeFace = ViewSettings.instance(context).typeFace

        var setVolumeView = viewInstance?.findViewById(R.id.plugin_localmusic_audio_volume_text) as TextView
        setVolumeView?.typeface = typeFace
        val t = "configure <font color=#FFFFFF>audio volume</font>"
        setVolumeView?.text = Html.fromHtml(t)

        var chooseDirectoryView = viewInstance?.findViewById(R.id.plugin_localmusic_audio_volume_text) as TextView
        chooseDirectoryView?.typeface = typeFace
        val text = "choose <font color=#FFFFFF>audio directory</font>"
        chooseDirectoryView?.text = Html.fromHtml(text)

        setVolumeView.setOnClickListener { presenter.configureAudioVolume() }
        chooseDirectoryView.setOnClickListener { presenter.chooseDirectory() }
        return viewInstance
    }

    // Environment.getExternalStorageDirectory().absolutePath + "/Music/"
    fun openFileBrowser(baseDirectory: String) {
        val selectedUri = Uri.parse(baseDirectory)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(selectedUri, "resource/folder")

        if (intent.resolveActivityInfo(context.getPackageManager(), 0) != null) {
            context.startActivity(intent)
        } else {
            // if you reach this place, it means there is no any file
            // explorer app installed on your device
        }
    }

}