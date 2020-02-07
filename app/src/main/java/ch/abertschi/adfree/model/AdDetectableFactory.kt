package ch.abertschi.adfree.model

import android.content.Context
import ch.abertschi.adfree.detector.*

class AdDetectableFactory(var context: Context,
                          val prefs: PreferencesFactory) {

    private var enableMap = HashMap<AdDetectable, Boolean>()

    private var adDetectors: List<AdDetectable> = listOf(
            NotificationActionDetector()
            , SpotifyTitleDetector(TrackRepository(this.context, prefs))
            , NotificationBundleAndroidTextDetector()
            , MiuiNotificationDetector()
            , ScDetector()
            , SpotifyNotificationDebugTracer(context.getExternalFilesDir(null))
            , ScNotificationDebugTracer(context.getExternalFilesDir(null))
    )

    init {
        loadMeta()
    }

    private fun loadMeta() {
        adDetectors.forEach { enableMap[it] = prefs.isAdDetectableEnabled(it) }
    }

    fun persistMeta() {
        enableMap.entries.forEach { prefs.saveAdDetectableEnable(it.value, it.key) }
    }

    fun isEnabled(d: AdDetectable): Boolean {
        return enableMap[d] ?: true
    }

    fun setEnable(enable: Boolean, d: AdDetectable) {
        enableMap[d] = enable
    }

    fun getEnabledDetectors() = adDetectors.filter { isEnabled(it) }

    fun getAllDetectors() = adDetectors

    fun getVisibleDetectors() =
        if (prefs.isDebugDetectors()) {
            getAllDetectors()
        } else adDetectors.filter { !it.getMeta().debugOnly }
}