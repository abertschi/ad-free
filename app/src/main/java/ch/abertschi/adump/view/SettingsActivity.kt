package ch.abertschi.adump.view

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.widget.TextView
import ch.abertschi.adump.R

/**
 * Created by abertschi on 21.04.17.
 */

class SettingsActivity : AppCompatActivity() {

    lateinit var mTypeFace: Typeface
    private var mSettingsTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.music_setting)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.parseColor("#252A2E")
        }

        mTypeFace = Typeface.createFromAsset(assets, "fonts/Raleway-ExtraLight.ttf")
        mSettingsTitle = findViewById(R.id.settingsTitle) as TextView
        mSettingsTitle?.typeface = mTypeFace
        val text = "what do you want to do while <font color=#FFFFFF>ads </font>are <font color=#FFFFFF>being played ?</font>"
        mSettingsTitle?.text = Html.fromHtml(text)
    }
}
