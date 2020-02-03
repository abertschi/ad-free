/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.detector

import android.app.Notification
import android.os.Bundle
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn


/**
 * Created by abertschi on 17.04.17.
 */
class NotificationBundleAndroidTextDetector : AbstractStatusBarDetector(), AnkoLogger {

    override fun canHandle(payload: AdPayload): Boolean = super.canHandle(payload)
            && payload?.statusbarNotification?.notification != null

    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        try {
            val bundle = getNotificationBundle(payload!!.statusbarNotification!!.notification)
            var flagAsAd = false
            bundle.let {
                val androidText: CharSequence? = bundle?.get("android.text") as CharSequence?
                flagAsAd = androidText == null
                        && payload!!.statusbarNotification!!.notification!!
                        .tickerText?.isNotEmpty() ?: false
            }
            return flagAsAd

        } catch (e: Exception) {
            warn(e)
        }
        return false
    }

    private fun getNotificationBundle(notification: Notification): Bundle? {
        try {
            //NoSuchFieldException
            val f = notification.javaClass.getDeclaredField("extras")
            f.isAccessible = true
            return f.get(notification) as Bundle
        } catch (e: Exception) {
            error("Can not access notification bundle with reflection, $e")
        }
    }
    override fun getMeta(): AdDetectorMeta
            = AdDetectorMeta("Notification bundle", "spotify generic inspection of notification bundle")
}