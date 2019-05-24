package tty.balanceyourio.page

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
        tfLight = Typeface.createFromAsset(context!!.assets, "OpenSans-Bold.ttf")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        data = helper!!.getBill()

        statisticsList=BillRecordsProvider.getBillRecordsForSumByTimeMode(data, timeMode)
//        val desc=Description()
//        desc.text="按日统计"
//        chart.description = desc
        chart.description.isEnabled=false
//        chart.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
        chart.setNoDataText("暂无数据")
        chart.setTouchEnabled(true)
        chart.isDoubleTapToZoomEnabled=false
        chart.isDragEnabled = true
        chart.isScaleXEnabled = true
        chart.isScaleYEnabled = false
        chart.setPinchZoom(false)

        val x = chart.xAxis
        x.labelCount=7
        x.setAvoidFirstLastClipping(true)
        x.granularity=1F
        //设置一页最大显示个数为7
        val ratio = statisticsList.size.toFloat() / 7
        //显示的时候是按照多大的比率缩放显示,1F表示不放大缩小
        chart.zoom(ratio,1F,0F,0F)
        x.isGranularityEnabled=true
        x.textColor = resources.getColor(R.color.colorNormal, null)
        x.position = XAxis.XAxisPosition.BOTTOM
        x.setDrawGridLines(false)
        x.axisLineColor = resources.getColor(R.color.colorNormal, null)

        val yOutcome = chart.axisLeft
        yOutcome.setLabelCount(7, false)
        yOutcome.axisLineWidth=1F
        yOutcome.textColor = resources.getColor(R.color.typeOutcome, null)
        yOutcome.setDrawGridLines(false)
        yOutcome.axisLineColor = resources.getColor(R.color.typeOutcome, null)

        val yIncome = chart.axisRight
        yIncome.setLabelCount(7, false)
        yIncome.axisLineWidth=1F
        yIncome.textColor = resources.getColor(R.color.typeIncome, null)
        yIncome.setDrawGridLines(false)
        yIncome.axisLineColor = resources.getColor(R.color.typeIncome, null)

        setData(chart, statisticsList, timeMode)

        x.valueFormatter=object : ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
                val p=value.toInt()
                return if(p>0 && p<statisticsList.size){
                    "Pos.$p"
                } else {
                    ""
                }
            }
        }

        chart.invalidate()

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "AF destroy")
        helper?.close()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun setData(chart: LineChart, statistics: ArrayList<HashMap<IOType, Double>>, timeMode: TimeMode) {

        val valuesOutcome = java.util.ArrayList<Entry>()
        val valuesIncome = java.util.ArrayList<Entry>()

        for (i in 0 until statistics.size) {
            valuesOutcome.add(Entry(i.toFloat(), statistics[i][IOType.Outcome]!!.toFloat()))
            valuesIncome.add(Entry(i.toFloat(), statistics[i][IOType.Income]!!.toFloat()))
        }

        val outcomeSet: LineDataSet
        val incomeSet: LineDataSet
        if (chart.data != null && chart.data.dataSetCount > 0) {
            outcomeSet = chart.data.getDataSetByIndex(0) as LineDataSet
            outcomeSet.values = valuesOutcome
            incomeSet = chart.data.getDataSetByIndex(1) as LineDataSet
            incomeSet.values = valuesOutcome
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
            outcomeSet.fillFormatter = IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }
            outcomeSet.setDrawFilled(true)
            outcomeSet.setDrawValues(true)
            outcomeSet.valueTextColor = resources.getColor(R.color.typeOutcome, null)

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
            incomeSet.fillFormatter = IFillFormatter { dataSet, dataProvider -> chart.axisRight.axisMinimum }
            incomeSet.setDrawFilled(true)
            incomeSet.setDrawValues(true)
            incomeSet.valueTextColor = resources.getColor(R.color.typeIncome, null)

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
