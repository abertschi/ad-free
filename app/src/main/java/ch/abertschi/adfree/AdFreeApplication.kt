/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree

import android.annotation.SuppressLint
import android.app.Application
import ch.abertschi.adfree.ad.AdDetector
import ch.abertschi.adfree.detector.*
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.model.RemoteManager
import ch.abertschi.adfree.model.TrackRepository
import ch.abertschi.adfree.model.YesNoModel
import ch.abertschi.adfree.plugin.AdPlugin
import ch.abertschi.adfree.plugin.PluginHandler
import ch.abertschi.adfree.plugin.interdimcable.InterdimCablePlugin
import ch.abertschi.adfree.plugin.localmusic.LocalMusicPlugin
import ch.abertschi.adfree.plugin.mute.MutePlugin
import ch.abertschi.adfree.util.NotificationUtils
import org.jetbrains.anko.AnkoLogger
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build.*
import ch.abertschi.adfree.exceptionhandler.SendLogActivity
import kotlin.system.exitProcess
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by abertschi on 21.04.17.
 */

class AdFreeApplication : Application(), AnkoLogger {

    lateinit var prefs: PreferencesFactory
    lateinit var adDetectors: List<AdDetectable>
    lateinit var adDetector: AdDetector
    lateinit var audioManager: AudioController
    lateinit var pluginHandler: PluginHandler
    lateinit var adPlugins: List<AdPlugin>
    lateinit var adStateController: AdStateController
    lateinit var notificationUtils: NotificationUtils
    lateinit var notificationChannel: NotificationChannel
    lateinit var yesNoModel: YesNoModel
    lateinit var remoteManager: RemoteManager

    override fun onCreate() {
        super.onCreate()
        prefs = PreferencesFactory(applicationContext)
        adDetectors = listOf<AdDetectable>(NotificationActionDetector()
                , SpotifyTitleDetector(TrackRepository(this, prefs))
                , NotificationBundleAndroidTextDetector(),
                ScDetector())

        audioManager = AudioController(applicationContext, prefs)
        remoteManager = RemoteManager(prefs)
        adDetector = AdDetector(adDetectors, remoteManager)

        yesNoModel = YesNoModel(this)
        yesNoModel.getRandomYes()

        notificationUtils = NotificationUtils(applicationContext)
        notificationChannel = NotificationChannel(notificationUtils)

        adPlugins = listOf(MutePlugin(),
                InterdimCablePlugin(prefs, audioManager, applicationContext, notificationChannel),
                LocalMusicPlugin(applicationContext, prefs, audioManager, yesNoModel)
        )
        pluginHandler = PluginHandler(prefs, adPlugins, adDetector)

        adStateController = AdStateController(audioManager,
                pluginHandler, notificationChannel)

        adDetector.addObserver(adStateController)



        Thread.setDefaultUncaughtExceptionHandler { thread, e -> handleUncaughtException(thread, e) }


    }

    fun handleUncaughtException(thread: Thread?, e: Throwable?) {
        e?.printStackTrace() // not all Android versions will print the stack trace automatically
        val (summary, logcat) = generateReport(e)

        val time = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(Date())
        val filename = "adfree-crashlog-${time}.txt"
        val file = File(this.filesDir, filename)
        file.writeText(logcat)

        val intent = Intent()
        intent.action = SendLogActivity.ACTION_NAME
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(SendLogActivity.EXTRA_LOGFILE, filename)
        intent.putExtra(SendLogActivity.EXTRA_SUMMARY, summary)
        startActivity(intent)

        System.exit(1)
        exitProcess(1)
    }

    @SuppressLint("SimpleDateFormat")
    private fun generateReport(th: Throwable?): Pair<String, String> {
        val manager = this.packageManager
        var info: PackageInfo? = null
        try {
            info = manager.getPackageInfo(this.packageName, 0)
        } catch (e2: PackageManager.NameNotFoundException) {
        }

        var model = MODEL
        if (!model.startsWith(MANUFACTURER))
            model = "$MANUFACTURER $model"

        val summary = StringBuilder()
        summary.append("Android version: " + VERSION.SDK_INT + "\n")
        summary.append("Device: $model\n")
        summary.append("App version: " + (info?.versionCode ?: "(null)") + "\n")
        summary.append("Time: " + SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(Date()) + "\n")
        summary.append("Root cause: "+ th.toString() + "\n\n\n")

        val logcat = StringBuilder()
        logcat.append("Logcat messages: \n"+ th?.message)
        logcat.append(readLogcat())
        return Pair(summary.toString(), logcat.toString())
    }

    private fun readLogcat(): String {
        val process = Runtime.getRuntime().exec("logcat -d")
        val bufferedReader = BufferedReader(
                InputStreamReader(process.inputStream))
        val log = bufferedReader.readText()
        return log
    }


}
