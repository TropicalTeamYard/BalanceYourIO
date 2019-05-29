package tty.balanceyourio.page

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener.*
import android.widget.AdapterView
import android.widget.ExpandableListView
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_data.*
import tty.balanceyourio.R
import tty.balanceyourio.adapter.ShowBillListAdapter
import tty.balanceyourio.data.BYIOHelper
import tty.balanceyourio.interfaces.BillRecordDeleted
import tty.balanceyourio.model.BillRecord
import tty.balanceyourio.model.GroupDateTuple
import tty.balanceyourio.model.IOType
import tty.balanceyourio.provider.BillRecordProvider
import tty.balanceyourio.util.DateConverter
import tty.balanceyourio.util.NumberFormatter
import java.util.*


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
    private var layoutWidth = 0
    private var layoutHeight = 0
    private var catL = 0
    private var catR = 0
    private var catT = 0
    private var catB = 0
    private lateinit var catParams: CoordinatorLayout.LayoutParams
    private var firstGroup: GroupDateTuple<Int?, Date?> = GroupDateTuple(null, null)

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
        fab_add_bill_record.setOnClickListener {
            startActivity(Intent(this.context, AddBillActivity::class.java))
        }

        catParams = CoordinatorLayout.LayoutParams(NumberFormatter.dp2px(context!!, 120F), NumberFormatter.dp2px(context!!, 120F))

        /**
         * 测量@layout_data_page的高度和宽度
         */
        layout_data_page.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if(layout_data_page.viewTreeObserver.isAlive){
                    layout_data_page.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    layoutWidth=layout_data_page.width
                    layoutHeight=layout_data_page.height
                }
            }
        })


        layout_data_month_overview.setOnTouchListener { v, event ->

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
                    if(catB > layoutHeight + NumberFormatter.dp2px(context!!, 48F)) {
                        catB = layoutHeight + NumberFormatter.dp2px(context!!, 48F)
                        catT = catB - v.height
                    }
                    if (catT < 0) {
                        catT = 0
                        catB = catT + v.height
                    }
                    if (catL < 0) {
                        catL = 0
                        catR = catL + v.width
                    }
                    if (catR > layoutWidth) {
                        catR = layoutWidth
                        catL = catR - v.width
                    }

                    v.layout(catL, catT, catR, catB)
                    lastCatX = event.rawX.toInt()
                    lastCatY = event.rawY.toInt()
                    v.postInvalidate()
                }

                MotionEvent.ACTION_UP -> {
                    if(catR > layoutWidth - NumberFormatter.dp2px(context!!, 80F) && catB > layoutHeight - NumberFormatter.dp2px(context!!, 80F)) {
                        catR = layoutWidth - NumberFormatter.dp2px(context!!, 80F)
                        catL = catR - v.width
                        catB = layoutHeight - NumberFormatter.dp2px(context!!, 80F)
                        catT = catB - v.height
                    }

                    if(catB > layoutHeight){
                        catB = layoutHeight + NumberFormatter.dp2px(context!!, 48F)
                        catT = catB - v.height
                    } else {
                        if((catL + v.width/2) < layoutWidth/2){
                            catL = -NumberFormatter.dp2px(context!!, 69F)
                            catR = catL + v.width
                            overview_cat.setImageDrawable(resources.getDrawable(R.drawable.cat_left_0, null))
                            overview_cat.scaleType=ImageView.ScaleType.FIT_END
                        } else {
                            catR = layoutWidth + NumberFormatter.dp2px(context!!, 69F)
                            catL = catR - v.width
                            overview_cat.setImageDrawable(resources.getDrawable(R.drawable.cat_right_0, null))
                            overview_cat.scaleType=ImageView.ScaleType.FIT_START
                        }

                        data_month_outcome.visibility=View.GONE
                        data_month_budget.visibility=View.GONE
                    }

                    v.layout(catL, catT, catR, catB)
                    v.postInvalidate()
                    catParams.leftMargin = v.left
                    catParams.topMargin = v.top
                    catParams.rightMargin = v.right
                    catParams.topMargin = v.top
                    v.layoutParams=catParams
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
                //判断是否所有Group都被删除了
                if(adapter.getGroupDate(0) == null) {
                    firstGroup = GroupDateTuple(null, null)
                    data_month_outcome.text = resources.getString(R.string.default_outcome)
                }
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

        } catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun setAdapter(){
        adapter=ShowBillListAdapter(this.context!!)
        elv_show_bill_data.setAdapter(adapter)
        elv_show_bill_data.setGroupIndicator(null)

        firstGroup.position = 0
        firstGroup.date = adapter.getGroupDate(0)
        if(firstGroup.position!=null){
            onMonthChange()
        }

        //默认展开所有项
        //TODO @HHR 完成COUNT为0时(没有子项时的显示文案)
        for (i in 0 until adapter.groupCount){
            elv_show_bill_data.expandGroup(i)
        }
        elv_show_bill_data.setOnGroupClickListener(this)
        elv_show_bill_data.setOnChildClickListener(this)
        elv_show_bill_data.onItemLongClickListener = this

        var scrollFlag = false //标记是否滑动
        var lastVisibleItemPosition = 0 //标记上次滑动位置

        elv_show_bill_data.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (scrollFlag) {
                    when {
                        firstVisibleItem > lastVisibleItemPosition -> {
                            //Log.i(TAG, "onScroll: -------->up")
                            fab_add_bill_record.hide()
                        }
                        firstVisibleItem < lastVisibleItemPosition -> {
                            //Log.i(TAG, "onScroll: -------->down")
                            fab_add_bill_record.show()
                        }
                        else -> return
                    }
                    lastVisibleItemPosition = firstVisibleItem
                }
                //判断月份是否有切换的逻辑

                val packedPosition = elv_show_bill_data.getExpandableListPosition(firstVisibleItem)
                val groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition)
//                val childPosition = ExpandableListView.getPackedPositionChild(packedPosition)
//                Log.d(TAG, "First POS: $firstVisibleItem, GROUP: $groupPosition, CHILD: $childPosition")
                if(firstGroup.date !=null && adapter.getGroupDate(groupPosition) != firstGroup.date){
                    //TODO(执行月份变化的判断)
                    if(firstGroup.position != null && adapter.getGroupDate(groupPosition) != null && !DateConverter.equalMonth(firstGroup.date!!, adapter.getGroupDate(groupPosition)!!)){
                        firstGroup.position = groupPosition
                        firstGroup.date = adapter.getGroupDate(groupPosition)
                        onMonthChange()
                    }

                }
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                when (scrollState) {
                    //when stop moving
                    SCROLL_STATE_IDLE-> {
                        scrollFlag = false

                        if (elv_show_bill_data.lastVisiblePosition == elv_show_bill_data.count - 1) {
                            //Log.d(TAG, "elv move to bottom")
                        }

                        if (elv_show_bill_data.firstVisiblePosition == 0) {
                            //Log.d(TAG, "elv move to top")
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

    private fun onMonthChange() {
        Log.d(TAG, "MONTH CHANGED: ${DateConverter.cutToMonth(firstGroup.date!!)}")
        val start = Calendar.getInstance()
        start.time = DateConverter.cutToMonth(firstGroup.date!!)
        val end = start.clone() as Calendar
        end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH))
        val info=BillRecordProvider.joinGroupByMonth(start.time, end.time, BYIOHelper(context!!).getBill(start.time, end.time))
        data_month_outcome.text="${resources.getString(R.string.outcome)}: ${NumberFormatter.decimalFormat2.format(info[0].outcomeSum)}"

    }

    //endregion

    companion object{
        const val TAG = "DF"
    }
}
