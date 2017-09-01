/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.view.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import ch.abertschi.adfree.R
import ch.abertschi.adfree.view.ViewSettings
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
        textView.typeface = ViewSettings.instance(context).typeFace
        view.setOnClickListener {
            spinner.performClick()
            spinner.setSelection(position)
            hideSpinnerDropDown(spinner)
        }
        textView.setOnClickListener {
            spinner.performClick()
            spinner.setSelection(position)
            hideSpinnerDropDown(spinner)
        }

        return view
    }

    fun setModel(objects: Array<String>) {
        this.objects = objects
    }

    /*
     * This is super ugly, but creating a custom spinner class got me into issues with default styling.
     * So this is the simplest workaround
     * http://stackoverflow.com/questions/17965611/how-to-hide-spinner-dropdown-android
     */
    fun hideSpinnerDropDown(spinner: Spinner) {
        try {
            val method = Spinner::class.java.getDeclaredMethod("onDetachedFromWindow")
            method.isAccessible = true
            method.invoke(spinner)
        } catch (e: Exception) {
            error("Can not hide spinner dialog, " + e)
        }
    }
}


