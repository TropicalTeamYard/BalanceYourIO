package tty.balanceyourio.page


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_choose_date.*
import tty.balanceyourio.R
import tty.balanceyourio.adapter.HorizontalSelectView
import java.util.*

class ChooseDateFragment : DialogFragment(), View.OnClickListener {
    private val calendar:Calendar=Calendar.getInstance()
    private var chooseTime=0
    private var year:Int=calendar.get(Calendar.YEAR)
    private var month:Int=calendar.get(Calendar.MONTH)
    private var day:Int=calendar.get(Calendar.DATE)
    private lateinit var recSelectTime: RecyclerView
    private lateinit var selectTimeAdapter: HorizontalSelectView

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.choose_date_morning->{
                choose_date_morning.setTextColor(Color.YELLOW)
                choose_date_noon.setTextColor(Color.GRAY)
                choose_date_afternoon.setTextColor(Color.GRAY)
                choose_date_evening.setTextColor(Color.GRAY)
                chooseTime=0
            }
            R.id.choose_date_noon->{
                choose_date_morning.setTextColor(Color.GRAY)
                choose_date_noon.setTextColor(Color.YELLOW)
                choose_date_afternoon.setTextColor(Color.GRAY)
                choose_date_evening.setTextColor(Color.GRAY)
                chooseTime=1
            }
            R.id.choose_date_afternoon->{
                choose_date_morning.setTextColor(Color.GRAY)
                choose_date_noon.setTextColor(Color.GRAY)
                choose_date_afternoon.setTextColor(Color.YELLOW)
                choose_date_evening.setTextColor(Color.GRAY)
                chooseTime=2
            }
            R.id.choose_date_evening->{
                choose_date_morning.setTextColor(Color.GRAY)
                choose_date_noon.setTextColor(Color.GRAY)
                choose_date_afternoon.setTextColor(Color.GRAY)
                choose_date_evening.setTextColor(Color.YELLOW)
                chooseTime=3
            }
            else->{
                Log.d(TAG, "button not used")
            }
        }
    }

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
            this.year=year
            this.month=month
            this.day=dayOfMonth
        }
        recSelectTime = choose_date_rec_time_select
        selectTimeAdapter = HorizontalSelectView()
        recSelectTime.adapter = selectTimeAdapter

        val layoutManager = LinearLayoutManager(this.context)
        layoutManager.orientation = LinearLayout.HORIZONTAL
        recSelectTime.layoutManager = layoutManager

        recSelectTime.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                Log.d("CDF", "newState: $newState")
                recyclerView.scrollToPosition(layoutManager.findFirstCompletelyVisibleItemPosition()+2)
                layoutManager.scrollToPositionWithOffset(layoutManager.findFirstCompletelyVisibleItemPosition()+2, 0)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.d("CDF", "dx: $dx, dy: $dy, first pos: ${layoutManager.findFirstCompletelyVisibleItemPosition()}")

            }
        })

        choose_date_morning.setOnClickListener(this)
        choose_date_noon.setOnClickListener(this)
        choose_date_afternoon.setOnClickListener(this)
        choose_date_evening.setOnClickListener(this)

        choose_date_submit.setOnClickListener {
            confirmAndDismiss()
        }
    }

    interface SendDate{
        fun getDate(date: Date)
    }

    private fun confirmAndDismiss(){
        dismiss()
        calendar.set(
            year,
            month,
            day,
            when(chooseTime){
                0->8
                1->11
                2->15
                3->21
                else->0
            },
            0)
        sendDate.getDate(calendar.time)
    }

    companion object{
        const val TAG = "CDF"
    }
}
