package ch.abertschi.adfree.detector
import org.jetbrains.anko.AnkoLogger
import java.io.File

class DeezerDebugTracer(storageFolder: File?) : AdDetectable, AnkoLogger,

    AbstractDebugTracer(storageFolder) {

    val PACKAGE = "deezer.android"
    val FILENAME = "adfree-deezer.txt"

    override fun getPackage() = PACKAGE
    override fun getFileName() = FILENAME

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Deezer tracer",
        "dump deezer notifications to a file. This is for debugging only. ", false,
        category = "Developer",
        debugOnly = true
    )
}
