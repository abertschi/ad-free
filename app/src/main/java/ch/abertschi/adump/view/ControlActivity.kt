package ch.abertschi.adump.view

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.text.Html
import android.view.View
import android.widget.TextView
import ch.abertschi.adump.R
import ch.abertschi.adump.di.ControlModul
import ch.abertschi.adump.presenter.ControlPresenter

/**
 * Created by abertschi on 15.04.17.
 */

class ControlActivity : AppCompatActivity(), ControlView {
    lateinit var mTypeFace: Typeface

    lateinit var mPowerButton: SwitchCompat
    lateinit var mEnjoySloganText: TextView
    lateinit var controlPresenter: ControlPresenter
    var isInit: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val controlModul = ControlModul(this, this)
        controlPresenter = controlModul.provideControlPresenter()

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.parseColor("#252A2E")
        }

        mPowerButton = findViewById(R.id.switch1) as SwitchCompat
        mTypeFace = Typeface.createFromAsset(assets, "fonts/Raleway-ExtraLight.ttf")
        mEnjoySloganText = findViewById(R.id.enjoy) as TextView

        val author = findViewById(R.id.author) as TextView
        author.typeface = mTypeFace

        mPowerButton.setOnCheckedChangeListener { buttonView, isChecked ->
            controlPresenter.enabledStatusChanged(isChecked)
        }
        controlPresenter.onCreate(this)
        isInit = true
    }

    override fun onResume() {
        if (isInit) {
            controlPresenter.onResume(this)
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
