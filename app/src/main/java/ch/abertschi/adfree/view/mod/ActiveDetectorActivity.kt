package ch.abertschi.adfree.view.mod

import android.opengl.Visibility
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
import java.lang.IllegalStateException

class ActiveDetectorActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var detectorRecyclerView: RecyclerView
    private lateinit var detectorViewAdapter: RecyclerView.Adapter<*>
    private lateinit var detectorViewManager: RecyclerView.LayoutManager

    private lateinit var presenter: ActiveDetectorPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mod_active_detectors)

        val textView = findViewById<TextView>(R.id.detectors_activity_title)

        presenter = ActiveDetectorPresenter(this)

        val category: String = intent.extras.getString(CategoriesPresenter.BUNDLE_CATEGORY_KEY)
            ?: throw  IllegalStateException("must set category")

        val text =
            "fine-tune detectors for <font color=#FFFFFF>$category</font>."
        textView.text = Html.fromHtml(text)


        findViewById<ScrollView>(R.id.mod_active_scroll).scrollTo(0, 0)

        detectorViewManager = LinearLayoutManager(this)
        detectorViewAdapter = DetectorAdapter(presenter.getDetectors(category), presenter)
        detectorRecyclerView = findViewById<RecyclerView>(R.id.detector_recycle_view).apply {
            layoutManager = detectorViewManager
            adapter = detectorViewAdapter

        }
    }

    fun showInfo(info: String) {
        longToast(info)
    }
}

class DetectorAdapter(
    private val detectors: List<AdDetectable>,
    private val presenter: ActiveDetectorPresenter
) :
    RecyclerView.Adapter<DetectorAdapter.MyViewHolder>(), AnkoLogger {

    class MyViewHolder(
        val view: View,
        val title: TextView,
        val subtitle: TextView,
        val switch: SwitchCompat,
        val sepView: View
    ) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

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
