/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.util

import android.content.Context
import ch.abertschi.adfree.R
import ch.abertschi.adfree.model.PreferencesFactory
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


/**
 * Created by abertschi on 01.05.17.
 */
class UsageFeedback(val context: Context, val prefs: PreferencesFactory) : AnkoLogger {

    companion object {
        val ADBLOCK: String = "adblock"
        val ACTION: String = "action"
        val USE: String = "use"
        val FIRST_RUN: String = "first_run"
    }

    private var feedback: Tracker? = null

    fun trackFirstRun() {
        if (!prefs.isFirstRun()) {
            feedbackFirstRun()
            info { "AdFree first run" }
        }
        prefs.setFirstRun()
    }

    fun trackScreen(obj: Any) {
        trackScreen(obj.javaClass.simpleName)
    }

    fun trackScreen(name: String) {
        val t = getFeedback()
        t.setScreenName(name)
        t.send(HitBuilders.ScreenViewBuilder().build())
    }

    fun feedbackAdBlock() {
        val t = getFeedback()
        t.send(HitBuilders.EventBuilder()
                .setCategory(ACTION)
                .setAction(ADBLOCK)
                .build())
    }

    fun feedbackFirstRun() {
        val t = getFeedback()
        t.send(HitBuilders.EventBuilder()
                .setCategory(USE)
                .setAction(FIRST_RUN)
                .build())

    }

    @Synchronized
    fun getFeedback(): Tracker {
        if (feedback == null) {
            val analytics = GoogleAnalytics.getInstance(context)
            feedback = analytics.newTracker(R.xml.global_tracker)
        }
        return feedback!!
    }

}
