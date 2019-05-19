package tty.balanceyourio.page


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener.*
import android.widget.AdapterView
import android.widget.ExpandableListView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_data.*
import tty.balanceyourio.R
import tty.balanceyourio.adapter.ShowBillListAdapter
import tty.balanceyourio.model.BillRecord
import tty.balanceyourio.model.IOType
import tty.balanceyourio.util.DateConverter


class DataFragment : Fragment(), ExpandableListView.OnChildClickListener, AdapterView.OnItemLongClickListener,
    ExpandableListView.OnGroupClickListener {
    override fun onGroupClick(parent: ExpandableListView?, v: View?, groupPosition: Int, id: Long): Boolean {
        return true
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        val packedPosition = elv_show_bill_data.getExpandableListPosition(position)
        val groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition)
        val childPosition = ExpandableListView.getPackedPositionChild(packedPosition)
        Log.d("DF", "position--$position")
        Log.d("DF", "packedPosition--$packedPosition")
        Log.d("DF", "groupPosition--$groupPosition")
        Log.d("DF", "childPosition--$childPosition")
        Toast.makeText(this.context, "P $position, G $groupPosition, C $childPosition", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onChildClick(parent: ExpandableListView?, v: View?, groupPosition: Int, childPosition: Int, id: Long): Boolean {
        val detail=BillDetailFragment()
        val bill: BillRecord=adapter.getChild(groupPosition, childPosition) as BillRecord
        val bundle=Bundle()
        bundle.putInt("id",bill.id)
        bundle.putString("type",bill.goodsType)
        bundle.putString("date", DateConverter.getSimpleString(bill.time!!))
        bundle.putString("money", bill.amount.toString())
        bundle.putString("comment", bill.remark)
        bundle.putInt("iotype",
            when (bill.ioType){
                IOType.Income-> 1
                IOType.Outcome -> 2
                else -> 0
            }
        )
        detail.arguments=bundle
        detail.show(this.fragmentManager, "BDF")
        return true
    }

    private lateinit var adapter:ShowBillListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        f_data_fab_add.setOnClickListener {
            startActivity(Intent(this.context, AddBillActivity::class.java))
        }
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

        // 默认展开所有项
        //TODO @HHR 完成COUNT为0时(没有子项时的显示文案)
        for (i in 0 until adapter.groupCount){
            elv_show_bill_data.expandGroup(i)
        }
        elv_show_bill_data.setOnGroupClickListener(this)
        elv_show_bill_data.setOnChildClickListener(this)
        elv_show_bill_data.onItemLongClickListener = this

        var scrollFlag = false;// 标记是否滑动
        var lastVisibleItemPosition = 0;// 标记上次滑动位置

        elv_show_bill_data.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (scrollFlag) {
                    when {
                        firstVisibleItem > lastVisibleItemPosition -> {
//                            Log.i(TAG, "onScroll: -------->up")
                            f_data_fab_add.hide()
                        }
                        firstVisibleItem < lastVisibleItemPosition -> {
//                            Log.i(TAG, "onScroll: -------->down")
                            f_data_fab_add.show()
                        }
                        else -> return
                    }
                    lastVisibleItemPosition = firstVisibleItem;
                }
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                when (scrollState) {
                    //when stop moving
                    SCROLL_STATE_IDLE-> {
                        scrollFlag = false

                        if (elv_show_bill_data.lastVisiblePosition == elv_show_bill_data.count - 1) {
//                            Log.d(TAG, "elv move to bottom")
                        }

                        if (elv_show_bill_data.firstVisiblePosition == 0) {
//                            Log.d(TAG, "elv move to top")
                        }
                    }
                    //when moving
                    SCROLL_STATE_TOUCH_SCROLL -> scrollFlag = true
                    //when moving inertial
                    SCROLL_STATE_FLING -> scrollFlag = false
                }
            }
        })
    }

    companion object{
        const val TAG = "DF"
    }
}
