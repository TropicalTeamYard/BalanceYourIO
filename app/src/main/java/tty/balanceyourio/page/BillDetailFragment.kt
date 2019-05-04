package tty.balanceyourio.page


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
import android.view.*
import kotlinx.android.synthetic.main.fragment_bill_detail.*


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
        detail_type.text=""
    }

}
