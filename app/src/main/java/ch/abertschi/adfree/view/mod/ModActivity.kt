package ch.abertschi.adfree.view.mod

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.text.Html

import android.view.View

import android.widget.TextView

import android.support.v7.app.AlertDialog
import android.widget.SeekBar


import android.view.LayoutInflater
import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.R
import org.jetbrains.anko.*

import android.content.Intent
import android.net.Uri
import ch.abertschi.adfree.BuildConfig


class ModActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var delayDialog: AlertDialog

    private lateinit var delayLayout: View
    private var enabledSwitch: SwitchCompat? = null
    private var alwaysOnSwitch: SwitchCompat? = null
    private lateinit var presenter: ModPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mod_activity)

        presenter = ModPresenter(this,
                (application as AdFreeApplication).prefs)

        val textView = findViewById(R.id.modTitle) as TextView
        val text =
                "change how <font color=#FFFFFF>ad-free</font> " +
                        "internally works."
        textView.text = Html.fromHtml(text)

        val factory = LayoutInflater.from(this)
        delayLayout = factory.inflate(R.layout.mod_delay_unmute, null)

        enabledSwitch = findViewById<SwitchCompat>(R.id.enableAdfreeSwitch)

        findViewById<View>(R.id.enableText).onClick { presenter.onEnableToggleChanged() }
        findViewById<View>(R.id.enableSubtext).onClick { presenter.onEnableToggleChanged() }
        findViewById<View>(R.id.enabledLayout).onClick { presenter.onEnableToggleChanged() }
        findViewById<View>(R.id.enableAdfreeSwitch).onClick { presenter.onEnableToggleChanged() }


        findViewById<View>(R.id.delay_unmute_mod_layout).onClick { presenter.onDelayUnmute() }
        findViewById<View>(R.id.delay_unmute_mod_title).onClick { presenter.onDelayUnmute() }
        findViewById<View>(R.id.delay_unmute_mod_subtitle).onClick { presenter.onDelayUnmute() }

        findViewById<View>(R.id.always_on_layout).onClick { presenter.onToggleAlwaysOnChanged() }
        findViewById<View>(R.id.always_on_text).onClick { presenter.onToggleAlwaysOnChanged() }
        findViewById<View>(R.id.always_on_subtext).onClick { presenter.onToggleAlwaysOnChanged() }
        findViewById<View>(R.id.always_on_switch).onClick { presenter.onToggleAlwaysOnChanged() }

        findViewById<View>(R.id.active_detectors_layout).onClick { presenter.onLaunchActiveDetectorsView() }
        findViewById<View>(R.id.active_detectors_title).onClick { presenter.onLaunchActiveDetectorsView() }
        findViewById<View>(R.id.active_detectors_subtitle).onClick { presenter.onLaunchActiveDetectorsView() }

        val versionView = findViewById<TextView>(R.id.mod_version1) as TextView
        versionView.text =
                "> version ${BuildConfig.VERSION_NAME} / ${BuildConfig.VERSION_CODE}"

        versionView.onClick {
            val browserIntent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/abertschi/ad-free/blob/master/CHANGELOG.md"))
            this.startActivity(browserIntent)
        }


        val alert = AlertDialog.Builder(this)
        alert.setTitle("> delay unmute")
        alert.setView(delayLayout)

        delayDialog = alert.create()

        alwaysOnSwitch = findViewById<SwitchCompat>(R.id.always_on_switch)

        val seek = delayLayout.findViewById(R.id.delay_unmute_seekbar) as SeekBar
        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                presenter.onDelayChanged(progress)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                presenter.onDelayChanged(seekBar!!.progress)
            }
        })

        presenter.onCreate(this)
    }

    fun showDelayUnmute() {
        delayDialog.show()
        delayDialog.getWindow().setBackgroundDrawableResource(R.color.colorBackground);
    }

    fun setDelayValue(p: Int) {
        val view = delayLayout.findViewById(R.id.unmutetext2) as TextView
        val text = "${p} seconds"
        view.text = text
        val seek = delayLayout.findViewById(R.id.delay_unmute_seekbar) as SeekBar
        seek.progress = p
        findViewById<TextView>(R.id.delay_unmute_mod_subtitle).text = text
    }

    fun setEnableToggle(b: Boolean) {
        enabledSwitch?.isChecked = b
        findViewById<TextView>(R.id.enableSubtext)?.text = if (b) "enabled" else "disabled"
    }

    fun setNotificationEnabled(b: Boolean) {
        alwaysOnSwitch?.isChecked = b
        info { "always On: " + b }
    }

    fun showPowerEnabled() {
        this.applicationContext?.runOnUiThread {
            toast("ad-free enabled")
        }
    }

//    fun showChooseDetectors() {

}

