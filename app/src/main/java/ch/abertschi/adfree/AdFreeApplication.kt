/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree

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
import ch.abertschi.adfree.crashhandler.CrashExceptionHandler
import android.content.pm.PackageManager
import android.content.ComponentName
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.service.notification.ConditionProviderService.requestRebind
import android.support.annotation.RequiresApi
import org.jetbrains.anko.warn


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
        Thread.setDefaultUncaughtExceptionHandler(CrashExceptionHandler(this))

        prefs = PreferencesFactory(applicationContext)

        adDetectors = listOf<AdDetectable>(
                  NotificationActionDetector()
                , SpotifyTitleDetector(TrackRepository(this, prefs))
                , NotificationBundleAndroidTextDetector()
                , ScDetector()
                , MiuiNotificationDetector()
//                , SpotifyNotificationTracer(getExternalFilesDir(null)) // TODO: for debug
        )

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

        restartNotificationListener()
    }


    fun restartNotificationListener() {
        toggleNotificationListenerService()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val componentName = ComponentName(applicationContext,
                    NotificationsListeners::class.java!!)

            requestRebind(componentName)
        } else {
            warn { "restart notification listener is not supported for current v. of android" }
        }

    }
    /**
     * Try deactivate/activate your component service
     */
    private fun toggleNotificationListenerService() {
        val pm = packageManager
        pm.setComponentEnabledSetting(ComponentName(this, NotificationsListeners::class.java!!),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
        pm.setComponentEnabledSetting(ComponentName(this, NotificationsListeners::class.java!!),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
    }
}