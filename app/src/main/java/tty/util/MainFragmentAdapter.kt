package tty.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import tty.balanceyourio.AnalysisFragment
import tty.balanceyourio.DataFragment

class MainFragmentAdapter(fm:FragmentManager): FragmentPagerAdapter(fm) {
    private var pages: ArrayList<Fragment> = ArrayList()

    init {
        pages.add(DataFragment())
        pages.add(AnalysisFragment())
    }
    override fun getCount(): Int {
        return pages.size
    }

    override fun getItem(p0: Int): Fragment? {
        return pages[p0]
    }

}