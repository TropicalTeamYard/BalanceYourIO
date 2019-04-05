package tty.util

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import tty.balanceyourio.R
import java.util.*

class AddBillRecyclerViewAdapter(private var data: List<HashMap<String, Any>>):RecyclerView.Adapter<AddBillRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<HashMap<String, Any>>){
        this.data=data
    }

    override fun onBindViewHolder(p0: AddBillRecyclerViewAdapter.ViewHolder, p1: Int) {
        p0.imageViewIcon.setImageBitmap(data[p1]["icon"] as Bitmap?)
        p0.textViewType.text= data[p1]["class"] as CharSequence?
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AddBillRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_in_out_come, p0, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageViewIcon:ImageView = itemView.findViewById(R.id.type_img)
        var textViewType: TextView = itemView.findViewById(R.id.type_class)
    }
}