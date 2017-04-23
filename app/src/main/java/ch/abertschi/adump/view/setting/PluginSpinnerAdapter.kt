package ch.abertschi.adump.view.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import ch.abertschi.adump.R
import ch.abertschi.adump.view.AppSettings
import org.jetbrains.anko.AnkoLogger

/**
 * Created by abertschi on 21.04.17.
 */

class PluginSpinnerAdapter
    : ArrayAdapter<String>, AnkoLogger {

    private var objects: Array<String>
    private var spinner: Spinner
    private var viewToClickOnToDismissPopup: View?

    constructor(context: Context, textViewResourceId: Int, objects: Array<String>, spinner: Spinner, viewToClickOnToDismissPopup: View? = null) : super(context, textViewResourceId, objects) {
        this.objects = objects
        this.spinner = spinner
        this.viewToClickOnToDismissPopup = viewToClickOnToDismissPopup
    }

    fun setModel(objects: Array<String>) {
        this.objects = objects
    }

    override fun getDropDownView(position: Int, convertView: View?,
                                 parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.replacer_setting_item, parent, false)
        val textView = view.findViewById(R.id.setting_spinner_item) as TextView
        textView.text = objects[position]
        textView.typeface = AppSettings.instance(context).typeFace
        view.setOnClickListener {
            spinner.setSelection(position)
            spinner.performClick()
//            spinner.onDetechedFromWindow()
        }
        textView.setOnClickListener {
            spinner.setSelection(position)
            spinner.performClick()
//            spinner.onDetechedFromWindow()
        }

        return view
    }
}


