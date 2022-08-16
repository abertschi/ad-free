/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.detector

import ch.abertschi.adfree.model.TrackRepository
import org.jetbrains.anko.AnkoLogger

/**
 * AdDetectable that checks for the Keyword Spotify
 *
 * Created by abertschi on 15.04.17.
 *
 *
 */
// TODO: add option to tag ads manually
class SpotifyTitleDetector(val trackRepository: TrackRepository) :
        AbstractSpStatusBarDetector(), AnkoLogger {

    private val keywords = listOf(
            "Spotify —"
            ,"Advertisement —")

    override fun canHandle(payload: AdPayload): Boolean {
        getTitle(payload).let { payload.ignoreKeys.add(it!!) }
        return super.canHandle(payload)
    }

    override fun flagAsAdvertisement(payload: AdPayload): Boolean
            = getTitle(payload)?.toLowerCase()?.trim()?.run {
        var isAdd = false
        for(k in keywords) {
            isAdd = isAdd || k.toLowerCase() == this
        }
        isAdd }?: false

    override fun flagAsMusic(payload: AdPayload): Boolean
            = getTitle(payload).let { trackRepository.getAllTracks().contains(it) }

    fun getTitle(payload: AdPayload): String?
            = payload?.statusbarNotification?.notification?.tickerText?.toString() ?: ""

    override fun getMeta(): AdDetectorMeta
            = AdDetectorMeta("Notification text", "spotify detector for text in notification", category = "Spotify")
}