package ch.abertschi.adfree.detector

import android.app.Notification
import ch.abertschi.adfree.model.TextRepository
import org.jetbrains.anko.AnkoLogger

class TextDetector(private val repo: TextRepository) : AdDetectable, AnkoLogger {

    override fun canHandle(payload: AdPayload): Boolean {
        var notificationKey: String? = payload?.statusbarNotification?.key?.toLowerCase()
            ?: return false
        var canHandle = false;
        for (key in repo.getAllKeys()) {
            if (notificationKey?.contains(key.toLowerCase().trim()) == true) {
                payload.matchedPackageKeys.add(key)
                canHandle = true;
            }
        }
        return canHandle
    }

    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        val extras = payload.statusbarNotification?.notification?.extras
        val title: String? = extras?.getString(Notification.EXTRA_TITLE)?.trim()?.toLowerCase()
        val subTitle: String? =
            extras?.getString(Notification.EXTRA_SUB_TEXT)?.trim()?.toLowerCase()


        for (key in payload.matchedPackageKeys) {
            for (entryLine in repo.getEntry(key)) {
                if (entryLine.trim().isEmpty()) {
                    continue
                }
                val matchTitle = title != null && title.contains(entryLine.toLowerCase().trim())
                val matchSubtitle =
                    subTitle != null && subTitle.contains(entryLine.toLowerCase().trim())
                if (matchTitle || matchSubtitle) {
                    return true;
                }
            }
        }
        return false;
    }

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Generic text base detector", "flag a notification based on the presence of text",
        false,
        category = "General",
        debugOnly = false
    )
}
