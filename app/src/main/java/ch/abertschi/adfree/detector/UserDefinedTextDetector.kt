package ch.abertschi.adfree.detector

import android.app.Notification
import android.os.Bundle
import ch.abertschi.adfree.model.TextRepository
import ch.abertschi.adfree.model.TextRepositoryData
import com.thoughtworks.xstream.XStream
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn
import java.util.*

class UserDefinedTextDetector(private val repo: TextRepository) : AdDetectable, AnkoLogger {

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

    // Use a fixed approach and search for predefined fields
    private fun flagAsAdvertisementFixed(payload: AdPayload): Boolean {
        val extras = payload.statusbarNotification.notification?.extras
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

    private fun flagAsAdvertisementDynamic(payload: AdPayload): Boolean {
        /*
         * XXX: This implementation is inefficient but simple.
         * Will a reflection approach be better?
         */
        val str = XStream().toXML(payload)!!.toLowerCase()
        for (entry in payload.matchedTextDetectorEntries) {
            for (entryLine in entry.content) {
                if (entryLine.trim().isEmpty()) {
                    continue
                }
                if (str.contains(entryLine.trim().toLowerCase())) {
                    return true
                }
            }
        }
        return false
    }
    override fun flagAsAdvertisement(payload: AdPayload)  =
        if (repo.useReflectionForMatch()) flagAsAdvertisementDynamic(payload)
        else flagAsAdvertisementFixed(payload)

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "User defined text", "flag a notification based on the presence of text",
        false,
        category = "General",
        debugOnly = false
    )
}
