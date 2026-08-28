/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.detector

import android.app.Notification
import ch.abertschi.adfree.model.TrackRepository
import org.jetbrains.anko.AnkoLogger

/**
 * AdDetectable that checks for the Keyword Spotify
 *
 * Created by abertschi on 15.04.17.
 *
 *
 */
class SpotifyTitleDetector(
    val trackRepository: TrackRepository
) : AbstractSpStatusBarDetector(), AnkoLogger {

    private val keywords = listOf(
        "Advertisement"
    )

    override fun canHandle(payload: AdPayload): Boolean {
        getTitle(payload)?.let { payload.ignoreKeys.add(it) }
        getNotificationText(payload)?.let { payload.ignoreKeys.add(it) }

        return super.canHandle(payload)
    }

    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        val text = getNotificationText(payload)?.trim() ?: return false

        return keywords.any { keyword ->
            text.contains(keyword, ignoreCase = true)
        }
    }

    override fun flagAsMusic(payload: AdPayload): Boolean =
        getTitle(payload)?.let {
            trackRepository.getAllTracks().contains(it)
        } ?: false

    fun getTitle(payload: AdPayload): String? =
        payload.statusbarNotification
            ?.notification
            ?.tickerText
            ?.toString()

    fun getNotificationText(payload: AdPayload): String? =
        payload.statusbarNotification
            ?.notification
            ?.extras
            ?.getCharSequence(Notification.EXTRA_TEXT)
            ?.toString()

    override fun getMeta(): AdDetectorMeta =
        AdDetectorMeta(
            "Notification text",
            "spotify detector for text in notification",
            category = "Spotify"
        )
}