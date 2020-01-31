package ch.abertschi.adfree.view.mod

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.text.Html

import android.view.View

import android.widget.TextView

import org.jetbrains.anko.AnkoLogger

import org.jetbrains.anko.onClick

import android.support.v7.app.AlertDialog
import android.widget.SeekBar


import android.view.LayoutInflater
import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.R
import org.jetbrains.anko.info


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

        findViewById<View>(R.id.enableText).onClick { presenter.toggleEnabled() }
        findViewById<View>(R.id.enableSubtext).onClick { presenter.toggleEnabled() }
        findViewById<View>(R.id.enabledLayout).onClick { presenter.toggleEnabled() }
        findViewById<View>(R.id.enableAdfreeSwitch).onClick { presenter.toggleEnabled() }


        findViewById<View>(R.id.delay_unmute_mod_layout).onClick { presenter.delayUmute() }
        findViewById<View>(R.id.delay_unmute_mod_title).onClick { presenter.delayUmute() }
        findViewById<View>(R.id.delay_unmute_mod_subtitle).onClick { presenter.delayUmute() }

        findViewById<View>(R.id.always_on_layout).onClick { presenter.toggleAlwaysOn() }
        findViewById<View>(R.id.always_on_text).onClick { presenter.toggleAlwaysOn() }
        findViewById<View>(R.id.always_on_subtext).onClick { presenter.toggleAlwaysOn() }
        findViewById<View>(R.id.always_on_switch).onClick { presenter.toggleAlwaysOn() }


        val alert = AlertDialog.Builder(this)
        alert.setTitle("> delay unmute")
        alert.setView(delayLayout)
        delayDialog = alert.create()

        alwaysOnSwitch = findViewById<SwitchCompat>(R.id.always_on_switch)
        presenter.onCreate(this)
    }

    fun showDelayUnmute() {
        delayDialog.show()

        // todo move
        val seek = delayLayout.findViewById(R.id.delay_unmute_seekbar) as SeekBar
        seek.setOnSeekBarChangeListener (object: SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                presenter.delayChanged(progress)
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                presenter.delayChanged(seekBar!!.progress)
            }
        })
    }

    fun updateDelaySeekbar(p: Int) {
        val view = delayLayout.findViewById(R.id.unmutetext2) as TextView
        val text = "${p} seconds"
        view.text = text
        val seek = delayLayout.findViewById(R.id.delay_unmute_seekbar) as SeekBar
        seek.progress = p
        findViewById<TextView>(R.id.delay_unmute_mod_subtitle).text = text
    }

    fun updateEnableToggle() {
        val b = presenter.isEnabled()
        enabledSwitch?.isChecked = b
        findViewById<TextView>(R.id.enableSubtext)?.text = if (b) "enabled" else "disabled"
    }

    fun updateAlwaysOnToggle() {
        val b = presenter.isAlwaysOnNotification()
        alwaysOnSwitch?.isChecked = b
        info { "always On: " + b}
    }
}