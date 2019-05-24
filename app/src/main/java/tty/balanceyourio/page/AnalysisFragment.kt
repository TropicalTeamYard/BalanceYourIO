package tty.balanceyourio.page

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import kotlinx.android.synthetic.main.fragment_analysis.*
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
    private var timeMode=TimeMode.Day
    lateinit var chart: LineChart
    private lateinit var tfLight: Typeface

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        helper = context?.let { BYIOHelper(it) }
        return inflater.inflate(R.layout.fragment_analysis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chart=time_line_chart
        tfLight = Typeface.createFromAsset(context!!.assets, "OpenSans-Light.ttf")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        data = helper!!.getBill()

        statisticsList=BillRecordsProvider.getBillRecordsForSumByTimeMode(data, timeMode)
        val desc=Description()
        desc.text="按日统计"
        chart.description = desc
        chart.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
        chart.setNoDataText("暂无数据")
        chart.setTouchEnabled(true)
//        chart.setMaxVisibleValueCount(7)
        chart.isDoubleTapToZoomEnabled=false
        chart.isDragEnabled = true
        chart.isScaleXEnabled = false
        chart.isScaleYEnabled = false
        chart.setPinchZoom(false)
        val x = chart.xAxis
        x.setLabelCount(7, false)
        x.textColor = Color.WHITE
        x.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        x.setDrawGridLines(false)
        x.axisLineColor = Color.WHITE

        val y = chart.axisLeft
        y.setLabelCount(6, false)
        y.textColor = Color.WHITE
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        y.setDrawGridLines(false)
        y.axisLineColor = Color.WHITE
        chart.axisRight.isEnabled = false

        setData(chart, statisticsList, timeMode)

        val sets = chart.data.dataSets
        for (iSet in sets) {
            val set = iSet as LineDataSet
            set.setDrawValues(true)
            set.setDrawCircles(true)
        }

        chart.invalidate()

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "AF destroy")
        helper?.close()
    }


    private fun setData(chart: LineChart, statistics: ArrayList<HashMap<IOType, Double>>, timeMode: TimeMode) {

        val values = java.util.ArrayList<Entry>()

        for (i in 0 until statistics.size) {
            val `val` = statistics[i][IOType.Outcome]!!.toFloat()
            values.add(Entry(i.toFloat(), `val`))
        }

        val set1: LineDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "支出")

            set1.mode = LineDataSet.Mode.LINEAR
            set1.cubicIntensity = 0.2f
            set1.setDrawFilled(true)
            set1.setDrawCircles(false)
            set1.lineWidth = 1.8f
            set1.circleRadius = 4f
            set1.setCircleColor(Color.WHITE)
            set1.highLightColor = Color.rgb(244, 117, 117)
            set1.color = Color.WHITE
            set1.fillColor = Color.WHITE
            set1.fillAlpha = 100
            set1.setDrawHorizontalHighlightIndicator(true)
            set1.fillFormatter = IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }

            // create a data object with the data sets
            val data = LineData(set1)
            data.setValueTypeface(tfLight)
            data.setValueTextSize(9f)
            data.setDrawValues(false)

            // set data
            chart.data = data
        }
    }



    companion object{
        const val TAG = "AF"
    }
}
