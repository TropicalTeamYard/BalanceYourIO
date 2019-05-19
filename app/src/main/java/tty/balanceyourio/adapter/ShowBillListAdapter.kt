package tty.balanceyourio.adapter

import android.content.Context
import android.graphics.Color
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ShowBillListAdapter(var context: Context) : BaseExpandableListAdapter() {

    private var billList: ArrayList<ArrayList<BillRecord>> = ArrayList()
    private var dateList: ArrayList<String> = ArrayList()

    init{
        val allBillRecord = BYIOHelper(context).getBill()
        if(allBillRecord.size==0){
            //TODO 记录数为0时
        } else {
            val dateSet=HashSet<String>()
            for(i in 0 until allBillRecord.size){
                dateSet.add(SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(allBillRecord[i].time))
            }
            dateList= ArrayList(dateSet)
            dateList.sort()
            dateList.reverse()
            for(i in 0 until dateList.size){
                val tData=ArrayList<BillRecord>()
                for(j in 0 until allBillRecord.size){
                    if(SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(allBillRecord[j].time) == dateList[i]){
                        tData.add(allBillRecord[j])
                    }
                }

                tData.sortByDescending { it.time }
//                tData.reverse()
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
            view.tag=viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ParentViewHolder
        }

        viewHolder.date.text= SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format((getChild(groupPosition,0) as BillRecord).time)

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

        try {
            //viewHolder.icon.setImageResource(BYIOCategory.getInstance().getIconIndex((getChild(groupPosition, childPosition) as BillRecord).goodsType!!));
            val goodsType = (getChild(groupPosition,childPosition) as BillRecord).goodsType!!
            Log.d("Adapter",goodsType);
            val iconIndex = BYIOCategory.getInstance().getIconIndex(goodsType);
            viewHolder.icon.setImageResource(PxlIconConverter().getResID(iconIndex));
        } catch (e:Exception){
            //DONE CHT 将索引转换为具体内容
            viewHolder.icon.setImageResource(R.drawable.ic_color_tag)
        }


        when((getChild(groupPosition, childPosition) as BillRecord).ioType){
            IOType.Income->{
                viewHolder.money.text="+"
                viewHolder.money.setTextColor(Color.GREEN)
            }
            IOType.OutCome-> {
                viewHolder.money.text="-"
                viewHolder.money.setTextColor(Color.RED)
            }
            else -> {
                viewHolder.money.text=""
                viewHolder.money.setTextColor(Color.GRAY)
            }
        }
        viewHolder.comment.text = "备注："
        viewHolder.comment.append((getChild(groupPosition, childPosition) as BillRecord).remark)
        viewHolder.money.append((getChild(groupPosition, childPosition) as BillRecord).amount.toString()+" 元")
        viewHolder.type.text = getFriendString(context, (getChild(groupPosition, childPosition) as BillRecord).goodsType!!)


        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return billList.size
    }

    companion object{
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
            lateinit var date:TextView
        }

        class ChildViewHolder{
            lateinit var icon: ImageView
            lateinit var type: TextView
            lateinit var comment: TextView
            lateinit var money: TextView
        }
    }
}
