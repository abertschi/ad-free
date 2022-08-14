package ch.abertschi.adfree.detector

import org.jetbrains.anko.AnkoLogger
import java.io.File

class AccuRadioDebugTracer(storageFolder: File?) : AdDetectable, AnkoLogger,
    AbstractDebugTracer(storageFolder) {

    private val PACKAGE = "com.slipstream.accuradio"
    private val FILENAME = "adfree-accuradio.txt"

    override fun getPackage() = PACKAGE
    override fun getFileName() = FILENAME

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Accuradio tracer",
        "dump accuradio notifications to a file. This is for debugging only. ", false,
        category = "Developer",
        debugOnly = true
    )
}
