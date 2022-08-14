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


class GenericTextDetectorActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var presenter: CategoriesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mod_active_detectors)

        val textView = findViewById<TextView>(R.id.detectors_activity_title)
        val text =
            "configure <font color=#FFFFFF>find ads</font>. " +
                    "choose what's active."
        textView.text = Html.fromHtml(text)

//        presenter = CategoriesPresenter(this)

        findViewById<ScrollView>(R.id.mod_active_scroll).scrollTo(0, 0)
        findViewById<TextView>(R.id.detectors_activity_title).onClick { presenter.onTabTitle() }
    }
}