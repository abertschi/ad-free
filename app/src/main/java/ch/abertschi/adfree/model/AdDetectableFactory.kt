package ch.abertschi.adfree.model

import android.content.Context
import ch.abertschi.adfree.detector.*
import ch.abertschi.adfree.model.PreferencesFactory

class AdDetectableFactory(var context: Context,
                          val prefs: PreferencesFactory) {

    private var adDetectors: List<AdDetectable> = listOf<AdDetectable>(
            NotificationActionDetector()
            , SpotifyTitleDetector(TrackRepository(this.context, prefs))
            , NotificationBundleAndroidTextDetector()
            , MiuiNotificationDetector()
            , ScDetector()
//                , SpotifyNotificationDebugTracer(context.getExternalFilesDir(null))
    )


    fun loadMetadata() {
        prefs.loadAdDetectables(adDetectors)
    }

    fun saveMetadataForDetectable(d: AdDetectable) {
        prefs.saveAdDetectable(d)
    }

    fun saveMetadata() {
        prefs.saveAdDetectables(adDetectors)
    }

    fun getDetectors() = adDetectors
}