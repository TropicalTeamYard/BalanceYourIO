package tty.balanceyourio.page


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
import android.view.*
import kotlinx.android.synthetic.main.fragment_bill_detail.*
import tty.balanceyourio.adapter.AddBillIconAdapter


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
        var type = arguments!!.getString("type", "")
        val id = arguments!!.getInt("id", -1)
        val date = arguments!!.getString("date", "")
        val money = arguments!!.getString("money", "")
        val comment = arguments!!.getString("comment",  "")
        val iotype = arguments!!.getInt("iotype", 0)
        if(type.isNotEmpty()){
            type=AddBillIconAdapter.getFriendString(this.context!!, type)
        } else {
            dismiss()
        }
        //detail_image.setImageResource()  To be filled by wcf
        detail_type.text=type
        detail_time.text=date
        detail_money.text=money
        if (comment == "（无）") {
            detail_comment.visibility = View.INVISIBLE
            detail_comment.height=0
        }
        else
            detail_comment.text=comment
        detail_money.setTextColor(when(iotype){
            1->Color.GREEN
            2->Color.RED
            else->Color.GRAY
        })

    }

}
