package tty.balanceyourio.page


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import kotlinx.android.synthetic.main.fragment_data.*
import tty.balanceyourio.R
import tty.balanceyourio.adapter.ShowBillListAdapter
import tty.balanceyourio.model.BillRecord
import java.text.SimpleDateFormat
import java.util.*

class DataFragment : Fragment(), ExpandableListView.OnChildClickListener {

    override fun onChildClick(parent: ExpandableListView?, v: View?, groupPosition: Int, childPosition: Int, id: Long): Boolean {
        val detail=BillDetailFragment()
        val bill: BillRecord=adapter.getChild(groupPosition, childPosition) as BillRecord
        val bundle=Bundle()
        bundle.putInt("id",bill.id)
        bundle.putString("type",bill.goodsType)
        bundle.putString("date", SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(bill.time))
        detail.arguments=bundle
        detail.show(this.fragmentManager, "BDF")
        return true
    }

    lateinit var adapter:ShowBillListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        f_data_fab_add.setOnClickListener {
            startActivity(Intent(this.context, AddBillActivity::class.java))
        }
        //setAdapter()
    }

    override fun onResume() {
        super.onResume()
        Log.d("DF", "DF resume")
        setAdapter()
    }

    private fun setAdapter(){
        adapter=ShowBillListAdapter(this.context!!)
        elv_show_bill_data.setAdapter(adapter)
        elv_show_bill_data.setGroupIndicator(null)

        try {
            elv_show_bill_data.expandGroup(0)
        } catch (e: IndexOutOfBoundsException){
            // TODO 当前没有记录的处理办法
        }

        elv_show_bill_data.setOnChildClickListener(this)
    }

}
