package ch.abertschi.adfree.view.mod

import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.detector.*
import org.jetbrains.anko.AnkoLogger

class ActiveDetectorPresenter(val view: ActiveDetectorActivity) : AnkoLogger {

    private val detectorFactory = (view.applicationContext as AdFreeApplication).adDetectors
    private val prefs = (view.applicationContext as AdFreeApplication).prefs

    fun getDetectors(category: String) = detectorFactory.getDetectorsForCategory(category)

    fun isEnabled(d: AdDetectable) = detectorFactory.isEnabled(d)

    fun onDetectorToggled(enable: Boolean, detector: AdDetectable) {
        detectorFactory.setEnable(enable, detector)
        detectorFactory.persistMeta()
        showAdditionalInfoFor(detector, enable)
    }

    fun showAdditionalInfoFor(d: AdDetectable, enable: Boolean) {
        if (d is AbstractDebugTracer && enable) {
            view.showInfo("recording to " + (d.storageFolder?.absolutePath ?: "not available, check permissions"))
        }

        if (d is TextDetector && enable) {
            // launch activity
        }
    }
}
