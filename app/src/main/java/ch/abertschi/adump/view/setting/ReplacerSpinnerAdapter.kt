package ch.abertschi.adump.view.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ch.abertschi.adump.R
import ch.abertschi.adump.view.AppSettings

/**
 * Created by abertschi on 21.04.17.
 */

class ReplacerSpinnerAdapter(context: Context, textViewResourceId: Int,
                             val objects: Array<String>)
    : ArrayAdapter<String>(context, textViewResourceId, objects) {


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
        return view
    }
}


