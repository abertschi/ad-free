package ch.abertschi.adfree.exceptionhandler


import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.widget.TextView
import ch.abertschi.adfree.R
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

import android.os.Build
import android.os.Environment.getExternalStorageDirectory

import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader


// capture logs on app crash
// https://stackoverflow.com/questions/19897628/need-to-handle-uncaught-exception-and-send-log-file

class SendLog : AppCompatActivity(), View.OnClickListener {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        "<font color=#FFFFFF>courage</font> to continue that counts. -- " +
                        "Winston Churchill <br/><br/>" +
                        "<font color=#FFFFFF>ad-free</font> crashed. help to continue and " +
                        "send the <font color=#FFFFFF>crash report.</font>"

        title?.text = Html.fromHtml(text)
        val f = extractLogToFile()
    }

    override fun onClick(v: View) {
        // respond to button clicks in your UI
        val f = extractLogToFile()
        println(f)
        Toast.makeText(this, f, Toast.LENGTH_LONG)
    }

    fun extractLogToFile(): String
    {
        val manager = this.packageManager
        var info: PackageInfo? = null
        try {
            info = manager.getPackageInfo(this.packageName, 0)
        } catch (e2: PackageManager.NameNotFoundException) {
        }

        var model = Build.MODEL
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model

        println(model)

        // Make file name - file must be saved to external storage or it wont be readable by
        // the email app.
        val path = Environment.getExternalStorageDirectory().absolutePath + "/" + ""
        val fullName = path + "error.txt"

        // Extract to file.
        val file = File(fullName)
        var reader: InputStreamReader? = null
        var writer: FileWriter? = null
        try {
            // For Android 4.0 and earlier, you will get all app's log output, so filter it to
            // mostly limit it to your app's output.  In later versions, the filtering isn't needed.
            val cmd = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
                "logcat -d -v time MyApp:v dalvikvm:v System.err:v *:s"
            else
                "logcat -d -v time"

            // get input stream
            val process = Runtime.getRuntime().exec(cmd)
            process.inputStream.toString()
            reader = InputStreamReader(process.inputStream)

            // write output stream
            writer = FileWriter(file)
            writer!!.write("Android version: " + Build.VERSION.SDK_INT + "\n")
            writer!!.write("Device: $model\n")
            writer!!.write("App version: " + (info?.versionCode ?: "(null)") + "\n")

            val buffer = CharArray(10000)
            do {
                val n = reader!!.read(buffer, 0, buffer.size)
                if (n == -1)
                    break
                writer!!.write(buffer, 0, n)
            } while (true)

            reader!!.close()
            writer!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
            if (writer != null)
                try {
                    writer!!.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }

            if (reader != null)
                try {
                    reader!!.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }

            // You might want to write a failure message to the log here.
        }


        return fullName

    }




    private fun sendLogFile() {
        // method as shown above
    }


//    private fun extractLogToFile(): String {
//        // method as shown above
//    }

}