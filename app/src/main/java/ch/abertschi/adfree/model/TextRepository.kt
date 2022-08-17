package ch.abertschi.adfree.model

import android.content.Context
import android.content.SharedPreferences
import com.thoughtworks.xstream.XStream
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

data class TextRepositoryData(
    var packageName: String = "",
    var content: List<String> = ArrayList<String>(),
    var subTitleNull: Boolean = false,
    var _id: String = UUID.randomUUID().toString()
) {

    fun serializeToString(): String {
        return Companion.serializeToString(this)
    }

    companion object {
        val serial: XStream = XStream()

        fun serializeToString(data: TextRepositoryData): String {
            return serial.toXML(data)
        }

        fun deserialzeFromString(s: String?): TextRepositoryData {
            return serial.fromXML(s) as TextRepositoryData;
        }
    }
}


class TextRepository : AnkoLogger {
    private val context: Context
    private val ID_KEY: String = "k_"
    private val ID_KEYS: String = "keys"

    private var dataEntries: ArrayList<TextRepositoryData>

    private fun formatKey(id: String) = ID_KEY + "_" + id

    private var sharedPreferences: SharedPreferences

    constructor(context: Context, sharedPreferences: PreferencesFactory) {
        this.context = context
        this.sharedPreferences = sharedPreferences.getPreferences()
        dataEntries = deserializeAllEntries()
    }

    private fun getKeys(): MutableSet<String> {
        return sharedPreferences.getStringSet(ID_KEYS, HashSet<String>())
    }

    private fun getEntryByFormattedKey(key: String): TextRepositoryData? {
        var dataStr: String = sharedPreferences.getString(key, null) ?: return null
        return TextRepositoryData.deserialzeFromString(dataStr)
    }

    private fun deserializeAllEntries(): ArrayList<TextRepositoryData> {
        var entries = ArrayList<TextRepositoryData>()
        for (key in getKeys()) {
            var entry: TextRepositoryData? = getEntryByFormattedKey(key)
            if (entry != null) {
                entries.add(entry)
            }
        }
        return entries
    }

    fun getAllEntries(): ArrayList<TextRepositoryData> {
        return ArrayList(dataEntries)
    }

    fun createNewEntry(): TextRepositoryData {
        var d = TextRepositoryData()
        dataEntries.add(d)
        return d
    }

    fun updateEntry(data: TextRepositoryData) {
        if (!dataEntries.contains(data)) {
            throw IllegalStateException("data entry not known")
        }
        var key = formatKey(data._id)
        info("storing text: " + key)
        info("storing text: " + data)

        var keys = getKeys()
        keys.add(key)
        setAllKeys(keys)
        sharedPreferences.edit().putString(key, data.serializeToString()).apply()
    }

    fun removeEntry(data: TextRepositoryData) {
        if (!dataEntries.contains(data)) {
            return
        }
        dataEntries.remove(data)

        var key = formatKey(data._id)
        val keys = getKeys()
        keys.remove(key)
        setAllKeys(keys)
        sharedPreferences.edit().remove(key).apply()
    }

    private fun setAllKeys(keys: Set<String>) {
        sharedPreferences.edit().putStringSet(ID_KEYS, keys).apply()
    }
}