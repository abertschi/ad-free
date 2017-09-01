/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.localmusic

import android.content.Context
import android.content.Intent
import android.view.View
import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.AudioController
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.plugin.AdPlugin
import ch.abertschi.adfree.plugin.AudioPlayer
import ch.abertschi.adfree.plugin.PluginActivityAction
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import java.io.File
import java.util.*

/**
 * Created by abertschi on 01.05.17.
 */
class LocalMusicPlugin(val context: Context,
                       val prefs: PreferencesFactory,
                       val audioController: AudioController) : AdPlugin, AnkoLogger {

    private val supportedFileExt = listOf(".mp3")
    private var view: LocalMusicView? = null
    private var player: AudioPlayer = AudioPlayer(context, prefs, audioController)

    override fun hasSettingsView(): Boolean = true

    override fun settingsView(context: Context, action: PluginActivityAction): View? {
        view = view ?: LocalMusicView(context, action)
        return view!!.onCreate(this)
    }

    override fun play() {
        val file = getRandomTrackfromUri(prefs.getLocalMusicDirectory())
        if (file == null) view?.showNoAudioTracksFoundMessage()
        else {
            info { "playing " + file.absolutePath }
            val ad = context.applicationContext as AdFreeApplication
            val name = file.absolutePath.split("/").last()
            runAndCatchException {
                player.play(file.absolutePath)
                ad.notificationChannel.updateAdNotification(title = name,
                        content = "touch to unmute ad")
            }
        }
    }

    override fun playTrial() {
        play()
    }

    override fun requestStop(onStoped: () -> Unit) {
        runAndCatchException({
            player.requestStop(onStoped)
        })
    }

    override fun forceStop(onStoped: () -> Unit) {
        runAndCatchException({
            player.forceStop(onStoped)
        })
    }

    override fun onPluginLoaded() {
    }

    override fun onPluginActivated() {
    }

    override fun onPluginDeactivated() {
        forceStop({})
    }

    override fun stop(onStoped: () -> Unit) {
        runAndCatchException({
            player.stop(onStoped)
        })
    }

    override fun title(): String = "local music"

    private fun getRandomTrackfromUri(path: String): File? {
        val musicDir = File(path)
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
        val f = File("/storage")
        view?.showFolderSelectionDialog(f)
    }

    fun onDirectorySelected(files: MutableList<String>) {
        files?.firstOrNull().let {
            prefs.setLocalMusicDirectory(it!!)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    private fun runAndCatchException(function: () -> Unit): Unit {
        try {
            function()
        } catch (e: Throwable) {
            view?.showAudioError()
            error(e)
        }
    }
}
