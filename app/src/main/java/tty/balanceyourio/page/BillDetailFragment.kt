package tty.balanceyourio.page


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_bill_detail.*
import tty.balanceyourio.R
import tty.balanceyourio.adapter.AddBillIconAdapter
import tty.balanceyourio.converter.PxlIconConverter
import tty.balanceyourio.data.BYIOCategory


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
        return inflater.inflate(R.layout.fragment_bill_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments!!.getString("goodstype", "")
        val id = arguments!!.getInt("id", -1)
        val date = arguments!!.getString("date", "")
        val money = arguments!!.getDouble("money", 0.0)
        val comment = arguments!!.getString("comment",  "")
        val iotype = arguments!!.getInt("iotype", 0)
        val typeString: String
        if(type.isNotEmpty()){
            typeString=AddBillIconAdapter.getFriendString(this.context!!, type)
        } else {
            typeString=""
            dismiss()
        }
        //detail_image.setImageResource()  To be filled by wcf
        try {
            val iconIndex = BYIOCategory.getInstance().getIconIndex(type);
            detail_image.setImageResource(PxlIconConverter().getResID(iconIndex));
        } catch (e:Exception){
            //DONE CHT 将索引转换为具体内容
            detail_image.setImageResource(R.drawable.type_others)
        }
        detail_type.text=typeString
        detail_time.text=date
        detail_money.text=money.toString()
        if (comment == resources.getString(R.string.blank)) {
            detail_comment.visibility = View.GONE
        } else {
            detail_comment.text = "${resources.getString(R.string.comment)}$comment"
        }
        detail_money.setTextColor(when(iotype){
            1->context!!.getColor(R.color.typeIncome)
            2->context!!.getColor(R.color.typeOutcome)
            else->context!!.getColor(R.color.typeOthers)
        })

        detail_edit.setOnClickListener {
            val intent = Intent(this.context, AddBillActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("update", 1)
            bundle.putInt("id", id)
//            bundle.putDouble("money", money)
//            bundle.putString("goodstype", type)
//            bundle.putInt("iotype", iotype)
//            bundle.putString("date", date)
//            bundle.putString("comment", comment)
            intent.putExtras(bundle)
            startActivity(intent)
            Toast.makeText(this.context, "EDIT", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        detail_delete.setOnClickListener {
//            deleteRecord.setDeleteId(id)
            Toast.makeText(this.context, "DELETE", Toast.LENGTH_SHORT).show()
        }
    }
}
