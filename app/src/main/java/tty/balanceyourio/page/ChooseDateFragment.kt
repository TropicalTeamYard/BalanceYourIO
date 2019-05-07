package tty.balanceyourio.page


import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_choose_date.*
import tty.balanceyourio.R

class ChooseDateFragment : DialogFragment() {

    lateinit var sendDate: SendDate

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is AddBillActivity){
            sendDate= context
        } else {
            throw Exception("context should be ABA")
        }
    }

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(dm)
        dialog.window!!.setLayout(dm.widthPixels, dialog.window!!.attributes.height)
        val params: WindowManager.LayoutParams = dialog.window!!.attributes
        params.gravity= Gravity.CENTER
        dialog.window!!.attributes=params
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        choose_date_calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            Toast.makeText(this.context, "$year-${month+1}-$dayOfMonth", Toast.LENGTH_SHORT).show()
            this.dismiss()
        }
    }

    interface SendDate{
        fun getStr(date: String)
    }
}
