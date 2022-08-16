package ch.abertschi.adfree.detector

import android.app.Notification
import android.os.Bundle
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn

open class SpLiteTextDetector : AdDetectable, AnkoLogger, AbstractNotificationBundleTextDetector() {

    override fun getPackage() = "com.spotify.lite"

    override fun detectAsAdvertisement(
        payload: AdPayload,
        title: Pair<String?, Boolean>,
        text: Pair<String?, Boolean>,
        subtext: Pair<String?, Boolean>
    ): Boolean {
        if (listOf(title.second, text.second, subtext.second).contains(false)) {
            return false
        }
        // title first is advertisement in english version
        return title.first != null && title.first!!.isNotEmpty()
                && text.first == null && subtext.first != null
    }

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Generic Text detector", "detector for presence of text for spotify lite",
        true,
        category = "Spotify Lite",
        debugOnly = false
    )
}