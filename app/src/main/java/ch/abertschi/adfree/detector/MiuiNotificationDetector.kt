package ch.abertschi.adfree.detector

import android.app.Notification
import android.os.Bundle
import android.text.SpannableString
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn

/**
 * Perform inspection of miui notification bundles
 */
class MiuiNotificationDetector : AbstractStatusBarDetector(), AnkoLogger {

    override fun canHandle(payload: AdPayload): Boolean
            = super.canHandle(payload) && payload?.statusbarNotification?.notification != null


    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        var flagAsAd = false
        val bundle = getNotificationBundle(payload!!.statusbarNotification!!.notification)

        // Notification content:
        //
        // <string>android.title</string>
        //   <android.text.SpannableString>
        //   <mSpanCount>0</mSpanCount>
        //   <mSpanData/>
        //   <mSpans/>
        //   <mText>Advertisement</mText>
        // </android.text.SpannableString>
        //
        try {
            bundle.let {
                val sp: SpannableString? = bundle?.get("android.title") as SpannableString?
                sp?.run {
                    val count = getSpanCount(this)
                    flagAsAd = count != null && count == 0
                }
            }
        } catch(e: java.lang.Exception) {
            warn("Cant apply miui listener, $e")
        }
        return flagAsAd
    }

    private fun getSpanCount(sp: SpannableString): Int? {
        try {
            val f = sp.javaClass.superclass.getDeclaredField("mSpanCount") //NoSuchFieldException
            f.isAccessible = true
            return f.get(sp) as Int?
        } catch (e: Exception) {
            warn("Can not access notification mSpanCount with reflection, $e")
        }
        return null
    }

    private fun getNotificationBundle(notification: Notification): Bundle? {
        try {
            val f = notification.javaClass.getDeclaredField("extras") //NoSuchFieldException
            f.isAccessible = true
            return f.get(notification) as Bundle
        } catch (e: Exception) {
            warn("Can not access notification bundle with reflection, $e")
        }
        return null
    }
}