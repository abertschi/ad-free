package ch.abertschi.adfree.detector

import java.lang.IllegalStateException

class BestEffortTextDetector : AbstractNotificationBundleAndroidTextDetector() {

    open override fun canHandle(payload: AdPayload): Boolean {
        var key: String = payload?.statusbarNotification?.key?.toLowerCase() ?: return false
        for (p in getPackageList()) {
            if (key.contains(p)) {
                return true
            }
        }
        return false
    }

    companion object {
        val cues = listOf<String>(
            "werbung",
            "advertisement",
            "advertising",
            "publicité",
            "pubblicità",
            "publicidad",
            "reklame",
            "reklaamimine",
            "reklaami",
            "διαφήμισης",
            "διαφήμιση",
            "iklan",
            "reklama",
            "reklama",
            "ogłoszenie",
            "reklama",
            "publicidade",
            "реклама",
            "reklam",
            "reklamcılık"

        )

        val packages = listOf(
            "com.spotify",
            "com.slipstream.accuradio",
            "deezer.android",
            "com.soundcloud.android",
            "com.aspiro.tidal"
        )
    }

    override fun getPackageName(): String {
        throw IllegalStateException("not used")
    }

    private fun getPackageList(): List<String> {
        return packages
    }

    override fun detectAsAdvertisement(
        payload: AdPayload,
        title: Pair<String?, Boolean>,
        text: Pair<String?, Boolean>,
        subtext: Pair<String?, Boolean>
    ): Boolean {
        if (title.second && tryMatch(title.first)) {
            return true
        }
        if (subtext.second && tryMatch(subtext.first)) {
            return true
        }
        if (text.second && tryMatch(text.first)) {
            return true
        }
        return false
    }

    private fun tryMatch(s: String?): Boolean {
        if (s == null) return false
        for (c in cues) {
            if (s.contains(c)) {
                return true
            }
        }
        return false
    }

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Best effort detector",
        "Parses various text fields of notification of all supported media players", false,
        category = "General",
        debugOnly = false
    )
}