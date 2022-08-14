/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.AsyncTask
import ch.abertschi.adfree.ad.AdDetector
import ch.abertschi.adfree.plugin.AdPlugin
import ch.abertschi.adfree.plugin.PluginHandler
import ch.abertschi.adfree.plugin.interdimcable.InterdimCablePlugin
import ch.abertschi.adfree.plugin.localmusic.LocalMusicPlugin
import ch.abertschi.adfree.plugin.mute.MutePlugin
import ch.abertschi.adfree.util.NotificationUtils
import org.jetbrains.anko.AnkoLogger
import ch.abertschi.adfree.crashhandler.CrashExceptionHandler
import ch.abertschi.adfree.model.*
import com.thoughtworks.xstream.mapper.Mapper
import java.lang.NullPointerException


/**
 * Created by abertschi on 21.04.17.
 */

class AdFreeApplication : Application(), AnkoLogger {

    lateinit var prefs: PreferencesFactory
    lateinit var adDetectors: AdDetectableFactory
    lateinit var adDetector: AdDetector
    lateinit var audioManager: AudioController
    lateinit var pluginHandler: PluginHandler
    lateinit var adPlugins: List<AdPlugin>
    lateinit var adStateController: AdStateController
    lateinit var notificationUtils: NotificationUtils
    lateinit var notificationChannel: NotificationChannel
    lateinit var yesNoModel: YesNoModel
    lateinit var remoteManager: RemoteManager
    lateinit var notificationStatus: NotificationStatusManager
    lateinit var googleCast: GoogleCastManager

    lateinit var mainActivity: Activity

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(CrashExceptionHandler(this))

        prefs = PreferencesFactory(applicationContext)

        googleCast = GoogleCastManager(prefs)
        notificationStatus = NotificationStatusManager(applicationContext)

        adDetectors = AdDetectableFactory(applicationContext, prefs)

        audioManager = AudioController(applicationContext, prefs)
        remoteManager = RemoteManager(prefs)
        adDetector = AdDetector(adDetectors, remoteManager)

        yesNoModel = YesNoModel(this)
        yesNoModel.getRandomYes()

        notificationUtils = NotificationUtils(applicationContext)
        notificationChannel = NotificationChannel(notificationUtils, prefs)

        adPlugins = listOf(MutePlugin(),
                LocalMusicPlugin(applicationContext, prefs, audioManager, yesNoModel),
                InterdimCablePlugin(prefs, audioManager, applicationContext, notificationChannel)
        )
        pluginHandler = PluginHandler(prefs, adPlugins, adDetector)

        adStateController = AdStateController(audioManager,
                pluginHandler, notificationChannel, googleCast, prefs)

        adDetector.addObserver(adStateController)

        notificationStatus.restartNotificationListener()

        AsyncTask.execute {
            if (prefs.isAlwaysOnNotificationEnabled()) {
             notificationStatus.forceTimedRestart()
            }
        }
    }
}