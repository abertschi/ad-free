/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.localmusic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import ch.abertschi.adfree.R
import ch.abertschi.adfree.plugin.PluginActivityAction
import ch.abertschi.adfree.view.ViewSettings
import com.github.angads25.filepicker.model.DialogConfigs
import com.github.angads25.filepicker.model.DialogProperties
import com.github.angads25.filepicker.view.FilePickerDialog
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.longToast
import org.jetbrains.anko.runOnUiThread
import java.io.File


/**
 * Created by abertschi on 29.08.17.
 */
class LocalMusicView(val context: Context, val action: PluginActivityAction) : AnkoLogger {

    var presenter: LocalMusicPlugin? = null

    fun onCreate(presenter: LocalMusicPlugin): View? {
        this.presenter = presenter
        val inflater = LayoutInflater.from(context)
        var viewInstance = inflater.inflate(R.layout.plugin_localmusic, null, false)

        val typeFace = ViewSettings.instance(context).typeFace

        var setVolumeView = viewInstance?.findViewById(R.id.plugin_localmusic_audio_volume_text) as TextView
        setVolumeView?.typeface = typeFace
        val t = "> configure <font color=#FFFFFF>audio volume</font>"
        setVolumeView?.text = Html.fromHtml(t)

        var chooseDirectoryView = viewInstance?.
                findViewById(R.id.plugin_localmusic_choose_audio_directory_text) as TextView
        chooseDirectoryView?.typeface = typeFace
        val text = "> choose <font color=#FFFFFF>audio directory</font>"
        chooseDirectoryView?.text = Html.fromHtml(text)

        setVolumeView.setOnClickListener { presenter.configureAudioVolume() }
        chooseDirectoryView.setOnClickListener {
            presenter.chooseDirectory()
        }

        action.addOnActivityResult({ requestCode, resultCode, data ->
            presenter.onActivityResult(requestCode, resultCode, data)
        })

        return viewInstance
    }

    fun showFolderSelectionDialog(default: File) {
        val properties = DialogProperties()
        properties.selection_mode = DialogConfigs.SINGLE_MODE
        properties.selection_type = DialogConfigs.DIR_SELECT
        properties.root = default
        properties.error_dir = File(DialogConfigs.DEFAULT_DIR)
        properties.offset = File(DialogConfigs.DEFAULT_DIR)
        properties.extensions = null

        val dialog = FilePickerDialog(context, properties)
        dialog.setTitle("Select a Folder")

        dialog.setDialogSelectionListener { files ->
            val f = mutableListOf<String>()
            files?.forEach { f.add(it) }
            presenter!!.onDirectorySelected(f)
        }
        dialog.show()

    }

    fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        action.startActivityForResult(intent, requestCode, options)
    }

    fun showAudioError() {
        context.applicationContext.runOnUiThread {
            longToast("Whooops, there was an error with audio")
        }
    }
}