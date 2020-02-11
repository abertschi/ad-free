package ch.abertschi.adfree

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class StartOnBootListener: BroadcastReceiver(), AnkoLogger {

    override fun onReceive(context: Context?, intent: Intent?) {
        info { "launching ad-free on boot. Hello world" }

        val app = context?.applicationContext as AdFreeApplication
        // launching ad-free application class on boot to initialize ad-free
        // see AdFreeApplication
        info { app }
    }
}