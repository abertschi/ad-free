package ch.abertschi.adump.view.home

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
import ch.abertschi.adump.R
import ch.abertschi.adump.di.ControlModul
import ch.abertschi.adump.presenter.ControlPresenter
import ch.abertschi.adump.view.CommonViewSettings

/**
 * Created by abertschi on 15.04.17.
 */

class ControlActivity : Fragment(), ControlView {
    lateinit var mTypeFace: Typeface

    lateinit var mPowerButton: SwitchCompat
    lateinit var mEnjoySloganText: TextView
    lateinit var controlPresenter: ControlPresenter
    var isInit: Boolean = false


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val controlModul = ControlModul(this.context, this)
        controlPresenter = controlModul.provideControlPresenter()

        mPowerButton = view?.findViewById(R.id.switch1) as SwitchCompat
        mTypeFace = CommonViewSettings.instance(this.context).typeFace

        mEnjoySloganText = view.findViewById(R.id.enjoy) as TextView

        mPowerButton.setOnCheckedChangeListener { buttonView, isChecked ->
            controlPresenter.enabledStatusChanged(isChecked)
        }
        controlPresenter.onCreate(this.context)
        isInit = true
    }

    override fun onResume() {
        if (isInit) {
            controlPresenter.onResume(this.context)
        }
        super.onResume()
    }

    override fun showPermissionRequired() {
        val text = "touch here to grant permission"
        setSloganText(text)
        mPowerButton.visibility = View.GONE
        mEnjoySloganText.setOnClickListener {
            showNotificationPermissionSettings()
        }
    }

    override fun showNotificationPermissionSettings() {
        startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }

    private fun setSloganText(text: String) {
        mEnjoySloganText.typeface = mTypeFace
        mEnjoySloganText.text = Html.fromHtml(text)
    }

    override fun showEnjoyAdFree() {
        val text = "enjoy your <font color=#FFFFFF>ad free</font> music experience"
        setSloganText(text)
        mEnjoySloganText.setOnClickListener(null)
        mPowerButton.visibility = View.VISIBLE
    }

    override fun setPowerState(state: Boolean) {
        mPowerButton.isChecked = state
    }
}
