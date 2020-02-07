package ch.abertschi.adfree.view.mod

import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.detector.AdDetectable
import ch.abertschi.adfree.detector.ScNotificationDebugTracer
import ch.abertschi.adfree.detector.SpotifyNotificationDebugTracer
import org.jetbrains.anko.AnkoLogger

class ActiveDetectorPresenter(val view: ActiveDetectorActivity) : AnkoLogger {

    private val detectorFactory = (view.applicationContext as AdFreeApplication).adDetectors
    private val prefs = (view.applicationContext as AdFreeApplication).prefs
    private var tabCounterForDebug = 0
    private val tabCounterForDebugThreshold = 5

    fun getDetectors(): List<AdDetectable> = detectorFactory.getVisibleDetectors()

    fun isEnabled(d: AdDetectable) = detectorFactory.isEnabled(d)

    fun onDetectorToggled(enable: Boolean, detector: AdDetectable) {
        detectorFactory.setEnable(enable, detector)
        detectorFactory.persistMeta()
        showAdditionalInfoFor(detector, enable)
    }

    // TODO: once this gets more advanced, wrap into view/model
    fun showAdditionalInfoFor(d: AdDetectable, enable: Boolean) {
        if (d is SpotifyNotificationDebugTracer && enable) {
            view.showInfo("recording to " + (d.storageFolder?.absolutePath ?: "not available"))
        }

        if (d is ScNotificationDebugTracer && enable) {
            view.showInfo("recording to " + (d.storageFolder?.absolutePath ?: "not available"))
        }
    }

    fun onTabTitle() {
        tabCounterForDebug ++
        if (tabCounterForDebug > tabCounterForDebugThreshold) {
            tabCounterForDebug = 0
            if (prefs.isDebugDetectors()) {
                prefs.setDebugDetectors(false)
                view.hideEnabledDebug()
            } else {
                prefs.setDebugDetectors(true)
                view.showEnabledDebug()
            }
        }
    }

}
