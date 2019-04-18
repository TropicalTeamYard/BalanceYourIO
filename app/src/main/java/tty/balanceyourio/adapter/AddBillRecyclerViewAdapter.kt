package tty.balanceyourio.adapter

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import tty.balanceyourio.R
import java.lang.NullPointerException
import java.util.*

@Deprecated("请改用AddBillIconAdapter")
class AddBillRecyclerViewAdapter(private var data: List<HashMap<String, Any>>):RecyclerView.Adapter<AddBillRecyclerViewAdapter.ViewHolder>() {
    private var mClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mClickListener = listener
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface OnItemClickListener : View.OnClickListener {
        fun onItemClick(view: View, position: Int)
    }

    fun setData(data: List<HashMap<String, Any>>){
        this.data=data
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.imageViewIcon.setImageBitmap(data[p1]["icon"] as Bitmap?)
        p0.textViewType.text= data[p1]["class"] as CharSequence?
        try {
            if((data[p1]["chosen"] as Boolean?)!!){
                p0.imageViewIcon.setBackgroundColor(0x99ffff00.toInt())
            } else {
                p0.imageViewIcon.setBackgroundColor(0x99fafafa.toInt())
            }
        } catch (e: NullPointerException){

        }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_in_out_come, p0, false)
        return ViewHolder(view, mClickListener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            mListener.onItemClick(itemView, layoutPosition)
        }

        constructor(itemView: View, listener: OnItemClickListener?) : this(itemView){
            itemView.setOnClickListener(this)
            mListener= listener!!
        }

        private lateinit var mListener: OnItemClickListener
        var imageViewIcon: ImageView = itemView.findViewById(R.id.type_img)
        var textViewType: TextView = itemView.findViewById(R.id.type_class)
    }

}