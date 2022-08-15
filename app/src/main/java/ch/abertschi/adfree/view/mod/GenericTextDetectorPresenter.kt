package ch.abertschi.adfree.view.mod

import android.content.Context
import android.content.Intent
import android.net.Uri
import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.model.TextRepository
import ch.abertschi.adfree.model.TextRepositoryData

class GenericTextDetectorPresenter(val ctx: Context, val view: GenericTextDetectorActivity) {
    private var data: ArrayList<TextRepositoryData>
    private var textRepository: TextRepository

    init {
        var app = ctx.applicationContext as AdFreeApplication
        textRepository = app.textRepository
        data = textRepository.getAllEntries()
    }

    fun getData(): List<TextRepositoryData> {
        return data;
    }

    fun addNewEntry(): TextRepositoryData {
        var d = textRepository.createNewEntry()
        data.add(d)
        textRepository.updateEntry(d)
        view.insertData()
        return d
    }

    fun updateEntry(d: TextRepositoryData) {
        textRepository.updateEntry(d)
    }

    fun deleteEntry(d: TextRepositoryData) {
        textRepository.removeEntry(d)
        data.remove(d)
        view.insertData()
    }

    fun browseHelp() {
        val url = "https://abertschi.github.io/ad-free/troubleshooting/troubleshooting.html#generic-text-detector"
        val browserIntent = Intent(Intent.ACTION_VIEW,
            Uri.parse(url))
        this.view.startActivity(browserIntent)
    }
}