package tty.balanceyourio.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import tty.balanceyourio.R
import tty.balanceyourio.model.BillRecord

class ShowBillListAdapter(var context: Context, private var billList: ArrayList<ArrayList<BillRecord>>) : BaseExpandableListAdapter() {
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

        val view: View;
        val viewHolder: ParentViewHolder
        if(convertView == null){
            view = View.inflate(context, R.layout.item_show_bill_child, parent)
            viewHolder= ParentViewHolder()
            viewHolder.date=view.findViewById(R.id.show_bill_date)
            view.tag=viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ParentViewHolder
        }

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
            view = View.inflate(context, R.layout.item_show_bill_parent, parent)
            viewHolder = ChildViewHolder()
            viewHolder.icon=view.findViewById(R.id.show_bill_icon)
            viewHolder.type=view.findViewById(R.id.show_bill_type)
            viewHolder.comment=view.findViewById(R.id.show_bill_comment)
            viewHolder.money=view.findViewById(R.id.show_bill_money)
            view.tag=viewHolder
        } else {
            view = convertView
            viewHolder=view.tag as ChildViewHolder
        }

        //viewHolder.icon.setImageResource()
        //TODO CHT 快来干活

        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return billList.size
    }

    companion object{
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
