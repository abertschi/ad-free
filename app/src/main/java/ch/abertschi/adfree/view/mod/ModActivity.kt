package ch.abertschi.adfree.view.mod

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ch.abertschi.adfree.R

class ModActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mod_activity)

        val textView = findViewById(R.id.modTitle) as TextView
        val text =
                "change how <font color=#FFFFFF>ad-free</font> " +
                        "internally works."
        textView.text = Html.fromHtml(text)
    }
}