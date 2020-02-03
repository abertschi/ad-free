package ch.abertschi.adfree.crashhandler

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.widget.TextView
import ch.abertschi.adfree.R
import android.widget.Toast
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn
import java.io.File
import java.lang.Exception
import android.content.Intent

// TODO: refator this into presenter and view
class SendCrashReportActivity : AppCompatActivity(), View.OnClickListener, AnkoLogger {

    companion object {
        val ACTION_NAME = "ch.abertschi.adfree.SEND_LOG_CRASH"
        val EXTRA_LOGFILE = "ch.abertschi.adfree.extra.logfile"
        val EXTRA_SUMMARY = "ch.abertschi.adfree.extra.summary"
        val MAIL_ADDR = "apps@abertschi.ch"
        val SUBJECT = "[ad-free-crash-report]"
    }

    private var logfile: String? = null
    private var summary: String? = null


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            parseIntent(this.intent)
            doOnCreate()
        } catch (e: Exception) {
            warn(e)
            Toast.makeText(this, "Error: $e", Toast.LENGTH_LONG).show()
        }
    }


    fun parseIntent(i: Intent?) {
        logfile = i?.extras?.getString(EXTRA_LOGFILE)
        summary = i?.extras?.getString(EXTRA_SUMMARY) ?: ""

    }

    fun sendReport() {
        try {
            val file = File(applicationContext.filesDir, logfile)
            val log = file.readText()
            launchSendIntent(summary!!)
        } catch (e: Exception) {

        }
    }

    private fun launchSendIntent(msg: String) {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(MAIL_ADDR))
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg)
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT)
        sendIntent.type = "text/plain"
        this.applicationContext
                .startActivity(Intent.createChooser(sendIntent, "Choose an Email client"))
    }


    private fun doOnCreate() {
        setupUI()
    }

    // TODO: Send logcat output and summary
    private fun setupUI() {
        setContentView(R.layout.crash_view)
        setFinishOnTouchOutside(false)
        val v = findViewById(R.id.crash_container) as View
        v.setOnClickListener(this)

        var typeFace: Typeface = Typeface.createFromAsset(baseContext.assets, "fonts/Raleway-ExtraLight.ttf")


        val title = findViewById(R.id.crash_Title) as TextView
        title.typeface = typeFace

        title.setOnClickListener(this)

        val text =
                "success is not final, failure is not fatal: it is the " +
                        "<font color=#FFFFFF>courage</font> to <font color=#FFFFFF>continue</font> that counts. -- " +
                        "Winston Churchill"

        title?.text = Html.fromHtml(text)

        val subtitle = findViewById(R.id.debugSubtitle) as TextView
        subtitle.typeface = typeFace

        subtitle.setOnClickListener(this)

        val subtitletext =
                "<font color=#FFFFFF>ad-free</font> crashed. be courageous and continue. " +
                        "send the <font color=#FFFFFF>crash report.</font>"


        subtitle.text = Html.fromHtml(subtitletext)
    }

    override fun onClick(v: View) {
        logfile?.let {
            try {
                sendReport()
            } catch (e: Exception) {
                warn { "cant send crash report" }
                warn { e }
                e.printStackTrace()
                Toast.makeText(this, "No crash report available.",
                        Toast.LENGTH_LONG).show()
            }
        } ?: run {
            Toast.makeText(this, "No crash report available.",
                    Toast.LENGTH_LONG).show()
        }
    }
}