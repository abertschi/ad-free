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
import android.support.v7.widget.SwitchCompat
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
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast

/**
 * Created by abertschi on 15.04.17.
 */

class HomeActivity : Fragment(), HomeView, AnkoLogger {

    lateinit var typeFace: Typeface

    lateinit var powerButton: SwitchCompat
    lateinit var enjoySloganText: TextView
    lateinit var homePresenter: HomePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.home_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homePresenter = HomeModul(this.activity!!, this).provideSettingsPresenter()
        
        powerButton = view?.findViewById(R.id.switch1) as SwitchCompat
        typeFace = ViewSettings.instance(this.context!!).typeFace

        enjoySloganText = view.findViewById(R.id.enjoy) as TextView

        powerButton.setOnCheckedChangeListener { buttonView, isChecked ->
            homePresenter.enabledStatusChanged(isChecked)
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

    override fun onResume() {
        homePresenter.onResume(this.context!!)
        super.onResume()
    }

    override fun showStatusEnabled() {
        context?.applicationContext?.runOnUiThread {
            context?.toast("Ad Free enabled")
        }
    }


    override fun showPermissionRequired() {
        val text = "touch here to grant permission"
        setSloganText(text)
        powerButton.visibility = View.GONE
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
        val text = "enjoy your <font color=#FFFFFF>ad free</font> music experience"
        setSloganText(text)
        enjoySloganText.setOnClickListener(null)
        powerButton.visibility = View.VISIBLE
    }

    override fun setPowerState(state: Boolean) {
        powerButton.isChecked = state
    }
}
