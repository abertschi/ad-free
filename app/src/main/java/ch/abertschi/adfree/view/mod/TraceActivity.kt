//package ch.abertschi.adfree.view.mod
//
//import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
//import android.text.Html
//
//import android.widget.TextView
//
//import android.view.LayoutInflater
//import ch.abertschi.adfree.R
//import org.jetbrains.anko.*
//
//import android.support.v7.widget.RecyclerView
//import android.view.ViewGroup
//
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.SwitchCompat
//import android.view.View
//import android.widget.ScrollView
//
//
//class TraceActivity : AppCompatActivity(), AnkoLogger {
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var viewAdapter: RecyclerView.Adapter<*>
//    private lateinit var viewManager: RecyclerView.LayoutManager
//
//    private lateinit var presenter: CategoriesPresenter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.mod_active_detectors)
//
//        val textView = findViewById<TextView>(R.id.detectors_activity_title)
//        val text =
//            "detectors <font color=#FFFFFF>find ads</font>. " +
//                    "choose what's active."
//        textView.text = Html.fromHtml(text)
//
//        presenter = CategoriesPresenter(this)
//
//        findViewById<ScrollView>(R.id.mod_active_scroll).scrollTo(0, 0)
//        findViewById<TextView>(R.id.detectors_activity_title).onClick { presenter.onTabTitle() }
//        initRecycleView()
//    }
//
//    private fun initRecycleView() {
//        viewManager = LinearLayoutManager(this)
//        viewAdapter = CategoryAdapter(presenter.getCategories(), presenter)
//        recyclerView = findViewById<RecyclerView>(R.id.detector_recycle_view).apply {
//            layoutManager = viewManager
//            adapter = viewAdapter
//
//        }
//    }
//
//    fun hideEnabledDebug() {
//        longToast("So Long, and Thanks for All the Fish")
//        initRecycleView()
//    }
//
//    fun showEnabledDebug() {
//        longToast("With great power comes great responsibility")
//        initRecycleView()
//    }
//}
//
//class CategoryAdapter(
//    private val cateogries: List<String>,
//    private val presenter: CategoriesPresenter
//) :
//    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(), AnkoLogger {
//
//    class CategoryViewHolder(
//        val view: View,
//        val title: TextView,
//        val subtitle: TextView,
//        val sepView: View
//    ) : RecyclerView.ViewHolder(view)
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): CategoryViewHolder {
//
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.mod_active_detectors_view_element, parent, false)
//        val title = view.findViewById(R.id.det_title) as TextView
//        val subtitle = view.findViewById(R.id.det_subtitle) as TextView
//        val sep = view.findViewById<View>(R.id.mod_det_seperation)
//        val switch = view.findViewById<TextView>(R.id.det_switch) as SwitchCompat
//        switch.visibility = View.GONE;
//        return CategoryViewHolder(view, title, subtitle, sep)
//    }
//
//    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
//        holder.view.setOnClickListener {
//            presenter.onCategorySelected(cateogries[position])
//        }
//        holder.title.setOnClickListener {
//            presenter.onCategorySelected(cateogries[position])
//        }
//        holder.subtitle.setOnClickListener {
//            presenter.onCategorySelected(cateogries[position])
//        }
//        holder.view.setOnClickListener {
//            presenter.onCategorySelected(cateogries[position])
//        }
//
//
//        holder.title.text = "> " + cateogries[position]
//        holder.subtitle.text = "configure detectors for " + cateogries[position]
//        holder.sepView.visibility =
//            if (position == cateogries.size - 1) View.INVISIBLE else View.VISIBLE
//    }
//
//    override fun getItemCount() = cateogries.size
//}
