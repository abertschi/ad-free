/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.view.setting

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import ch.abertschi.adfree.R
import ch.abertschi.adfree.di.SettingsModul
import ch.abertschi.adfree.plugin.PluginActivityAction
import ch.abertschi.adfree.presenter.SettingsPresenter
import ch.abertschi.adfree.view.ViewSettings
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.onItemSelectedListener
import org.jetbrains.anko.toast


/**
 * Created by abertschi on 21.04.17.
 */

class SettingsActivity : Fragment(), SettingsView, AnkoLogger, PluginActivityAction {
    override fun activity(): Fragment = this

    private lateinit var typeFace: Typeface
    private var rootView: View? = null
    private var settingsTitle: TextView? = null
    private var spinner: Spinner? = null
    private var pluginViewContainer: LinearLayout? = null
    private var spinnerAdapter: PluginSpinnerAdapter? = null
    private var init: Boolean = false
    private val callablesOnActivityResult:
            MutableList<(requestCode: Int, resultCode: Int, data: Intent?) -> Unit> = ArrayList()

    lateinit var settingPresenter: SettingsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.setting_view, container, false)
    }

    override fun clearPluginView() {
        pluginViewContainer?.removeAllViews()
    }

    override fun setPluginView(view: View) {
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

        pluginViewContainer = rootView?.findViewById(R.id.setting_plugin_view) as LinearLayout
        clearPluginView()
        pluginViewContainer?.addView(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.rootView = view

        typeFace = ViewSettings.instance(this.activity!!).typeFace
        settingsTitle = view?.findViewById(R.id.settingsTitle) as TextView
        settingsTitle?.typeface = typeFace

        settingPresenter = SettingsModul(this.activity!!, this).provideSettingsPresenter()

        val text = "what do you want to do while <font color=#FFFFFF>ads </font>are " +
                "<font color=#FFFFFF>being played ?</font>"

        settingsTitle?.text = Html.fromHtml(text)

        spinner = view?.findViewById(R.id.spinner) as Spinner
        spinnerAdapter = PluginSpinnerAdapter(this.activity!!, R.layout.replacer_setting_item,
                settingPresenter.getStringEntriesOfModel(), spinner!!, view)
        spinner?.adapter = spinnerAdapter

        spinner?.onItemSelectedListener {
            onItemSelected { adapterView, view, i, l ->
                run {
                    if (init) settingPresenter.onPluginSelected(i)
                    spinnerAdapter?.notifyDataSetChanged()
                }
            }
        }
        view.findViewById<ImageView>(R.id.try_plugin_button).setOnClickListener {
            settingPresenter.tryPlugin()
        }
        view.findViewById<LinearLayout>(R.id.setting_spinner_item_container)
                ?.setOnTouchListener { v, event ->
                    spinner?.performClick()
                    false
                }

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

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)
    }

    override fun getContext(): Context = this.activity!!

    override fun showSuggestNewPlugin() {
        val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/abertschi/ad-free/issues"))
        this.context.startActivity(browserIntent)
    }

    override fun showTryOutMessage() {
        this.activity?.toast("Trying out plugin")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callablesOnActivityResult.forEach { it(requestCode, resultCode, data) }
    }

    override fun addOnActivityResult(callable: (requestCode: Int, resultCode: Int, data: Intent?)
    -> Unit) {
        callablesOnActivityResult.add(callable)
    }

    override fun signalizeTryOut() {
        // TODO: crashes
//        YoYo.with(Techniques.Shake)
//                .duration(800)
//                .repeat(0)
//                .playOn(activity?.findViewById(R.id.try_plugin_button))
    }
}
