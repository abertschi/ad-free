package ch.abertschi.adfree.detector

import android.app.Notification
import ch.abertschi.adfree.model.TextRepository
import ch.abertschi.adfree.model.TextRepositoryData
import org.jetbrains.anko.AnkoLogger

class TextDetector(private val repo: TextRepository) : AdDetectable, AnkoLogger {

    override fun canHandle(payload: AdPayload): Boolean {
        var notificationKey: String? = payload?.statusbarNotification?.key?.toLowerCase()
            ?: return false
        var canHandle = false;
        for (entry in repo.getAllEntries()) {
            val key = entry.packageName
            if (key.isEmpty() || key.isBlank()) {
                continue
            }
            if (notificationKey?.contains(key.toLowerCase().trim()) == true) {
                payload.matchedTextDetectorEntries.add(entry)
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


        for (entry in payload.matchedTextDetectorEntries) {
            for (entryLine in entry.content) {
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
        debugOnly = true
    )
}
