package ch.abertschi.adfree.detector

import android.service.notification.StatusBarNotification
import com.thoughtworks.xstream.XStream
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.warn
import java.io.File
import java.io.FileOutputStream

abstract class AbstractDebugTracer(val storageFolder: File?) : AdDetectable, AnkoLogger {

    abstract fun getPackage(): String
    abstract fun getFileName(): String

    override fun canHandle(payload: AdPayload): Boolean {
        if (storageFolder == null) {
            warn { "Given storageFolder is null, cant work. Disabling functionality ..." }
            return false
        }

        if (payload?.statusbarNotification?.key?.toLowerCase()?.contains(getPackage()) == true) {
            recordNotification(payload.statusbarNotification!!)
        }
        return false
    }

    private fun recordNotification(sbn: StatusBarNotification) {
        val file = File(storageFolder, getFileName())
        info { XStream().toXML(sbn) }
        info("writing notification content to $file}")

        val stream = FileOutputStream(file, true)
        try {
            stream.write(XStream().toXML(sbn).toByteArray())
        } catch (e: Exception) {
            info(e)
        } finally {
            stream.close()
        }
    }
}
