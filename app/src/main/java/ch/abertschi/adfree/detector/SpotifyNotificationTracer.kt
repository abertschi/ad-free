package ch.abertschi.adfree.detector

import android.service.notification.StatusBarNotification
import com.thoughtworks.xstream.XStream
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn
import java.io.File
import java.io.FileOutputStream

class SpotifyNotificationTracer(val storageFolder: File?) : AdDetectable, AnkoLogger {

    companion object {
        private val SPOTIFY_PACKAGE = "com.spotify"
        private val FILENAME = "adfree-spotify.txt"
    }

    override fun canHandle(payload: AdPayload): Boolean {
        if (storageFolder == null){
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
        warn { XStream().toXML(sbn) }
        val stream = FileOutputStream(file, true)
        try {
            stream.write(XStream().toXML(sbn).toByteArray())
        } finally {
            stream.close()
        }
    }
}
