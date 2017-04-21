package ch.abertschi.adump.view.setting

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ch.abertschi.adump.R
import ch.abertschi.adump.view.AppSettings

/**
 * Created by abertschi on 21.04.17.
 */

class AboutActivity : Fragment() {

    lateinit var mTypeFace: Typeface

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.about_view, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mTypeFace = AppSettings.instance(this.context).typeFace

        val textView = view?.findViewById(R.id.authorTitle) as TextView
        textView.typeface = mTypeFace
        val text =
                "built with &lt;3, a keyboard, and coffee by <font color=#FFFFFF>abertschi</font>. get my latest hacks and follow me on twitter"

        textView?.text = Html.fromHtml(text)
    }
}
