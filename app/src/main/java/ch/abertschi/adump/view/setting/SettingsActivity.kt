package ch.abertschi.adump.view.setting

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import ch.abertschi.adump.R
import ch.abertschi.adump.view.CommonViewSettings
import ch.abertschi.adump.view.setting.ReplacerSpinnerAdapter

/**
 * Created by abertschi on 21.04.17.
 */

class SettingsActivity : Fragment() {

    lateinit var mTypeFace: Typeface
    private var mSettingsTitle: TextView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.music_setting, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mTypeFace = CommonViewSettings.instance(this.activity).typeFace
        mSettingsTitle = view?.findViewById(R.id.settingsTitle) as TextView
        mSettingsTitle?.typeface = mTypeFace
        val text = "what do you want to do while <font color=#FFFFFF>ads </font>are <font color=#FFFFFF>being played ?</font>"
        mSettingsTitle?.text = Html.fromHtml(text)
        val spinner: Spinner = view?.findViewById(R.id.spinner) as Spinner
        val list: List<String> = listOf("mute audio", "local music", "soundcloud", "interdimensional cable", "joke time", "meh", "suggest something ...")

        spinner.adapter = ReplacerSpinnerAdapter(this.activity, R.layout.replacer_setting_item, list.toTypedArray())
    }
}
