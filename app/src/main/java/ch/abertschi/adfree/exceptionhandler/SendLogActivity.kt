package ch.abertschi.adfree.exceptionhandler
import android.Manifest
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
import android.support.v4.app.ActivityCompat

class SendLogActivity : AppCompatActivity(), View.OnClickListener, AnkoLogger {

    companion object {
        val ACTION_NAME = "ch.abertschi.adfree.SEND_LOG_CRASH"
        val EXTRA_LOGFILE = "ch.abertschi.adfree.extra.logfile"
        val EXTRA_SUMMARY = "ch.abertschi.adfree.extra.summary"
        val MAIL_ADDR = "apps@abertschi.ch"
        val SUBJECT = "[ad-free-crash-report]"
    }

    var logfile: String? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            doOnCreate()
        } catch (e: Exception) {
            warn(e)
            Toast.makeText(this, "Error: $e", Toast.LENGTH_LONG).show()
        }
    }

    fun doOnCreate() {
        setupUI()
        logfile = intent?.extras?.getString(EXTRA_LOGFILE)
    }

    fun setupUI() {
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
                        "Winston Churchill <br/><br/>" +
                        "<font color=#FFFFFF>ad-free</font> crashed. help to continue and " +
                        "send the <font color=#FFFFFF>crash report.</font>"

        title?.text = Html.fromHtml(text)
    }

    override fun onClick(v: View) {

//        ActivityCompat.requestPermissions(this, Arrays.{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);



        logfile?.let {
            try {
                val file = File(filesDir, logfile)
                val log = file.readText()
                launchSendIntent(log)
            } catch (e: Exception) {
                warn {"cant send crash report"}
                warn { e }
                e.printStackTrace()
                Toast.makeText(this, "No crash report available.", Toast.LENGTH_LONG).show()
            }
        } ?: run {Toast.makeText(this, "No crash report available.", Toast.LENGTH_LONG).show()}
    }

    fun launchSendIntent(msg: String) {
        val sendIntent = Intent(Intent.ACTION_SEND)

        sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(MAIL_ADDR))
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg)
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT)
        sendIntent.type = "text/plain"
        this.startActivity(Intent.createChooser(sendIntent, "Choose an Email client"))

    }
}