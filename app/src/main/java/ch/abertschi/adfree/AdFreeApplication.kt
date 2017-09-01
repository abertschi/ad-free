/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree

import android.app.Application
import ch.abertschi.adfree.ad.AdDetector
import ch.abertschi.adfree.detector.AdDetectable
import ch.abertschi.adfree.detector.NotificationActionDetector
import ch.abertschi.adfree.detector.NotificationBundleAndroidTextDetector
import ch.abertschi.adfree.detector.SpotifyTitleDetector
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.model.TrackRepository
import ch.abertschi.adfree.plugin.AdPlugin
import ch.abertschi.adfree.plugin.PluginHandler
import ch.abertschi.adfree.plugin.interdimcable.InterdimCablePlugin
import ch.abertschi.adfree.plugin.localmusic.LocalMusicPlugin
import ch.abertschi.adfree.plugin.mute.MutePlugin
import ch.abertschi.adfree.util.NotificationUtils
import ch.abertschi.adfree.util.UsageFeedback


/**
 * Created by abertschi on 21.04.17.
 */

class AdFreeApplication : Application() {

    lateinit var prefs: PreferencesFactory
    lateinit var feedback: UsageFeedback
    lateinit var adDetectors: List<AdDetectable>
    lateinit var adDetector: AdDetector
    lateinit var audioManager: AudioController
    lateinit var pluginHandler: PluginHandler
    lateinit var adPlugins: List<AdPlugin>
    lateinit var adStateController: AdStateController
    lateinit var notificationUtils: NotificationUtils
    lateinit var notificationChannel: NotificationChannel

    override fun onCreate() {
        super.onCreate()
        prefs = PreferencesFactory(applicationContext)
        feedback = UsageFeedback(applicationContext, prefs)
        feedback.trackFirstRun()

        adDetectors = listOf<AdDetectable>(NotificationActionDetector()
                , SpotifyTitleDetector(TrackRepository(this, prefs))
                , NotificationBundleAndroidTextDetector())

        audioManager = AudioController(applicationContext, prefs)

        adDetector = AdDetector(adDetectors)

        adPlugins = listOf(MutePlugin(),
                InterdimCablePlugin(prefs, audioManager, applicationContext),
                LocalMusicPlugin(applicationContext, prefs, audioManager)
        )
        pluginHandler = PluginHandler(prefs, adPlugins, adDetector)


        notificationUtils = NotificationUtils(applicationContext)

        notificationChannel = NotificationChannel(notificationUtils)
        adStateController = AdStateController(audioManager, pluginHandler, notificationChannel)

        adDetector.addObserver(adStateController)

    }
}
