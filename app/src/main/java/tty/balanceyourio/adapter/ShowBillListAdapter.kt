package tty.balanceyourio.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import tty.balanceyourio.R
import tty.balanceyourio.converter.CategoryConverter
import tty.balanceyourio.converter.PxlIconConverter
import tty.balanceyourio.data.BYIOCategory
import tty.balanceyourio.data.BYIOHelper
import tty.balanceyourio.model.BillRecord
import tty.balanceyourio.model.IOType
import tty.balanceyourio.util.DateConverter
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ShowBillListAdapter(var context: Context) : BaseExpandableListAdapter() {

    var billList: ArrayList<ArrayList<BillRecord>> = ArrayList()
    var dateList: ArrayList<Date> = ArrayList()
    var daySumList: ArrayList<HashMap<IOType, Double>> = ArrayList()
    private val decimalFormat=DecimalFormat("#.##")

    init{
        val allBillRecord = BYIOHelper(context).getBill()
        if(allBillRecord.size==0){
            //TODO 记录数为0时
        } else {
            val dateSet=HashSet<Date>()
            for(i in 0 until allBillRecord.size){
                dateSet.add(DateConverter.cutToDate(allBillRecord[i].time!!))
                daySumList.add(HashMap())
                daySumList[i][IOType.Income]=0.0
                daySumList[i][IOType.Outcome]=0.0
                daySumList[i][IOType.Unset]=0.0
            }
            dateList= ArrayList(dateSet)

            dateList.sort()
            dateList.reverse()
            for(i in 0 until dateList.size){
                val tData=ArrayList<BillRecord>()
                for(j in 0 until allBillRecord.size){
                    if(DateConverter.equalDate( allBillRecord[j].time!!, dateList[i])){
                        tData.add(allBillRecord[j])
                        when(allBillRecord[j].ioType){
                            IOType.Income -> daySumList[i][IOType.Income] = daySumList[i][IOType.Income]!! + allBillRecord[j].amount
                            IOType.Outcome -> daySumList[i][IOType.Outcome] = daySumList[i][IOType.Outcome]!! + allBillRecord[j].amount
                            else -> daySumList[i][IOType.Unset] = daySumList[i][IOType.Unset]!! + allBillRecord[j].amount
                        }
                    }
                }

                tData.sortByDescending { it.time }
                billList.add(tData)
            }
        }

    }

    override fun getGroup(groupPosition: Int): Any {
        return billList[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ParentViewHolder
        if(convertView == null){
            view = LayoutInflater.from(this.context).inflate(R.layout.item_show_bill_parent, parent, false)
            viewHolder= ParentViewHolder()
            viewHolder.date=view.findViewById(R.id.show_bill_date)
            viewHolder.income=view.findViewById(R.id.show_bill_today_income)
            viewHolder.outcome=view.findViewById(R.id.show_bill_today_outcome)
            view.tag=viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ParentViewHolder
        }

        viewHolder.date.text = DateConverter.getFriendDateString(
        (getChild(groupPosition,0) as BillRecord).time!!)

        viewHolder.income.text = "${context.resources.getString(R.string.income)} ${decimalFormat.format(daySumList[groupPosition][IOType.Income])}"
        viewHolder.outcome.text = "${context.resources.getString(R.string.outcome)} ${decimalFormat.format(daySumList[groupPosition][IOType.Outcome])}"
        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return billList[groupPosition].size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return billList[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ChildViewHolder
        if(convertView == null){
            view = LayoutInflater.from(this.context).inflate(R.layout.item_show_bill_child, parent, false)
            viewHolder = ChildViewHolder()
            viewHolder.icon=view.findViewById(R.id.show_bill_icon)
            viewHolder.type=view.findViewById(R.id.show_bill_type)
            viewHolder.comment=view.findViewById(R.id.show_bill_comment)
            viewHolder.money=view.findViewById(R.id.show_bill_money)
            view.tag=viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ChildViewHolder
        }

        val billRecord = (getChild(groupPosition,childPosition) as BillRecord)

        try {
            val goodsType = billRecord.goodsType!!
            //Log.d("Adapter", goodsType);
            val iconIndex = BYIOCategory.getInstance().getIconIndex(goodsType);
            viewHolder.icon.setImageResource(PxlIconConverter().getResID(iconIndex));
        } catch (e:Exception){
            //DONE CHT 将索引转换为具体内容
            viewHolder.icon.setImageResource(R.drawable.type_others)
        }


        when(billRecord.ioType){
            IOType.Income->{
                viewHolder.money.text="+"
                viewHolder.money.setTextColor(context.getColor(R.color.typeIncome))
            }
            IOType.Outcome-> {
                viewHolder.money.text="-"
                viewHolder.money.setTextColor(context.getColor(R.color.typeOutcome))
            }
            else -> {
                viewHolder.money.text=""
                viewHolder.money.setTextColor(context.getColor(R.color.typeOthers))
            }
        }
        viewHolder.comment.text = context.resources.getString(R.string.comment)

        if (billRecord.remark == null || billRecord.remark == ""){
            viewHolder.comment.visibility = View.GONE
        } else {
            viewHolder.comment.visibility = View.VISIBLE
        }

        viewHolder.comment.append(billRecord.displayRemark)


        viewHolder.money.append(billRecord.amount.toString())
        viewHolder.type.text = getFriendString(context, billRecord.goodsType!!)


        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return billList.size
    }

    companion object{
        const val TAG = "SBLA"
        private fun getFriendString(context: Context, input:String):String{
            val value: String
            value = if (input.startsWith("key.")){
                val key:Int? =  input.substring(4).toIntOrNull()
                if (key != null)
                    context.getString(CategoryConverter().getResID(key))
                else
                    "KeyNotFound"
            } else {
                input
            }
            return value
        }

        class ParentViewHolder{
            lateinit var date: TextView
            lateinit var income: TextView
            lateinit var outcome: TextView
        }

        class ChildViewHolder{
            lateinit var icon: ImageView
            lateinit var type: TextView
            lateinit var comment: TextView
            lateinit var money: TextView
        }
    }
}
