package ch.abertschi.adfree.detector

import android.service.notification.StatusBarNotification
import com.thoughtworks.xstream.XStream
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.warn
import java.io.File
import java.io.FileOutputStream

class SpotifyNotificationDebugTracer(val storageFolder: File?) : AdDetectable, AnkoLogger {

    val SPOTIFY_PACKAGE = "com.spotify"
    val FILENAME = "adfree-spotify.txt"

    override fun canHandle(payload: AdPayload): Boolean {
        if (storageFolder == null) {
            warn { "Given storageFolder is null, cant work. Disabling functionality ..." }
            return false
        }

        if (payload?.statusbarNotification?.key?.toLowerCase()?.contains(SPOTIFY_PACKAGE) == true) {
            recordNotification(payload.statusbarNotification!!)
        }
        return false
    }

    private fun recordNotification(sbn: StatusBarNotification) {
        val file = File(storageFolder, FILENAME)
        info { XStream().toXML(sbn) }
        info("writing spotify notification content to $file}")

        val stream = FileOutputStream(file, true)
        try {
            stream.write(XStream().toXML(sbn).toByteArray())
        } finally {
            stream.close()
        }
    }

    override fun getMeta(): AdDetectorMeta
            = AdDetectorMeta("Spotify tracer",
            "dump spotify notifications to a file. This is for debugging only. ", false,
            debugOnly = true)
}
