package ch.abertschi.adfree.detector

import android.annotation.SuppressLint
import android.widget.RemoteViews
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.warn
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class AccuradioDetector : AdDetectable, AnkoLogger, AbstractNotificationDetector() {

    override fun getPackageName(): String {
        return "com.slipstream.accuradio"
    }

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Accuradio", "notification text based detector for accuradio",
        true,
        category = "Accuradio",
        debugOnly = false
    )


    private fun extractObject(target: JvmType.Object, declaredField: String): Any? {
        try {
            val f = target.javaClass.getDeclaredField(declaredField) //NoSuchFieldException
            f.isAccessible = true
            return f.get(target)
        } catch (e: Exception) {
            warn("Can not access $declaredField with reflection, $e")
        }
        return null
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun getActions(views: RemoteViews): List<*>? {
        try {
            val f = views.javaClass.getDeclaredField("mActions") //NoSuchFieldException
            f.isAccessible = true
            return f.get(views) as List<*>
        } catch (e: Exception) {
            warn("Can not access mactions with reflection, $e")
        }
        return null
    }

    private fun extractObject(target: Any, declaredField: String): Any? {
        return try {
            val f = target.javaClass.getDeclaredField(declaredField) //NoSuchFieldException
            f.isAccessible = true
            return f.get(target)
        } catch (e: Exception) {
            warn("Can not access $declaredField with reflection, $e")
            null
        }
    }


    private fun inspectContentViews(contentView: RemoteViews?): Boolean {
        try {
            if (contentView != null) {
                val actions = extractObject(contentView, "mActions") as List<*>?
                if (actions != null) {
                    for (a in actions) {
                        if (a == null) {
                            continue
                        }
                        val methodName: Any = extractObject(a, "methodName") ?: continue
                        if (methodName !is CharSequence) {
                            continue
                        }
                        if (methodName != "setText") {
                            continue
                        }
                        val value: Any = extractObject(a, "value") ?: continue
                        if (value !is CharSequence) {
                            continue
                        }
                        if (value.toString().trim().toLowerCase().contains("music will resume shortly")) {
                            return true
                        }
                    }
                }
            }
        } catch (e: Exception) {
            warn(e)
        }
        return false
    }

    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        // XXX: Support old deprecated fields
        val contentView = payload.statusbarNotification.notification?.contentView
        val bigView = payload.statusbarNotification.notification?.bigContentView
        val tickerView = payload.statusbarNotification.notification?.tickerView

        for (v in listOf(contentView, bigView, tickerView)) {
            if (inspectContentViews(v)) {
                return true;
            }
        }
        return false;
    }
}