package ch.abertschi.adfree.detector

import android.service.notification.StatusBarNotification
import com.thoughtworks.xstream.XStream
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.warn
import java.io.File
import java.io.FileOutputStream

class ScNotificationDebugTracer(storageFolder: File?) : AdDetectable, AnkoLogger,
    AbstractDebugTracer(storageFolder) {

    val SOUNDCLOUD_PACKAGE = "com.soundcloud.android"
    val FILENAME = "adfree-soundcloud.txt"

    override fun getPackage() = SOUNDCLOUD_PACKAGE
    override fun getFileName() = FILENAME

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Soundcloud tracer",
        "dump soundcloud notifications to a file. This is for debugging only and drains more battery",
        false,
        category = "Developer",
        debugOnly = true
    )
}
