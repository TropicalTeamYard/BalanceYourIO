package tty.balanceyourio.page

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_analysis.view.*
import tty.balanceyourio.R
import tty.balanceyourio.data.BYIOHelper
import tty.balanceyourio.model.BillRecord

class AnalysisFragment : Fragment() {

    private var helper: BYIOHelper? = null
    private lateinit var data:ArrayList<BillRecord>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        helper = context?.let { BYIOHelper(it) }
        return inflater.inflate(R.layout.fragment_analysis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data= helper?.getBill()!!
        //Log.d(TAG, "一共有 ${data.size} 条记录" )
        view.analysis_chart.data=data
    }

    override fun onResume() {
        super.onResume()
        data= helper!!.getBill()
        //Log.d(TAG, "一共有 ${data.size} 条记录" )
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
