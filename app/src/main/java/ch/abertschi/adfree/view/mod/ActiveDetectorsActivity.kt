package ch.abertschi.adfree.view.mod

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html

import android.widget.TextView


import android.view.LayoutInflater
import ch.abertschi.adfree.R
import org.jetbrains.anko.*

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SwitchCompat
import android.view.View
import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.ad.AdDetector
import ch.abertschi.adfree.detector.AdDetectable


class ActiveDetectorsActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mod_active_detectors)

        val textView = findViewById(R.id.detectors_activity_title) as TextView
        val text =
                "detectors <font color=#FFFFFF>find ads</font>. " +
                        "choose what's active."
        textView.text = Html.fromHtml(text)


        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter((applicationContext as AdFreeApplication).adDetectors.getDetectors())

        recyclerView = findViewById<RecyclerView>(R.id.detector_recycle_view).apply {
            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }

}

class MyAdapter(private val detectors: List<AdDetectable>) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View,
                       val title: TextView,
                       val subtitle: TextView,
                       val switch: SwitchCompat) : RecyclerView.ViewHolder(view)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.mod_active_detectors_view_element, parent, false)

        val title = view.findViewById<TextView>(R.id.det_title) as TextView
        val subtitle = view.findViewById<TextView>(R.id.det_subtitle) as TextView
        val switch = view.findViewById<TextView>(R.id.det_switch) as SwitchCompat

        return MyViewHolder(view, title, subtitle, switch)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = "> " + detectors[position].getMeta().title
        holder.subtitle.text = detectors[position].getMeta().description
        holder.switch.isChecked = detectors[position].getMeta().enabled
    }

    override fun getItemCount() = detectors.size
}
