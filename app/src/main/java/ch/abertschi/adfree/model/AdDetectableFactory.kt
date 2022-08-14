package ch.abertschi.adfree.model

import android.content.Context
import ch.abertschi.adfree.detector.*

class AdDetectableFactory(var context: Context,
                          val prefs: PreferencesFactory) {

    private var enableMap = HashMap<AdDetectable, Boolean>()

    private var isGloballyEnabled = true

    private var adDetectors: List<AdDetectable> = listOf(
            NotificationActionDetector()
            , SpotifyTitleDetector(TrackRepository(this.context, prefs))
            , NotificationBundleAndroidTextDetector()
            , MiuiNotificationDetector()
            , ScDetector()
            , DummyGlobal()
            , DummySpotifyDetector()
            , SpotifyNotificationDebugTracer(context.getExternalFilesDir(null))
            , ScNotificationDebugTracer(context.getExternalFilesDir(null))
    )

    init {
        loadMeta()
    }

    private fun loadMeta() {
        isGloballyEnabled = prefs.isBlockingEnabled()
        adDetectors.forEach { enableMap[it] = prefs.isAdDetectableEnabled(it) }
    }

    fun persistMeta() {
        enableMap.entries.forEach { prefs.saveAdDetectableEnable(it.value, it.key) }
    }

    fun isAdfreeEnabled() = isGloballyEnabled

    fun setAdfreeEnabled(e: Boolean) {
        isGloballyEnabled = e
        prefs.setBlockingEnabled(e)
    }

    fun isEnabled(d: AdDetectable): Boolean {
        return enableMap[d] ?: true
    }

    fun setEnable(enable: Boolean, d: AdDetectable) {
        enableMap[d] = enable
    }

    fun getEnabledDetectors() = adDetectors.filter { isEnabled(it) }

    fun getAllDetectors() = adDetectors

    fun getDetectorsForCategory(c: String) = getVisibleDetectors().filter { it.getMeta().category == c }

    fun getVisibleDetectors() =
        if (prefs.isDeveloperModeEnabled()) {
            getAllDetectors()
        } else adDetectors.filter { !it.getMeta().debugOnly }

    fun getVisibleCategories() =
        getVisibleDetectors().map {it.getMeta().category}.toHashSet().toList()
}