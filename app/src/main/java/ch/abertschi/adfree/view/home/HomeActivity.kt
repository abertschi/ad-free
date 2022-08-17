/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.view.home

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ch.abertschi.adfree.R
import ch.abertschi.adfree.di.HomeModul
import ch.abertschi.adfree.presenter.HomePresenter
import ch.abertschi.adfree.view.ViewSettings
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.onClick

/**
 * Created by abertschi on 15.04.17.
 */

class HomeActivity() : Fragment(), HomeView, AnkoLogger {
    private lateinit var typeFace: Typeface
    private lateinit var enjoySloganText: TextView
    private lateinit var homePresenter: HomePresenter
    private lateinit var updateMessageInfo: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.home_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homePresenter = HomeModul(this.activity!!, this).provideSettingsPresenter()
        

        typeFace = ViewSettings.instance(this.context!!).typeFace

        enjoySloganText = view.findViewById(R.id.enjoy) as TextView
        updateMessageInfo =
                view.findViewById(R.id.version_update_reminder) as TextView

        view.findViewById<TextView>(R.id.troubleshooting).onClick {
            homePresenter.onTroubleshooting()
        }

        homePresenter.onCreate(this.context!!)

        // TODO: this is debug code
//        val r: Random = Random()
//        val c: AdFreeApplication = globalContext.applicationContext as AdFreeApplication
//        view.onTouch { view, motionEvent ->
//            info { "AdFree event created" }
//            when (r.nextBoolean()) {
//                true -> c.adDetector.notifyObservers(AdEvent(EventType.IS_AD))
//                else -> c.adDetector.notifyObservers(AdEvent(EventType.NO_AD))
//            }
//            true
//        }
    }

    override fun showUpdateMessage(show: Boolean) {
        if (show ){
            updateMessageInfo.visibility = View.VISIBLE
            updateMessageInfo.onClick {
                homePresenter.onUpdateMessageClicked()
            }
        } else {
            updateMessageInfo.visibility = View.GONE
        }

    }

    override fun onResume() {
        homePresenter.onResume(this.context!!)
        super.onResume()
    }

    override fun showPermissionRequired() {
        val text = "touch here to grant permission"
        setSloganText(text)
        enjoySloganText.setOnClickListener {
            showNotificationPermissionSettings()
        }
    }

    override fun showNotificationPermissionSettings() {
        startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }

    private fun setSloganText(text: String) {
        enjoySloganText.typeface = typeFace
        enjoySloganText.text = Html.fromHtml(text)
    }

    override fun showEnjoyAdFree() {
        val text = "<font color=#FFFFFF>enjoy</font> your <font color=#FFFFFF>ad-free</font> music experience."
        setSloganText(text)
        enjoySloganText.setOnClickListener(null)
    }

//    override fun setPowerState(state: Boolean) {
//        powerButton.isChecked = state
//    }
}
