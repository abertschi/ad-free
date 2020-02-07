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
import android.widget.ScrollView
import ch.abertschi.adfree.detector.AdDetectable
class ActiveDetectorActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var presenter: ActiveDetectorPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mod_active_detectors)

        val textView = findViewById<TextView>(R.id.detectors_activity_title)
        val text =
                "detectors <font color=#FFFFFF>find ads</font>. " +
                        "choose what's active."
        textView.text = Html.fromHtml(text)

        presenter = ActiveDetectorPresenter(this)


        findViewById<ScrollView>(R.id.mod_active_scroll).scrollTo(0, 0)
        findViewById<TextView>(R.id.detectors_activity_title).onClick { presenter.onTabTitle() }
        initRecycleView()
    }

    private fun initRecycleView() {
        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(presenter.getDetectors(), presenter)
        recyclerView = findViewById<RecyclerView>(R.id.detector_recycle_view).apply {
            layoutManager = viewManager
            adapter = viewAdapter

        }
    }

    fun hideEnabledDebug() {
        longToast("So Long, and Thanks for All the Fish")
        initRecycleView()
    }

    fun showEnabledDebug() {
        longToast("With great power comes great responsibility")
        initRecycleView()
    }

    fun showInfo(info: String) {
        longToast(info)
    }
}

class MyAdapter(private val detectors: List<AdDetectable>, private val presenter: ActiveDetectorPresenter) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>(), AnkoLogger {

    class MyViewHolder(val view: View,
                       val title: TextView,
                       val subtitle: TextView,
                       val switch: SwitchCompat,
                       val sepView: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.mod_active_detectors_view_element, parent, false)
        val title = view.findViewById(R.id.det_title) as TextView
        val subtitle = view.findViewById(R.id.det_subtitle) as TextView
        val switch = view.findViewById<TextView>(R.id.det_switch) as SwitchCompat
        val sep = view.findViewById<View>(R.id.mod_det_seperation)
        return MyViewHolder(view, title, subtitle, switch, sep)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = "> " + detectors[position].getMeta().title
        holder.subtitle.text = detectors[position].getMeta().description
//                " (${detectors[position].javaClass.simpleName})"
        holder.switch.isChecked = presenter.isEnabled(detectors[position])
        holder.switch.setOnCheckedChangeListener { _, isChecked ->
            presenter.onDetectorToggled(isChecked, detectors[position])
            info(detectors[position].javaClass.canonicalName)
        }
        holder.sepView.visibility =
                if (position == detectors.size - 1) View.INVISIBLE else View.VISIBLE
    }
    override fun getItemCount() = detectors.size
}
