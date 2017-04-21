package ch.abertschi.adump.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import ch.abertschi.adump.R
import ch.abertschi.adump.view.home.HomeActivity
import ch.abertschi.adump.view.setting.AboutActivity
import ch.abertschi.adump.view.setting.SettingsActivity



/**
 * Created by abertschi on 21.04.17.
 */

class MainActivity : FragmentActivity() {

    companion object {
        private val NUM_PAGES = 3
    }

    private var mPager: ViewPager? = null
    private var mPagerAdapter: PagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        mPager = findViewById(R.id.pager) as ViewPager
        mPagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        mPager!!.adapter = mPagerAdapter

        val tabLayout = findViewById(R.id.tabDots) as TabLayout
        tabLayout.setupWithViewPager(mPager, true)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.parseColor("#252A2E")
        }
    }


    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 -> return HomeActivity()
                1 -> return SettingsActivity()
                else -> return AboutActivity()
            }
        }

        override fun getCount(): Int {
            return NUM_PAGES
        }
    }
}
