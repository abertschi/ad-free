/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.model

import android.content.Context
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.json.JSONArray
import java.io.IOException
import java.nio.charset.Charset


/**
 * Created by abertschi on 01.09.17.
 */
class YesNoModel(val context: Context) : AnkoLogger {

    var yes: List<String> = listOf()
    var no: List<String> = listOf()

    init {
        yes = loadJSONFromAsset("yes.json")
        no = loadJSONFromAsset("no.json")
    }

    fun getRandomYes(): String {
        return yes[(Math.random() * yes.size).toInt()]
    }

    fun getRandomNo(): String {
        return no[(Math.random() * no.size).toInt()]
    }

    fun loadJSONFromAsset(assetLocation: String): List<String> {
        var json: String? = null
        try {

            val stream = context.assets.open(assetLocation)
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            json = buffer.toString(Charset.defaultCharset())
            info(json)
            val words = JSONArray(json)
            var result = ArrayList<String>()
            (0 until words.length()).mapTo(result) { words[it] as String }
            return result
        } catch (ex: IOException) {
            ex.printStackTrace()
            return listOf("")
        }
    }
}