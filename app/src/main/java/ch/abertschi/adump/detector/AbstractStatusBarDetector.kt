/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump.detector

/**
 * Created by abertschi on 13.12.16.
 */
abstract class AbstractStatusBarDetector : AdDetectable {

    companion object {
        private val SPOTIFY_PACKAGE = "com.spotify"
    }

    override fun canHandle(payload: AdPayload): Boolean
            = payload?.statusbarNotification?.key?.toLowerCase()?.contains(SPOTIFY_PACKAGE) ?: false

}
