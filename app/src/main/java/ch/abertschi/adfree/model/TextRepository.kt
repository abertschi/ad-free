package ch.abertschi.adfree.model

import android.content.Context
import android.content.SharedPreferences
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class TextRepository : AnkoLogger {

    private val context: Context
    private val ID_KEY: String = "text_repository"
    private val ID_KEYS: String = "keys_text_repository"

    private fun formatKey(id: String) = ID_KEY + "_" + id

    private var sharedPreferences: SharedPreferences

    constructor(context: Context, sharedPreferences: PreferencesFactory) {
        this.context = context
        this.sharedPreferences = sharedPreferences.getPreferences()
    }

    private fun getKeys(): MutableSet<String> {
        return sharedPreferences.getStringSet(ID_KEYS, HashSet<String>())
    }

    fun getEntry(key: String): MutableSet<String> {
        var id = formatKey(key)
        return sharedPreferences.getStringSet(id, HashSet<String>())
    }

    fun addOrUpdateEntry(key: String, content: Set<String>) {
        info("storing text: " + key)
        info("storing text: " + content)

        var keys = getKeys()
        keys.add(key)
        setAllKeys(keys)

        var id = formatKey(key)
        sharedPreferences.edit().putStringSet(id, content).apply()
    }

    fun removeEntry(key: String) {
        val keys = getKeys()
        keys.remove(key)
        sharedPreferences.edit().putStringSet(ID_KEYS, keys).apply()

        var id = formatKey(key)
        sharedPreferences.edit().remove(id).apply()
    }

    fun getAllKeys(): Set<String> {
        return getKeys()
    }

    private fun setAllKeys(keys: Set<String>) {
        sharedPreferences.edit().putStringSet(ID_KEYS, keys).apply()
    }

    fun deleteEntry(key: String) {
        val keys = getKeys()
        keys.remove(key)
        setAllKeys(keys)

        var id = formatKey(key)
        sharedPreferences.edit().putStringSet(id, null).apply()
    }
}