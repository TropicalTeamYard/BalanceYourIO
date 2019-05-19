package tty.balanceyourio.page


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.fragment_bill_detail.*
import tty.balanceyourio.R
import tty.balanceyourio.adapter.AddBillIconAdapter
import tty.balanceyourio.converter.PxlIconConverter
import tty.balanceyourio.data.BYIOCategory
import tty.balanceyourio.model.BillRecord


class BillDetailFragment : DialogFragment() {

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(dm)
        dialog.window!!.setLayout(dm.widthPixels, dialog.window!!.attributes.height)
        val params: WindowManager.LayoutParams = dialog.window!!.attributes
        params.gravity=Gravity.BOTTOM
        dialog.window!!.attributes=params
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(tty.balanceyourio.R.layout.fragment_bill_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments!!.getString("type", "")
        val id = arguments!!.getInt("id", -1)
        val date = arguments!!.getString("date", "")
        val money = arguments!!.getString("money", "")
        val comment = arguments!!.getString("comment",  "")
        val iotype = arguments!!.getInt("iotype", 0)
        val s_type: String
        if(type.isNotEmpty()){
            s_type=AddBillIconAdapter.getFriendString(this.context!!, type)
        } else {
            s_type=""
            dismiss()
        }
        //detail_image.setImageResource()  To be filled by wcf
        try {
            val goodsType = type
            val iconIndex = BYIOCategory.getInstance().getIconIndex(goodsType);
            detail_image.setImageResource(PxlIconConverter().getResID(iconIndex));
        } catch (e:Exception){
            //DONE CHT 将索引转换为具体内容
            detail_image.setImageResource(R.drawable.type_others)
        }
        detail_type.text=s_type
        detail_time.text=date
        detail_money.text=money
        if (comment == "（无）") {
            detail_comment.visibility = View.GONE
        }
        else
            detail_comment.text = "备注：$comment"
        detail_money.setTextColor(when(iotype){
            1->context!!.getColor(R.color.typeIncome)
            2->context!!.getColor(R.color.typeOutcome)
            else->context!!.getColor(R.color.typeOthers)
        })

    }

}
