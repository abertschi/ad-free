/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */ode.
// */
//
//package ch.abertschi.adfree
//
//import android.service.notification.NotificationListenerService
//import android.service.notification.StatusBarNotification
//import ch.abertschi.adfree.detector.*
//import ch.abertschi.adfree.model.PreferencesFactory
//import ch.abertschi.adfree.model.RemoteManager
//import ch.abertschi.adfree.model.RemoteSetting
//import ch.abertschi.adfree.model.TrackRepository
//import ch.abertschi.adfree.util.UpdateManager
//import com.github.javiersantos.appupdater.AppUpdater
//import io.reactivex.Observable
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.schedulers.Schedulers
//import org.jetbrains.anko.AnkoLogger
//import org.jetbrains.anko.debug
//import org.jetbrains.anko.info
//
//
///**
// * Created by abertschi on 11.12.16.
// */
//class NotificationListener : NotificationListenerService(), AnkoLogger {
//
//    private var init: Boolean = false
//    private var audioController: AudioController = AudioController.instance
//    private var remoteSetting: RemoteSetting = RemoteSetting()
//
//    lateinit var remoteManager: RemoteManager
//    lateinit var preferences: PreferencesFactory
//    lateinit var detectors: List<AdDetectable>
//
//    init {
//        info("Spotify Ad listener online")
//    }
//
//    private fun intiVars() {
//        preferences = PreferencesFactory.providePrefernecesFactory(this)
//        detectors = listOf<AdDetectable>(NotificationActionDetector()
//                , SpotifyTitleDetector(TrackRepository(applicationContext, preferences))
//                , NotificationBundleAndroidTextDetector())
//
//        remoteManager = RemoteManager(preferences)
//        remoteManager.getRemoteSettingsObservable().subscribe({
//            remoteSetting = it
//            info { "Downloaded remote settings ..." }
//            debug { remoteSetting }
//        })
//    }
//
//    override fun onNotificationPosted(sbn: StatusBarNotification) {
//        if (!init) {
//            init = true
//            intiVars()
//        }
//        if (audioController.isMusicStreamMuted() || !preferences.isBlockingEnabled()) {
//            return
//        }
//        debug("Spotify Ad Listener is active")
//        applyDetectors(AdPayload(sbn))
//    }
//
//    private fun applyDetectors(payload: AdPayload) {
//        var isMusic = false
//        var isAd = false
//
//        val activeDetectors: ArrayList<AdDetectable> = ArrayList()
//        detectors.forEach {
//            if (it.canHandle(payload)) {
//                activeDetectors.add(it)
//            }
//        }
//        if (activeDetectors.size > 0) {
//            info("${activeDetectors.size} detectors are active for current track")
//        }
//        activeDetectors.forEach {
//            if (it.flagAsMusic(payload)) {
//                isMusic = true
//            }
//        }
//        if (!isMusic) {
//            activeDetectors.forEach {
//                if (it.flagAsAdvertisement(payload)) {
//                    isAd = true
//                }
//            }
//        }
//        if (isAd) {
//            info("Ad detected")
//            if (remoteSetting.enabled) {
//                checkForUpdatesWithNotification()
//                (applicationContext as AdFreeApplication).getFeedback().feedbackAdBlock()
//                audioController.muteMusicAndRunActivePlugin(this)
//            }
//        }
//    }
//
//    private fun checkForUpdatesWithNotification() {
//        val updateManager = UpdateManager(preferences)
//        if (remoteSetting.showNotificationOnUpdate
//                && remoteSetting.useGithubReleasesForUpdateReminder
//                && updateManager.isAppUpdaterForInServiceUseFrequencyDue()) {
//            Observable.create<AppUpdater> { source ->
//                val updater = updateManager.appUpdaterForInServiceUse(this)
//                updater.start()
//                source.onComplete()
//            }.observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
//        }
//    }
//
//    override fun onNotificationRemoved(sbn: StatusBarNotification) {
//        super.onNotificationRemoved(sbn)
//    }
//}