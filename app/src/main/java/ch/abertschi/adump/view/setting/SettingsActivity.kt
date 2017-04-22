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
import android.widget.Spinner
import android.widget.TextView
import ch.abertschi.adump.R
import ch.abertschi.adump.presenter.SettingsPresenter
import ch.abertschi.adump.view.AppSettings
import org.jetbrains.anko.onItemSelectedListener

/**
 * Created by abertschi on 21.04.17.
 */

class SettingsActivity : Fragment(), SettingsView {

    lateinit var mTypeFace: Typeface
    private var mSettingsTitle: TextView? = null
    private var mSpinner: Spinner? = null;

    private var mAdapter: ReplacerSpinnerAdapter? = null

    private var mInit: Boolean = true

    lateinit var mSettingPresenter: SettingsPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.setting_view, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mTypeFace = AppSettings.instance(this.activity).typeFace
        mSettingsTitle = view?.findViewById(R.id.settingsTitle) as TextView
        mSettingsTitle?.typeface = mTypeFace

        mSettingPresenter = SettingsPresenter(this)

        val text = "what do you want to do while <font color=#FFFFFF>ads </font>are <font color=#FFFFFF>being played ?</font>"
        mSettingsTitle?.text = Html.fromHtml(text)

        mSpinner = view?.findViewById(R.id.spinner) as Spinner
        mAdapter = ReplacerSpinnerAdapter(this.activity, R.layout.replacer_setting_item, mSettingPresenter.getStringEntriesOfModel())
        mSpinner?.adapter = mAdapter
        mSpinner?.onItemSelectedListener {
            onItemSelected { adapterView, view, i, l -> if (!mInit) mSettingPresenter.onPluginSelected(i) }
        }

        view.findViewById(R.id.try_plugin_button).setOnClickListener {
            mSettingPresenter.tryPlugin()
        }

        mSettingPresenter.onCreate()
        mInit = false
    }

    override fun onResume() {
        super.onResume()
        mSettingPresenter.onResume()
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
