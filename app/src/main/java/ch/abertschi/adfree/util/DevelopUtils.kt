/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.util

import org.jetbrains.anko.AnkoLogger

/**
 * Created by abertschi on 03.09.17.
 */
class DevelopUtils : AnkoLogger {

    fun serializeAndWriteToFile(o: Any, keyword: String = "") {
//
//        val serialized = XStream().toXML(o)
//        val time = System.currentTimeMillis()
//
////        val logFile = File(ContextCompat.getExternalFilesDirs(), time + "_" + keyword + "_log.file")
//        info { "serializing: " + o.javaClass.canonicalName + " " + keyword + " to " + logFile.absolutePath}
//        if (!logFile.exists()) {
//            try {
//                logFile.createNewFile()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//        }
//        try {
//            //BufferedWriter for performance, true to set append to file flag
//            val buf = BufferedWriter(FileWriter(logFile, false))
//            buf.write(serialized)
//            buf.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }

    }
}