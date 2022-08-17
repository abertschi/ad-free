package ch.abertschi.adfree.view.mod

import android.content.Intent
import ch.abertschi.adfree.AdFreeApplication
import org.jetbrains.anko.AnkoLogger

class CategoriesPresenter(val view: CategoriesActivity) : AnkoLogger {

    companion object {
        const val BUNDLE_CATEGORY_KEY: String = "category"
    }

    private val detectorFactory = (view.applicationContext as AdFreeApplication).adDetectors
    private val prefs = (view.applicationContext as AdFreeApplication).prefs
    private var tabCounterForDebug = 0
    private val tabCounterForDebugThreshold = 5

    fun getCategories(): List<String> = detectorFactory.getVisibleCategories()

    fun onTabTitle() {
        tabCounterForDebug++
        if (tabCounterForDebug > tabCounterForDebugThreshold) {
            tabCounterForDebug = 0
            if (prefs.isDeveloperModeEnabled()) {
                prefs.setDeveloperMode(false)
                view.hideEnabledDebug()
            } else {
                prefs.setDeveloperMode(true)
                view.showEnabledDebug()
            }
        }
    }

    fun onCategorySelected(s: String) {
        val myIntent = Intent(this.view, ActiveDetectorActivity::class.java)
        myIntent.putExtra(BUNDLE_CATEGORY_KEY, s)
        this.view.startActivity(myIntent)
    }
}
