package tty.balanceyourio.page

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import tty.balanceyourio.R
import tty.balanceyourio.adapter.MainFragmentAdapter
import tty.balanceyourio.interfaces.BillRecordDeleted

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    androidx.viewpager.widget.ViewPager.OnPageChangeListener, BillRecordDeleted {
    override fun notifyUpdate() {
//        Log.d(TAG, "interface notifyUpdate")
        val analysisFragment: AnalysisFragment = adapter?.getItem(1) as AnalysisFragment
        analysisFragment.updateData()
    }


    //TODO @WCF 预算功能设置 可灵活设置日期
    //TODO @WCF 新建账本 自定义

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        return when (p0.itemId) {
            R.id.item_nav_record -> {
                main_viewPager.currentItem = 0
                main_toolbar.title = "数据"
                true
            }
            R.id.item_nav_analysis -> {
                main_viewPager.currentItem = 1
                main_toolbar.title = "分析"
                true
            }
            R.id.item_nav_user -> {
                main_viewPager.currentItem = 2
                main_toolbar.title = "我的"
                true
            }
            else -> false
        }
    }

    override fun onPageScrollStateChanged(p0: Int) {

    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

    }

    override fun onPageSelected(p0: Int) {

    }

    private var adapter: MainFragmentAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)
        main_toolbar.title = "数据"
        supportActionBar?.elevation = 0F

        adapter = MainFragmentAdapter(supportFragmentManager)
        main_viewPager.adapter = adapter

        main_viewPager.addOnPageChangeListener(this)
        main_bottom_nav.setOnNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.item_setting -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> false
        }
    }

    companion object {
        const val TAG = "MA"
    }

}
