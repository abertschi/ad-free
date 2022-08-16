package ch.abertschi.adfree.detector

import android.app.Notification
import android.os.Bundle
import ch.abertschi.adfree.model.TextRepository
import ch.abertschi.adfree.model.TextRepositoryData
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn
import java.util.*

class TextDetector(private val repo: TextRepository) : AdDetectable, AnkoLogger {

    override fun canHandle(payload: AdPayload): Boolean {
        var notificationKey: String? =
            payload?.statusbarNotification?.key?.toLowerCase() ?: return false

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

    private fun extractString(extras: Bundle?, s: String): String? {
        return try {
            (extras?.get(s) as CharSequence?)
                ?.toString()?.trim()?.toLowerCase()
        } catch (e: Exception) {
            warn { e }
            null
        }
    }

    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        val extras = payload.statusbarNotification?.notification?.extras
        val title = extractString(extras, Notification.EXTRA_TITLE)
        val subTitle = extractString(extras, Notification.EXTRA_SUB_TEXT)

        for (entry in payload.matchedTextDetectorEntries) {
            for (entryLine in entry.content) {
                if (entryLine.trim().isEmpty()) {
                    continue
                }
                val matchTitle = title != null && title.contains(entryLine.trim().toLowerCase())
                val matchSubtitle =
                    subTitle != null && subTitle.contains(entryLine.trim().toLowerCase())
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
