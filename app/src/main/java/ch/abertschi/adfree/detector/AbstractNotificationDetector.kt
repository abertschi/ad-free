package ch.abertschi.adfree.detector

import org.jetbrains.anko.AnkoLogger


abstract class AbstractNotificationDetector : AdDetectable, AnkoLogger {

    // get package lower case
    abstract fun getPackageName(): String

    override fun canHandle(payload: AdPayload): Boolean {
        return payload?.statusbarNotification?.key?.toLowerCase()?.contains(getPackageName())
            ?: false
    }


}

