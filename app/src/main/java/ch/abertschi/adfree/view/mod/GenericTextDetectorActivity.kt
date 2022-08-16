package ch.abertschi.adfree.view.mod

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import ch.abertschi.adfree.R
import ch.abertschi.adfree.model.TextRepositoryData
import org.jetbrains.anko.*


class GenericTextDetectorActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var presenter: GenericTextDetectorPresenter
    private lateinit var viewAdapter: DetectorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mod_text_detector)
        val textView = findViewById<TextView>(R.id.textdetector_activity_title)
        val text =
            "the <font color=#FFFFFF>text detector</font> flags a notification based on the presence of text."
        textView.text = Html.fromHtml(text)
        findViewById<ScrollView>(R.id.mod_text_scroll).scrollTo(0, 0)

        presenter = GenericTextDetectorPresenter(this, this)


        var viewManager = LinearLayoutManager(this)
        viewAdapter = DetectorAdapter(presenter.getData(), presenter)
        var recyclerView = findViewById<RecyclerView>(R.id.detector_recycle_view).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        findViewById<TextView>(R.id.det_title_text).setOnClickListener {
            presenter.addNewEntry()
        }
        findViewById<TextView>(R.id.det_subtitle_text).setOnClickListener {
            presenter.addNewEntry()
        }
        findViewById<TextView>(R.id.det_title_help).setOnClickListener {
            presenter.browseHelp()
        }
        findViewById<TextView>(R.id.det_subtitle_help).setOnClickListener {
            presenter.browseHelp()
        }
    }

    fun showOptionDialog(entry: TextRepositoryData) {

        val d = AlertDialog.Builder(this)
            .setTitle("Options")
            .setView(LayoutInflater.from(this).inflate(R.layout.delete_dialog, null))
            .setPositiveButton(android.R.string.yes) { dialog, which ->
                presenter.deleteEntry(entry)
            }
            .setNegativeButton(android.R.string.no) { dialog, which ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                it.dismiss()
            }
            .create()
            d.window?.setBackgroundDrawableResource(R.color.colorBackground)
        d.show()
    }

    fun insertData() {
        viewAdapter.notifyDataSetChanged();
    }

    private class DetectorAdapter(
        private val data: List<TextRepositoryData>,
        private val presenter: GenericTextDetectorPresenter
    ) :
        RecyclerView.Adapter<DetectorAdapter.MyViewHolder>(), AnkoLogger {

        class MyViewHolder(
            val view: View,
            val title: EditText,
            val subtitle: EditText,
            val more: ImageView,
            val sepView: View
        ) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MyViewHolder {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.mod_text_detector_view_element, parent, false)
            val title = view.findViewById(R.id.det_title) as EditText
            val subtitle = view.findViewById(R.id.det_subtitle) as EditText
            val more = view.findViewById<ImageView>(R.id.det_more) as ImageView
            val sep = view.findViewById<View>(R.id.mod_det_seperation)
            return MyViewHolder(view, title, subtitle, more, sep)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            var entry = data[position]
            holder.more.onClick { presenter.onMoreClicked(entry) }
            holder.title.setText(entry.packageName)
            holder.subtitle.setText(entry.content.joinToString(separator = "\n"))

            holder.title.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable) {
                    entry.packageName = s.toString()
                    presenter.updateEntry(entry)
                }
            })
            holder.subtitle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable) {
                    entry.content = s.toString().split("\n")
                    presenter.updateEntry(entry)
                }
            })

            holder.sepView.visibility =
                if (position == data.size - 1) View.INVISIBLE else View.VISIBLE
        }

        override fun getItemCount() = data.size
    }

}

