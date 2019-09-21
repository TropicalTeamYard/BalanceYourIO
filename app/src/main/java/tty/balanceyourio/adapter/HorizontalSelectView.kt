package tty.balanceyourio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_type_time_select.view.*
import tty.balanceyourio.R

class HorizontalSelectView :
    androidx.recyclerview.widget.RecyclerView.Adapter<HorizontalSelectView.ViewHolder>() {
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.time.text = timeList[p1]
    }

    private lateinit var context: Context
    private var timeList: ArrayList<String> = ArrayList()

    init {
        timeList.add("-")
        timeList.add("-")
        timeList.add("上午")
        timeList.add("中午")
        timeList.add("下午")
        timeList.add("晚上")
        timeList.add("-")
        timeList.add("-")
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        context = p0.context
        return ViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.item_type_time_select,
                p0,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return timeList.size
    }

    inner class ViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
        var time: TextView = v.time_select_text
    }
}