/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.localmusic

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.AudioController
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.model.YesNoModel
import ch.abertschi.adfree.plugin.AdPlugin
import ch.abertschi.adfree.plugin.AudioPlayer
import ch.abertschi.adfree.plugin.PluginActivityAction
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import android.provider.DocumentsContract
import java.lang.Exception
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.support.v4.content.ContextCompat.checkSelfPermission


/**
 * Created by abertschi on 01.05.17.
 */
class LocalMusicPlugin(val context: Context,
                       val prefs: PreferencesFactory,
                       val audioController: AudioController,
                       val yesNoModel: YesNoModel) : AdPlugin, AnkoLogger {

    private val supportedFileExt = listOf(".mp3", ".wav", ".m4a")
    private var view: LocalMusicView? = null
    private var player: AudioPlayer = AudioPlayer(context, prefs, audioController)

    companion object {
        val PICK_DIRECTORY = 100
    }

    override fun hasSettingsView(): Boolean = true

    override fun settingsView(context: Context, action: PluginActivityAction): View? {
        view = view ?: LocalMusicView(context, action)
        val settingsView = view!!.onCreate(this)
        view?.showLoopEnabled(prefs.getLoopMusicPlayback())
        view?.showPlayUntilEndEnabled(prefs.getPlayUntilEnd())
        view?.showAudioDirectoryPath(prefs.getLocalMusicDirectory())
        showLoopIfAllowed()
        return settingsView
    }

    override fun play() {
        val file = getRandomTrackfromUri(prefs.getLocalMusicDirectory())
        info { file }
        if (file == null) view?.showNoAudioTracksFoundMessage()
        else {
            info { "playing " + file.absolutePath }
            val ad = context.applicationContext as AdFreeApplication
            val name = file.absolutePath.split("/").last()
            runAndCatchException {
                player.play(file.absolutePath, prefs.getLoopMusicPlayback())
                Observable.just(true).delay(1000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe {
                            val content = when (prefs.getPlayUntilEnd()) {
                                true -> "playing until end - touch to stop"
                                else -> "touch to unmute ad"
                            }

                            ad.notificationChannel.updateAdNotification(title = name,
                                    content = content)
                        }
            }
        }
    }

    override fun playTrial() {
        play()
    }

    override fun requestStop(onStoped: () -> Unit) {
        runAndCatchException {
            player.requestStop(onStoped)
        }
    }

    override fun forceStop(onStoped: () -> Unit) {
        runAndCatchException {
            player.forceStop(onStoped)
        }
    }

    override fun onPluginLoaded() {
    }

    override fun onPluginActivated() {
    }

    override fun onPluginDeactivated() {
        forceStop({})
    }

    override fun stop(onStoped: () -> Unit) {
        if (prefs.getPlayUntilEnd()) {
            requestStop(onStoped)
        } else {
            runAndCatchException {
                player.stop(onStoped)
            }
        }
    }

    override fun title(): String = "local music"

    private fun getRandomTrackfromUri(path: String): File? {
        info { "choosing random track in $path" }
        val musicDir = File(path)
        val allFiles = ArrayList<File>()
        val dirs = LinkedList<File>()
        dirs.add(musicDir)
        while (!dirs.isEmpty()) {
            val d = dirs.poll()
            val listFiles = d.listFiles() ?: continue
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

    fun onConfigureAudioVolume() {
        audioController.showVoiceCallVolume()
    }

    fun onChooseDirectory() {
        if (!hasStoragePermissions()) {
            requestStoragePermissions()
//            view?.showNeedStoragePermissions()
        } else {
            view?.showFolderSelectionDialog()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_DIRECTORY && resultCode == Activity.RESULT_OK) {
            val uri = data?.getData()
            val docUri = DocumentsContract.buildDocumentUriUsingTree(uri,
                    DocumentsContract.getTreeDocumentId(uri))

            var path: String? = null
            try {
                path = getPath(this.context, docUri)
            } catch (e: Exception) {
                view?.showErrorInChoosingDirectory(e.message ?: "")
            }
            if (path != null && File(path).exists()) {
                view?.showAudioDirectoryPath(path)
                prefs.setLocalMusicDirectory(path)
                info { "changing directory to ${prefs.getLocalMusicDirectory()}" }
            } else {
                view?.showErrorInChoosingDirectory()
            }
        }
    }

    private fun runAndCatchException(function: () -> Unit): Unit {
        try {
            function()
        } catch (e: Throwable) {
            view?.showAudioError()
            error(e)
        }
    }

    fun onPlayUntilEndChanged() {
        val playUntilEnd = !prefs.getPlayUntilEnd()
        prefs.setPlayUntilEnd(playUntilEnd)
        view?.showPlayUntilEndEnabled(playUntilEnd)

        val loopMusic = prefs.getLoopMusicPlayback()
        view?.hideLoopMusic(playUntilEnd)
        if (playUntilEnd && loopMusic) {
            prefs.setLoopMusicPlayback(!loopMusic)
            view?.showLoopEnabled(!loopMusic)
        }
    }

    private fun showLoopIfAllowed() {
        val playUntilEnd = prefs.getPlayUntilEnd()
        view?.hideLoopMusic(playUntilEnd)
    }

    fun onLoopPlaybackChanged() {
        val status = !prefs.getLoopMusicPlayback()
        prefs.setLoopMusicPlayback(status)
        view?.showLoopEnabled(status)
    }

//    fun loadPlayUntilEndFlag() {
//        val keyword = when (prefs.getPlayUntilEnd()) {
//            true -> yesNoModel.getRandomYes()
//            else -> yesNoModel.getRandomNo()
//        }
//        view?.setPlayUntilEndTo(keyword)
//    }

    private fun hasStoragePermissions(): Boolean {
        return if (VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(context, READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                info("Permission is revoked")
                false
            }
        } else {
            info("Permission is granted1")
            true
        }
    }

    private fun requestStoragePermissions() {
        view?.action!!.activity().requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), 2)
    }

}
