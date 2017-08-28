/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.detector

import com.thoughtworks.xstream.XStream
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by abertschi on 13.12.16.
 */
abstract class AbstractStatusBarDetector : AdDetectable, AnkoLogger {

    companion object {
        private val SPOTIFY_PACKAGE = "com.spotify"
    }

    override fun canHandle(payload: AdPayload): Boolean {
        val b = payload?.statusbarNotification?.key?.toLowerCase()?.contains(SPOTIFY_PACKAGE) ?: false
//        info { "status: " + b }
//        info { XStream().toXML(payload.statusbarNotification) }
//        info { payload }
        return b
    }


}

