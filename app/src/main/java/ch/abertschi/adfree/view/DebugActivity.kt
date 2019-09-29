package ch.abertschi.adfree.view
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ch.abertschi.adfree.R
import org.jetbrains.anko.AnkoLogger
import java.lang.RuntimeException

class DebugActivity: Fragment(), DebugView, AnkoLogger {

    private lateinit var typeFace: Typeface

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.debug_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        typeFace = ViewSettings.instance(this.context!!).typeFace

//        presenter = AboutModul(this.activity!!, this).provideAboutPresenter()

        val title = view?.findViewById(R.id.debugTitle) as TextView
        title.typeface = typeFace

        val text = "is ad-free <font color=#FFFFFF>not working</font> properly? </br>" +
                "send <font color=#FFFFFF>device logs</font> " +
                "and help improve ad coverage."

        title?.text = Html.fromHtml(text)

//        val subtitle = view?.findViewById(R.id.debugSubtitle) as TextView
//        subtitle.typeface = typeFace

        val info1 = view?.findViewById(R.id.debug_info1) as TextView
        info1.typeface = typeFace

        val info2 = view?.findViewById(R.id.debugInfo2) as TextView
        info2.typeface = typeFace

                info2?.setOnTouchListener { v, event ->
                    throw RuntimeException("oh no")
                    false
                }


//        val text =
//                "built with much &lt;3 by <font color=#FFFFFF>abertschi</font>. " +
//                        "get my latest hacks and follow me on twitter."

//        textView?.text = Html.fromHtml(text)

    }

}