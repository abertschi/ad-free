/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.localmusic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import ch.abertschi.adfree.R
import ch.abertschi.adfree.plugin.PluginActivityAction

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.longToast
import org.jetbrains.anko.runOnUiThread

import android.support.v7.app.AlertDialog
import android.support.v7.widget.SwitchCompat

/**
 * Created by abertschi on 29.08.17.
 */
class LocalMusicView(val context: Context, val action: PluginActivityAction) : AnkoLogger {

    private lateinit var audioDirDialog: AlertDialog
    private lateinit var viewInstance: View

    var presenter: LocalMusicPlugin? = null

    private lateinit var playUntilEndAnswer: TextView

    fun onCreate(presenter: LocalMusicPlugin): View? {
        this.presenter = presenter
        val inflater = LayoutInflater.from(context)
        viewInstance = inflater.inflate(R.layout.plugin_localmusic, null, false)

        viewInstance.findViewById<View>(R.id.layout_play_until_end)
                .setOnClickListener { presenter.onPlayUntilEndChanged() }
        viewInstance.findViewById<View>(R.id.local_music_title_play_until_end)
                .setOnClickListener { presenter.onPlayUntilEndChanged() }
        viewInstance.findViewById<View>(R.id.local_music_play_until_end_subtext)
                .setOnClickListener { presenter.onPlayUntilEndChanged() }
        viewInstance.findViewById<View>(R.id.local_music_play_until_end_switch)
                .setOnClickListener { presenter.onPlayUntilEndChanged() }

        viewInstance.findViewById<View>(R.id.layout_loop)
                .setOnClickListener { presenter.onLoopPlaybackChanged() }
        viewInstance.findViewById<View>(R.id.local_music_title_loop)
                .setOnClickListener { presenter.onLoopPlaybackChanged() }
        viewInstance.findViewById<View>(R.id.local_music_loop_subtext)
                .setOnClickListener { presenter.onLoopPlaybackChanged() }
        viewInstance.findViewById<View>(R.id.local_music_loop_switch)
                .setOnClickListener { presenter.onLoopPlaybackChanged() }

        viewInstance.findViewById<View>(R.id.layout_configure_audio)
                .setOnClickListener { presenter.onConfigureAudioVolume() }
        viewInstance.findViewById<View>(R.id.configure_audio_title)
                .setOnClickListener { presenter.onConfigureAudioVolume() }
        viewInstance.findViewById<View>(R.id.configure_audio_subtitle)
                .setOnClickListener { presenter.onConfigureAudioVolume() }

        viewInstance.findViewById<View>(R.id.layout_music_dir).setOnClickListener {
            presenter.onChooseDirectory()}
        viewInstance.findViewById<View>(R.id.music_dir_title).setOnClickListener {
            presenter.onChooseDirectory()}
        viewInstance.findViewById<View>(R.id.music_dir_subtitle).setOnClickListener {
            presenter.onChooseDirectory()}

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

    fun showPlayUntilEndEnabled(e: Boolean) {
        viewInstance.findViewById<SwitchCompat>(R.id.local_music_play_until_end_switch).isChecked = e
    }

    fun showLoopEnabled(e: Boolean) {
        viewInstance.findViewById<SwitchCompat>(R.id.local_music_loop_switch).isChecked = e
    }

    private fun showDirectoryChooser() {
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        val chooser = Intent.createChooser(i, "Choose directory")
        startActivityForResult(chooser, LocalMusicPlugin.PICK_DIRECTORY, null)
    }

    fun showFolderSelectionDialog() {
        audioDirDialog.show()
        audioDirDialog.window?.setBackgroundDrawableResource(R.color.colorBackground)
    }

    fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        action.startActivityForResult(intent, requestCode, options)
    }

    fun showErrorInChoosingDirectory(hint: String = "") {
        context.applicationContext.runOnUiThread {
            longToast("Whoops, error with chosen directory. Choose a different one. $hint")
        }
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
    fun showNeedStoragePermissions() {
        context.applicationContext.runOnUiThread {
            longToast("Storage permissions needed")
        }
    }

    fun hideLoopMusic(hide: Boolean) {
        viewInstance.findViewById<View>(R.id.layout_loop).visibility =
                if (hide) View.GONE else View.VISIBLE
    }

    fun showAudioDirectoryPath(s: String) {
        viewInstance.findViewById<TextView>(R.id.music_dir_subtitle).text = s
    }

}
