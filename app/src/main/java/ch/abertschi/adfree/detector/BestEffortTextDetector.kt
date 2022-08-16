package ch.abertschi.adfree.detector

import java.lang.IllegalStateException

class BestEffortTextDetector : AbstractNotificationBundleTextDetector() {

    open override fun canHandle(payload: AdPayload): Boolean {
        var key: String = payload?.statusbarNotification?.key?.toLowerCase() ?: return false
        for (p in getPackageList()) {
            if (key.contains(p)) {
                return true
            }
        }
        return false
    }

    override fun getPackage(): String {
        throw IllegalStateException("not used")
    }

    fun getPackageList(): List<String> {
        return listOf<String>()
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

    private fun tryMatch(first: String?): Boolean {
        if (first == null) return false

        return false
    }
}