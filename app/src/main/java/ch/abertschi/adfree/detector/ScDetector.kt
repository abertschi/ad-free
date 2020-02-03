package ch.abertschi.adfree.detector

import android.app.Notification
import org.jetbrains.anko.AnkoLogger

class ScDetector : AdDetectable, AnkoLogger {

    private val keyword: String = "advertisement"
    private val pack = "com.soundcloud.android"

    override fun canHandle(payload: AdPayload): Boolean {
        return payload?.statusbarNotification?.key?.toLowerCase()?.contains(pack) ?: false
    }

    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        val extras = payload.statusbarNotification?.notification?.extras
        val title : String? = extras?.getString(Notification.EXTRA_TITLE)?.trim()?.toLowerCase()
        val subTitle: String? = extras?.getString(Notification.EXTRA_SUB_TEXT)

        return title != null && title == keyword
                && subTitle == null
    }

    override fun getMeta(): AdDetectorMeta
            = AdDetectorMeta("Soundcloud DE", "experimental detector for soundcloud DE version")
}