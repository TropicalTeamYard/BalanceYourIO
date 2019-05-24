package tty.balanceyourio.page

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tty.balanceyourio.R
import tty.balanceyourio.data.BYIOHelper
import tty.balanceyourio.data.BillRecordsProvider
import tty.balanceyourio.model.BillRecord
import tty.balanceyourio.model.IOType
import tty.balanceyourio.model.TimeMode

class AnalysisFragment : Fragment() {

    private var helper: BYIOHelper? = null
    private lateinit var data:ArrayList<BillRecord>
    private lateinit var statisticsList: ArrayList<HashMap<IOType, Double>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        helper = context?.let { BYIOHelper(it) }
        return inflater.inflate(R.layout.fragment_analysis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = helper?.getBill()!!
        //Log.d(TAG, "一共有 ${data.size} 条记录" )
    }

    override fun onResume() {
        super.onResume()
        data = helper!!.getBill()
//        Log.d(TAG, "一共有 ${data.size} 条记录" )
//        view!!.analysis_chart.data=data
        statisticsList=BillRecordsProvider.getBillRecordsForSumByTimeMode(data, TimeMode.Day)
        
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "AF destroy")
        helper?.close()
    }

    companion object{
        const val TAG = "AF"
    }
}
