package ch.abertschi.adfree.crashhandler

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.MANUFACTURER
import android.os.Build.MODEL
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

/**
 * Capture app crashes and launch Activity to report error
 * @author abertschi
 */
class CrashExceptionHandler(val context: Context) : Thread.UncaughtExceptionHandler {

    @SuppressLint("SimpleDateFormat")
    override fun uncaughtException(t: Thread?, e: Throwable?) {
        e?.printStackTrace() // not all Android versions will print the stack trace automatically

        val (summary, logcat) = generateReport(e)
        val filename = writeLogfile(logcat)

        val i = Intent()
        i.action = SendCrashReportActivity.ACTION_NAME
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.putExtra(SendCrashReportActivity.EXTRA_LOGFILE, filename)
        i.putExtra(SendCrashReportActivity.EXTRA_SUMMARY, summary)
        context.startActivity(i)

        System.exit(1)
        exitProcess(1)
    }

    private fun writeLogfile(logcat: String): String {
        val time = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(Date())
        val filename = "adfree-crashlog-${time}.txt"

        val file = File(context.filesDir, filename)
        file.writeText(logcat)
        return filename
    }

    @SuppressLint("SimpleDateFormat")
    private fun generateReport(th: Throwable?): Pair<String, String> {
        val manager = context.packageManager
        var info: PackageInfo? = null
        try {
            info = manager.getPackageInfo(context.packageName, 0)
        } catch (e2: PackageManager.NameNotFoundException) {
        }

        var model = MODEL
        if (!model.startsWith(MANUFACTURER))
            model = "$MANUFACTURER $model"

        val summary = StringBuilder()
        summary.append("Android version: " + Build.VERSION.SDK_INT + "\n")
        summary.append("Device: $model\n")
        summary.append("App version: " + (info?.versionCode ?: "(null)") + "\n")
        summary.append("Time: " + SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(Date()) + "\n")
        summary.append("Root cause: \n" + Log.getStackTraceString(th) + "")

        val logcat = StringBuilder()
        logcat.append("Logcat messages: \n" + th?.message)
        logcat.append(readLogcat())
        return Pair(summary.toString(), logcat.toString())
    }

    private fun readLogcat(): String {
        val process = Runtime.getRuntime().exec("logcat -d")
        val bufferedReader = BufferedReader(
                InputStreamReader(process.inputStream))
        val log = bufferedReader.readText()
        return log
    }
}