package tty.balanceyourio.page

import android.annotation.TargetApi
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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.fragment_analysis.*
import tty.balanceyourio.R
import tty.balanceyourio.data.BYIOHelper
import tty.balanceyourio.data.BillRecordsProvider
import tty.balanceyourio.model.BillRecord
import tty.balanceyourio.model.IOType
import tty.balanceyourio.model.TimeMode
import tty.balanceyourio.util.DateConverter
import java.text.DecimalFormat
import java.util.*


class AnalysisFragment : Fragment() {
    fun updateData() {
        Log.d(TAG, "update DATA")
        getDataAndShow()
    }

    private var helper: BYIOHelper? = null
    private lateinit var data:ArrayList<BillRecord>
    private lateinit var statisticsList: ArrayList<HashMap<IOType, Double>>
    private lateinit var timeList: ArrayList<Date>
    private var timeMode=TimeMode.Day
    lateinit var chart: LineChart
    private lateinit var tfLight: Typeface
    private val decimalFormat2 = DecimalFormat("#.##")
    private val decimalFormat0 = DecimalFormat("#")
    var ratio=0F

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        helper = context?.let { BYIOHelper(it) }
        return inflater.inflate(R.layout.fragment_analysis, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chart=time_line_chart
        tfLight = Typeface.createFromAsset(context!!.assets, "OpenSans-Bold.ttf")
        chart.description.isEnabled=false
        chart.setNoDataText("暂无数据")
        chart.setTouchEnabled(true)
        chart.isDoubleTapToZoomEnabled=false
        chart.isDragEnabled = true
        chart.isScaleXEnabled = false
        chart.isScaleYEnabled = false
        chart.setPinchZoom(false)

        val x = chart.xAxis
        x.setLabelCount(7, false)
        x.setAvoidFirstLastClipping(false)
        x.textColor = resources.getColor(R.color.colorNormal, null)
        x.position = XAxis.XAxisPosition.BOTTOM
        x.setDrawGridLines(false)
        x.isGranularityEnabled = true
        x.axisLineColor = resources.getColor(R.color.colorNormal, null)

        chart.axisLeft.isEnabled=false
        chart.axisRight.isEnabled=false
        chart.axisLeft.axisMinimum=0F
        chart.axisRight.axisMinimum=0F

        x.valueFormatter=object : ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
//                Log.d(TAG, decimalFormat0.format(value))
                val pos=decimalFormat0.format(value).toInt()
                if(pos<0 || pos>=timeList.size){
                    return "..."
                }
                return DateConverter.getXAxisDate(timeList[pos], timeMode)
            }
        }

    }


    override fun onResume() {
        super.onResume()
        getDataAndShow()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun getDataAndShow() {
        data = helper!!.getBill()
        statisticsList = BillRecordsProvider.getBillRecordsForSumByTimeMode(data, timeMode)
        timeList = BillRecordsProvider.getBillRecordsForTimeListByTimeMode(data, timeMode)
        if (ratio >= 0) {
            ratio = statisticsList.size.toFloat() / 7
            chart.zoom(ratio, 1F, 0F, 0F)
            ratio = -1F
        }
        setData(chart, statisticsList, timeMode)
        chart.invalidate()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "AF destroy")
        helper?.close()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun setData(chart: LineChart, statistics: ArrayList<HashMap<IOType, Double>>, timeMode: TimeMode) {

        val valuesOutcome = ArrayList<Entry>()
        val valuesIncome = ArrayList<Entry>()

        for (i in 0 until statistics.size) {
            val valueOutcome: Float = Math.log(statistics[i][IOType.Outcome]!!+1).toFloat()
            val valueIncome: Float = Math.log(statistics[i][IOType.Income]!!+1).toFloat()
            valuesOutcome.add(Entry(i.toFloat(), valueOutcome))
            valuesIncome.add(Entry(i.toFloat(), valueIncome))
        }

        val outcomeSet: LineDataSet
        val incomeSet: LineDataSet
        if (chart.data != null && chart.data.dataSetCount > 0) {
            outcomeSet = chart.data.getDataSetByIndex(0) as LineDataSet
            outcomeSet.values = valuesOutcome
            incomeSet = chart.data.getDataSetByIndex(1) as LineDataSet
            incomeSet.values = valuesIncome
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            outcomeSet = LineDataSet(valuesOutcome, "支出")
            outcomeSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            outcomeSet.cubicIntensity = 0.2f
            outcomeSet.lineWidth = 1.8f
            outcomeSet.circleRadius = 2.4f
            outcomeSet.setCircleColor(resources.getColor(R.color.typeOutcome, null))
            outcomeSet.highLightColor = resources.getColor(R.color.typeOutcome, null)
            outcomeSet.color = resources.getColor(R.color.typeOutcome, null)
            outcomeSet.fillColor = resources.getColor(R.color.typeOutcome, null)
            outcomeSet.fillAlpha = 100
            outcomeSet.setDrawHorizontalHighlightIndicator(true)
            outcomeSet.fillFormatter = IFillFormatter { _, _ -> chart.axisLeft.axisMinimum }
            outcomeSet.setDrawFilled(true)
            outcomeSet.setDrawValues(true)
            outcomeSet.valueTextColor = resources.getColor(R.color.typeOutcome, null)
            outcomeSet.valueFormatter=object : ValueFormatter(){
                override fun getFormattedValue(value: Float): String {
                    return decimalFormat2.format(Math.pow(Math.E, value.toDouble())-1)
                }
            }

            incomeSet = LineDataSet(valuesIncome, "收入")
            incomeSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            incomeSet.cubicIntensity = 0.2f
            incomeSet.lineWidth = 1.8f
            incomeSet.circleRadius = 2.4f
            incomeSet.setCircleColor(resources.getColor(R.color.typeIncome, null))
            incomeSet.highLightColor = resources.getColor(R.color.typeIncome, null)
            incomeSet.color = resources.getColor(R.color.typeIncome, null)
            incomeSet.fillColor = resources.getColor(R.color.typeIncome, null)
            incomeSet.fillAlpha = 100
            incomeSet.setDrawHorizontalHighlightIndicator(true)
//            incomeSet.axisDependency = YAxis.AxisDependency.RIGHT
            incomeSet.fillFormatter = IFillFormatter { _, _ -> chart.axisRight.axisMinimum }
            incomeSet.setDrawFilled(true)
            incomeSet.setDrawValues(true)
            incomeSet.valueTextColor = resources.getColor(R.color.typeIncome, null)
            incomeSet.valueFormatter=object : ValueFormatter(){
                override fun getFormattedValue(value: Float): String {
                    return decimalFormat2.format(Math.pow(Math.E, value.toDouble())-1)
                }
            }


            val data = LineData(outcomeSet, incomeSet)
            data.setValueTypeface(tfLight)
            data.setValueTextSize(9f)

            chart.data = data
        }
    }

    companion object{
        const val TAG = "AF"
    }
}
