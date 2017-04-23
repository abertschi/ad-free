package ch.abertschi.adump.view.setting

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import ch.abertschi.adump.R
import ch.abertschi.adump.presenter.SettingsPresenter
import ch.abertschi.adump.view.AppSettings
import org.jetbrains.anko.onItemSelectedListener
import org.jetbrains.anko.onTouch


/**
 * Created by abertschi on 21.04.17.
 */

class SettingsActivity : Fragment(), SettingsView {

    private lateinit var mTypeFace: Typeface
    private var rootView: View? = null
    private var mSettingsTitle: TextView? = null
    private var mSpinner: Spinner? = null
    private var pluginViewContainer: LinearLayout? = null
    private var spinnerAdapter: ReplacerSpinnerAdapter? = null
    private var init: Boolean = true

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

        mTypeFace = AppSettings.instance(this.activity).typeFace
        mSettingsTitle = view?.findViewById(R.id.settingsTitle) as TextView
        mSettingsTitle?.typeface = mTypeFace

        settingPresenter = SettingsPresenter(this)

        val text = "what do you want to do while <font color=#FFFFFF>ads </font>are <font color=#FFFFFF>being played ?</font>"
        mSettingsTitle?.text = Html.fromHtml(text)

        mSpinner = view?.findViewById(R.id.spinner) as Spinner
        spinnerAdapter = ReplacerSpinnerAdapter(this.activity, R.layout.replacer_setting_item, settingPresenter.getStringEntriesOfModel())
        mSpinner?.adapter = spinnerAdapter
        mSpinner?.onItemSelectedListener {
            onItemSelected { adapterView, view, i, l ->
                run {
                    if (!init) settingPresenter.onPluginSelected(i)
                    spinnerAdapter?.notifyDataSetChanged()
                }
            }
        }
        mSpinner?.onTouch { view, motionEvent -> mSpinner?.performClick()!! }


        view.findViewById(R.id.try_plugin_button).setOnClickListener {
            settingPresenter.tryPlugin()
        }


        settingPresenter.onCreate()
        init = false
    }

    override fun onResume() {
        super.onResume()
        settingPresenter.onResume()
    }

    override fun setActivePlugin(index: Int) {
        mSpinner?.setSelection(index, true)
    }

    override fun getContext(): Context = this.activity

    override fun showSuggestNewPlugin() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/abertschi/ad-free/issues"))
        this.getContext().startActivity(browserIntent)
    }
}
