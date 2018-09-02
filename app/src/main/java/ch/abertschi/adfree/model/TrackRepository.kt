/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.model

import android.content.Context
import android.content.SharedPreferences
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by abertschi on 15.04.17.
 */
open class TrackRepository: AnkoLogger {

    private val context: Context
    private val TRACKS: String = "tracks"
    private var sharedPreferences: SharedPreferences

    constructor(context: Context, sharedPreferences: PreferencesFactory) {
        this.context = context
        this.sharedPreferences = sharedPreferences.getPreferences()
    }

    private fun getTracks(): MutableSet<String> {
        return sharedPreferences.getStringSet(TRACKS, HashSet<String>())
    }

    open fun addTrack(content: String) {
        info("storing track: " + content)

        val tracks = getTracks()
        tracks.add(content)
        sharedPreferences.edit().putStringSet(TRACKS, tracks).apply()
    }

    open fun removeTrack(content: String) {
        val tracks = getTracks()
        tracks.remove(content)
        sharedPreferences.edit().putStringSet(TRACKS, tracks).apply()
    }

    open fun getAllTracks(): Set<String> {
        val tracks = getTracks()
        return tracks
    }

    open fun setAllTracks(tracks: Set<String>) {
        sharedPreferences.edit().putStringSet(TRACKS, tracks).apply()
    }
}