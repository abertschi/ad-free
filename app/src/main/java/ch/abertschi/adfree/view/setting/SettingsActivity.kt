/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.view.setting

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import ch.abertschi.adfree.R
import ch.abertschi.adfree.presenter.SettingsPresenter
import ch.abertschi.adfree.view.AppSettings
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.onItemSelectedListener


/**
 * Created by abertschi on 21.04.17.
 */

class SettingsActivity : Fragment(), SettingsView, AnkoLogger {

    private lateinit var typeFace: Typeface
    private var rootView: View? = null
    private var settingsTitle: TextView? = null
    private var spinner: Spinner? = null
    private var pluginViewContainer: LinearLayout? = null
    private var spinnerAdapter: PluginSpinnerAdapter? = null
    private var init: Boolean = false

    lateinit var settingPresenter: SettingsPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.setting_view, container, false)
    }

    override fun clearPluginView() {
        pluginViewContainer?.removeAllViews()
    }

    override fun setPluginView(view: View) {
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pluginViewContainer = rootView?.findViewById(R.id.setting_plugin_view) as LinearLayout
        clearPluginView()
        pluginViewContainer?.addView(view)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.rootView = view

        typeFace = AppSettings.instance(this.activity).typeFace
        settingsTitle = view?.findViewById(R.id.settingsTitle) as TextView
        settingsTitle?.typeface = typeFace

        settingPresenter = SettingsPresenter(this)

        val text = "what do you want to do while <font color=#FFFFFF>ads </font>are <font color=#FFFFFF>being played ?</font>"
        settingsTitle?.text = Html.fromHtml(text)

        spinner = view?.findViewById(R.id.spinner) as Spinner
        spinnerAdapter = PluginSpinnerAdapter(this.activity, R.layout.replacer_setting_item, settingPresenter.getStringEntriesOfModel(), spinner!!, view)
        spinner?.adapter = spinnerAdapter

        spinner?.onItemSelectedListener {
            onItemSelected { adapterView, view, i, l ->
                run {
                    if (init) settingPresenter.onPluginSelected(i)
                    spinnerAdapter?.notifyDataSetChanged()

                }
            }
        }
        view.findViewById(R.id.try_plugin_button).setOnClickListener {
            settingPresenter.tryPlugin()
        }
        view.findViewById(R.id.setting_spinner_item_container)?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                spinner?.performClick()
                return false
            }
        })

        settingPresenter.onCreate()
        init = true
    }

    override fun onResume() {
        super.onResume()
        settingPresenter.onResume()
    }

    override fun setActivePlugin(index: Int) {
        spinner?.setSelection(index, true)
    }

    override fun getContext(): Context = this.activity

    override fun showSuggestNewPlugin() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/abertschi/ad-free/issues"))
        this.getContext().startActivity(browserIntent)
    }
}
