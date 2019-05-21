package tty.balanceyourio.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.item_type_in_out_come.view.*
import tty.balanceyourio.R
import tty.balanceyourio.converter.CategoryConverter
import tty.balanceyourio.converter.RConverter

/**
 * 选择输入输出类型图标的适配器
 * @param source 数据源，
 * @param rConverter index:int->resource:int 用于转换icon储存索引到资源的id
 */
class AddBillIconAdapter(var source:List<HashMap<String,Any>>, private var rConverter: RConverter): RecyclerView.Adapter<AddBillIconAdapter.ViewHolder>(){

    private lateinit var context:Context
    private var mClickListener:OnItemClickListener?=null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        context = p0.context
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_type_in_out_come,p0,false), mClickListener)
    }

    override fun getItemCount(): Int {
        return source.count()
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val iconRes:Int = rConverter.getResID( source[p1]["icon"] as Int)
        //查找并未发现该图标资源
        if (iconRes == -1){
            //TODO("检查图标资源索引。")
            p0.imageView.setImageResource(R.drawable.type_others)
        } else{
            p0.imageView.setImageResource(iconRes)
        }

        p0.textView.text = getFriendString(context ,source[p1]["class"] as String)

        try {
            if((source[p1]["chosen"] as Boolean?)!!){
                p0.imageView.setColorFilter(0xaaffff00.toInt())
            } else {
                p0.imageView.setColorFilter(0x00ffffff.toInt())
            }
        } catch (e: NullPointerException){

        }
    }

    interface OnItemClickListener:View.OnClickListener{
        fun onItemClick(v:View?,position:Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.mClickListener = listener
    }

    companion object {
        /**
         * 转换特殊字符串的工具函数
         * 仅适用于{@see AddBillIconAdapter}
         */
        fun getFriendString(context: Context,input:String):String{
            val value: String
            value = if (input.startsWith("key.")){
                val key:Int? =  input.substring(4).toIntOrNull()
                if (key != null){
                    context.getString(CategoryConverter().getResID(key))
                }
                else
                    "KeyNotFound"
            } else {
                input
            }
            return value
        }
    }

    inner class ViewHolder(v: View, private var listener:OnItemClickListener?):RecyclerView.ViewHolder(v),View.OnClickListener{
        override fun onClick(v: View?) {
            listener?.onItemClick(v,layoutPosition)
        }

        private var mListener: OnItemClickListener

        var imageView:ImageView = v.type_img
        var textView:TextView = v.type_class
        var linearLayout: LinearLayout = v.type_container!!

        init {
            v.setOnClickListener(this)
            mListener= this.listener!!
        }
    }
}