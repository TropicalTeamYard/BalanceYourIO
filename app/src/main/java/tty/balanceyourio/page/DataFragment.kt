package tty.balanceyourio.page

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener.*
import android.widget.AdapterView
import android.widget.ExpandableListView
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_data.*
import tty.balanceyourio.R
import tty.balanceyourio.adapter.ShowBillListAdapter
import tty.balanceyourio.interfaces.BillRecordDeleted
import tty.balanceyourio.model.BillRecord
import tty.balanceyourio.model.IOType
import tty.balanceyourio.util.DateConverter
import tty.balanceyourio.util.NumberFormatter


class DataFragment : Fragment(),
    ExpandableListView.OnChildClickListener,
    AdapterView.OnItemLongClickListener,
    ExpandableListView.OnGroupClickListener,
    DialogInterface.OnDismissListener {

    private lateinit var adapter:ShowBillListAdapter
    private var currentId = 0
    private var cGroupP = 0
    private var cChildrenP = 0
    private lateinit var billRecordDeleted: BillRecordDeleted
    private var lastCatX = 0
    private var lastCatY = 0
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var isLastHide = false
    private var catL = 0
    private var catR = 0
    private var catT = 0
    private var catB = 0
    private lateinit var catLayoutParams: CoordinatorLayout.LayoutParams

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is MainActivity){
            billRecordDeleted = context
        }
    }

    //region 对父类的重写方法
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screenWidth=resources.displayMetrics.widthPixels
        screenHeight=resources.displayMetrics.heightPixels
        fab_add_bill_record.setOnClickListener {
            startActivity(Intent(this.context, AddBillActivity::class.java))
        }

        catLayoutParams=CoordinatorLayout.LayoutParams(NumberFormatter.dp2px(context!!, 120F), NumberFormatter.dp2px(context!!, 120F))

        layout_data_month_overview.setOnTouchListener { v, event ->
            screenWidth=resources.displayMetrics.widthPixels
            screenHeight=resources.displayMetrics.heightPixels
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    data_month_outcome.visibility=View.VISIBLE
                    data_month_budget.visibility=View.VISIBLE
                    overview_cat.setImageDrawable(resources.getDrawable(R.drawable.cat_1, null))
                    lastCatX=event.rawX.toInt()
                    lastCatY=event.rawY.toInt()
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx=(event.rawX - lastCatX).toInt()
                    val dy=(event.rawY - lastCatY).toInt()
                    catL=v.left+dx
                    catR=v.right+dx
                    catT=v.top+dy
                    catB=v.bottom+dy
                    if(catB > screenHeight){
                        catB = screenHeight
                        catT = catB - v.height
                    }
                    if (catT < 0) {
                        catT = 0
                        catB = catT + v.height
                    }
                    if (catL < 0) {
//                        if(isLastHide){
//                            catL=0
//                            catR = catL + v.width
//                            isLastHide=false
//                        } else {
//                            catL = -NumberFormatter.dp2px(context!!, 65F)
//                            catR = catL + v.width
//                            isLastHide=true
//                        }
                        catL = 0
                        catR = catL + v.width
                    }
                    if (catR > screenWidth) {
                        catR = screenWidth
                        catL = catR - v.width
                    }

                    v.layout(catL, catT, catR, catB)
                    lastCatX = event.rawX.toInt()
                    lastCatY = event.rawY.toInt()
                    v.postInvalidate()
                }

                MotionEvent.ACTION_UP -> {
                    if(catR > screenWidth - NumberFormatter.dp2px(context!!, 80F) && catB > screenHeight - NumberFormatter.dp2px(context!!, 80F)) {
                        catR = screenWidth - NumberFormatter.dp2px(context!!, 80F)
                        catL = catR - v.width
                        catB = screenHeight - NumberFormatter.dp2px(context!!, 120F)
                        catT = catB - v.height
                    }
                    if(catB >= screenHeight - NumberFormatter.dp2px(context!!, 48F)){
                        catB = screenHeight
                        catT = screenHeight - v.height
                    } else {
                        if((catL + v.width/2)<screenWidth/2){
                            catL = -NumberFormatter.dp2px(context!!, 65F)
                            catR = catL + v.width
                            overview_cat.setImageDrawable(resources.getDrawable(R.drawable.cat_left_0, null))
                            overview_cat.scaleType=ImageView.ScaleType.FIT_END
                        } else {
                            catR = screenWidth + NumberFormatter.dp2px(context!!, 65F)
                            catL = catR - v.width
                            overview_cat.setImageDrawable(resources.getDrawable(R.drawable.cat_right_0, null))
                            overview_cat.scaleType=ImageView.ScaleType.FIT_START
                        }

                        data_month_outcome.visibility=View.GONE
                        data_month_budget.visibility=View.GONE
                    }



                    v.layout(catL, catT, catR, catB)
                    v.postInvalidate()
                    catLayoutParams.leftMargin = v.left
                    catLayoutParams.topMargin = v.top
                    catLayoutParams.rightMargin = v.right
                    catLayoutParams.topMargin = v.top
                    v.layoutParams=catLayoutParams
                }
            }
            Log.d(TAG, "ldmo on touch")
            true
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("DF", "DF Resume")
        setAdapter()
    }
    //endregion

    //region 与视图的交互
    override fun onGroupClick(parent: ExpandableListView?, v: View?, groupPosition: Int, id: Long): Boolean {
        return true
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        val packedPosition = elv_show_bill_data.getExpandableListPosition(position)
        val groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition)
        val childPosition = ExpandableListView.getPackedPositionChild(packedPosition)
        return true
    }

    override fun onChildClick(parent: ExpandableListView?, v: View?, groupPosition: Int, childPosition: Int, id: Long): Boolean {
        val detail=BillDetailFragment()
        val bill: BillRecord=adapter.getChild(groupPosition, childPosition) as BillRecord
        val bundle=Bundle()
        bundle.putInt("id",bill.id)
        currentId = bill.id
        cGroupP = groupPosition
        cChildrenP = childPosition
        bundle.putString("goodstype",bill.goodsType)
        bundle.putString("date", DateConverter.getSimpleString(bill.time!!))
        bundle.putDouble("money", bill.amount)
        bundle.putString("comment", bill.remark)
        bundle.putInt("iotype",
            when (bill.ioType){
                IOType.Income-> 1
                IOType.Outcome -> 2
                else -> 0
            }
        )
        detail.arguments=bundle
        detail.setOnDismissListener(this)
        detail.show(this.fragmentManager, "BDF")
        return true
    }

    override fun onDismiss(dialog: DialogInterface?) {

        try{
            /**
             * DONE("修复删除只剩一项之后的闪退问题")
             */
            //TODO("将删除项封装到Adapter里")
            Log.d(TAG,"count::${adapter.dateList.count()}")
            if (adapter.getChildrenCount(cGroupP) <= 1){
                adapter.dateList.removeAt(cGroupP)
                adapter.billList.removeAt(cGroupP)
                adapter.daySumList.removeAt(cGroupP)
            } else {
                val a = adapter.billList[cGroupP][cChildrenP]
                when(a.ioType){
                    IOType.Outcome->{
                        adapter.daySumList[cGroupP][IOType.Outcome] = adapter.daySumList[cGroupP][IOType.Outcome]!! - a.amount
                    }
                    IOType.Income ->{
                        adapter.daySumList[cGroupP][IOType.Income] = adapter.daySumList[cGroupP][IOType.Income]!! - a.amount
                    }
                    else ->{}
                }
                adapter.billList[cGroupP].removeAt(cChildrenP)
            }


            Log.d(TAG,"newCount::${adapter.dateList.count()}")

            adapter.notifyDataSetChanged()

            billRecordDeleted.notifyUpdate()

            fab_add_bill_record.show()
//            layout_data_month_overview.visibility=View.VISIBLE

        } catch (e:Exception){
            e.printStackTrace()
        }

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
                            fab_add_bill_record.hide()
//                            layout_data_month_overview.visibility=View.GONE
                        }
                        firstVisibleItem < lastVisibleItemPosition -> {
//                            Log.i(TAG, "onScroll: -------->down")
                            fab_add_bill_record.show()
//                            layout_data_month_overview.visibility=View.VISIBLE
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

    //endregion

    companion object{
        const val TAG = "DF"
    }
}
