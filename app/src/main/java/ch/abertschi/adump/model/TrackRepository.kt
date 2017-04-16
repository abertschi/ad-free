package ch.abertschi.adump.model

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by abertschi on 15.04.17.
 */
open class TrackRepository {

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
        print("storing track: " + content)
        val tracks = getTracks()
        tracks.add(content)
        sharedPreferences.edit().putStringSet(TRACKS, tracks).commit()
    }

    open fun removeTrack(content: String) {
        val tracks = getTracks()
        tracks.remove(content)
        sharedPreferences.edit().putStringSet(TRACKS, tracks).commit()
    }

    open fun getAllTracks(): Set<String> {
        val tracks = getTracks()
        return tracks
    }

    open fun setAllTracksd(tracks: Set<String>) {
        sharedPreferences.edit().putStringSet(TRACKS, tracks).commit()
    }
}