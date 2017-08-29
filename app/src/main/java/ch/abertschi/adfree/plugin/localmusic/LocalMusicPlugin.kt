/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.localmusic

import android.content.Context
import android.os.Environment
import android.view.View
import ch.abertschi.adfree.AudioController
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.plugin.AdPlugin
import java.io.File
import java.util.*


/**
 * Created by abertschi on 01.05.17.
 */
class LocalMusicPlugin(val context: Context,
                       val prefs: PreferencesFactory,
                       val audioController: AudioController) : AdPlugin {

    private val supportedFileExt = listOf(".mp3")
    private var view: LocalMusicView? = null

    override fun hasSettingsView(): Boolean = true

    override fun settingsView(context: Context): View? {
        view = view ?: LocalMusicView(context)
        return view!!.onCreate(this)
    }

    override fun play() {
    }

    override fun playTrial() {
        play()
    }

    override fun requestStop(onStoped: () -> Unit) {
    }

    override fun forceStop() {
    }

    override fun onPluginLoaded() {
    }

    override fun onPluginActivated() {
    }

    override fun onPluginDeactivated() {
    }

    override fun title(): String = "local music"

    private fun getUrl(): File? {
        val musicDir: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        val allFiles = ArrayList<File>()
        val dirs = LinkedList<File>()
        dirs.add(musicDir)
        while (!dirs.isEmpty()) {
            val listFiles = dirs.poll().listFiles() ?: continue
            for (f in listFiles) {
                if (f.isDirectory) {
                    dirs.add(f)
                } else if (f.isFile) {
                    for (ext: String in supportedFileExt) {
                        if (f.absoluteFile.toString().endsWith(ext)) {
                            println(f.absoluteFile)
                            allFiles.add(f)
                        }
                    }
                }
            }
        }
        return if (allFiles.size == 0) null else allFiles[(Math.random() * allFiles.size).toInt()]
    }

    fun configureAudioVolume() {
        audioController.showVoiceCallVolume()
    }

    fun chooseDirectory() {
        val path = Environment.getExternalStorageDirectory().absolutePath + "/Music/"
        view?.openFileBrowser(path)
    }
}