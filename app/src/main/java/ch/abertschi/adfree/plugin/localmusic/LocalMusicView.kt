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



import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.longToast
import org.jetbrains.anko.runOnUiThread
import java.io.File
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.content.DialogInterface

import android.support.v7.app.AlertDialog


/**
 * Created by abertschi on 29.08.17.
 */
class LocalMusicView(val context: Context, val action: PluginActivityAction) : AnkoLogger {

    private lateinit var audioDirDialog: AlertDialog

    var presenter: LocalMusicPlugin? = null

    private lateinit var playUntilEndAnswer: TextView

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

        var playUntilEnd = viewInstance?.
                findViewById(R.id.plugin_localmusic_play_till_end) as TextView
        playUntilEnd?.typeface = typeFace

        val playUntilEndText = "> play until end "
        playUntilEnd?.text = Html.fromHtml(playUntilEndText)

        playUntilEndAnswer = viewInstance?.
                findViewById(R.id.plugin_localmusic_play_till_end_answer) as TextView

        playUntilEndAnswer?.typeface = typeFace
        playUntilEndAnswer.setOnClickListener { presenter.changePlayUntilEndFlag() }
        playUntilEnd.setOnClickListener { presenter.changePlayUntilEndFlag() }

        setVolumeView.setOnClickListener { presenter.configureAudioVolume() }
        chooseDirectoryView.setOnClickListener {
            presenter.chooseDirectory()
        }

        action.addOnActivityResult { requestCode, resultCode, data ->
            presenter.onActivityResult(requestCode, resultCode, data)
        }

        audioDirDialog = AlertDialog.Builder(context)
                .setTitle("Audio directory")
                .setView(LayoutInflater.from(this.context).inflate(R.layout.choose_audio_dir, null))
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    showDirectoryChooser()
                }
                .setOnDismissListener {
                    showDirectoryChooser()
                }
                .create()
        return viewInstance
    }

    private fun showDirectoryChooser() {
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        val chooser = Intent.createChooser(i, "Choose directory")
        startActivityForResult(chooser, LocalMusicPlugin.PICK_DIRECTORY, null)
    }

    fun showFolderSelectionDialog(default: File) {
        audioDirDialog.show()
        audioDirDialog.window?.setBackgroundDrawableResource(R.color.colorBackground)
    }

    fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        action.startActivityForResult(intent, requestCode, options)
    }

    fun showNoAudioTracksFoundMessage() {
        context.applicationContext.runOnUiThread {
            longToast("Whoops, no music found in current audio directory")
        }
    }

    fun showAudioError() {
        context.applicationContext.runOnUiThread {
            longToast("Whoops, there was an error with audio")
        }
    }

    fun setPlayUntilEndTo(keyword: String) {
        val playUntilEndAnswerText = "<font color=#FFFFFF>$keyword</font>"
        playUntilEndAnswer?.text = Html.fromHtml(playUntilEndAnswerText)
    }
}