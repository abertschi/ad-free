package ch.abertschi.adfree.detector

import org.jetbrains.anko.AnkoLogger
import java.io.File

class TidalDebugTracer(storageFolder: File?) : AdDetectable, AnkoLogger,
    AbstractDebugTracer(storageFolder) {

    private val PACKAGE = "com.aspiro.tidal"
    private val FILENAME = "adfree-tidal.txt"

    override fun getPackage() = PACKAGE
    override fun getFileName() = FILENAME

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Tidal tracer",
        "dump tidal notifications to a file. This is for debugging only. ", false,
        category = "Developer",
        debugOnly = true
    )
}
