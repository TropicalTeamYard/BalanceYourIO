package tty.balanceyourio.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import tty.balanceyourio.R
import tty.balanceyourio.model.BillRecord
import tty.balanceyourio.model.ChartMode
import tty.balanceyourio.model.IOType
import tty.balanceyourio.model.TimeMode
import tty.balanceyourio.util.DateConverter
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ShowBillChart : View {

    private var _title: String? = null
    private var _chartMode: ChartMode? = null
    private var _timeMode: TimeMode? = null
    private var _titleColor: Int = 0xFFFFFFFF.toInt()

    private var timeModeBill = ArrayList<ArrayList<BillRecord>>()
    private var timeModeSum = ArrayList<HashMap<IOType, Double>>()
    private var timeModeList = ArrayList<Date>()

    private val decimalFormat = DecimalFormat("#.##")

    private var textPaint: TextPaint? = null
    private var textWidth: Float = 0F
    private var textHeight: Float = 0F

    private var _data: ArrayList<BillRecord>? = null

    private var positionStart = Position(0F, 0F)
    private var positionEnd = Position(0F, 0F)

    private var title: String?
        get() = _title
        set(value) {
            _title = value
            invalidateTitleTextPaintAndMeasurements()
        }

    /**
     * data should be array of BillRecord
     */
    var data: ArrayList<BillRecord>?
        get() = _data
        set(value) {
            _data = value
        }

    private var chartMode: ChartMode?
        get() = _chartMode
        set(value) {
            _chartMode = value
        }

    private var timeMode: TimeMode?
        get() = _timeMode
        set(value) {
            _timeMode = value
        }

    private var titleColor: Int
        get() = _titleColor
        set(value) {
            _titleColor = value
        }


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun dp2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density;
        return ((dpValue * scale + 0.5f).toInt())
    }

    private fun px2dp(pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        performClick()
        if (event?.y!! < textHeight + paddingTop) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "TOUCH DOWN")
                positionStart.x = event.x
                positionStart.y = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                positionEnd.x = event.x
                positionEnd.y = event.y

                if (positionEnd.x - positionStart.x > dp2px(2F)) {
                    Log.d(TAG, "MOVE RIGHT")
                } else if (positionEnd.x - positionStart.x < dp2px(-2F)) {
                    Log.d(TAG, "MOVE LEFT")
                }
                if (positionEnd.y - positionStart.y > dp2px(2F)) {
                    Log.d(TAG, "MOVE DOWN")
                } else if (positionEnd.y - positionStart.y < dp2px(-2F)) {
                    Log.d(TAG, "MOVE UP")
                }

                positionStart.x = event.x
                positionStart.y = event.y
            }
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "TOUCH UP")
//                positionEnd.x=event.x
//                positionEnd.y=event.y
//                if(positionEnd.x-positionStart.x>0){
//                    Log.d(TAG, "MOVE RIGHT")
//                } else {
//                    Log.d(TAG, "MOVE LEFT")
//                }
//                Log.d(TAG, "init position")
                positionEnd.x = 0F
                positionEnd.y = 0F
                positionStart.x = 0F
                positionStart.y = 0F
            }
            MotionEvent.ACTION_OUTSIDE -> {

            }
            else -> {
                Log.d(TAG, "OTHER GESTURE")
            }
        }
        //Log.d(TAG,"X, Y = ${event.x}, ${event.y}")
        //Toast.makeText(context, "X,Y=(${event?.x}, ${event?.y})", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.ShowBillChart, defStyle, 0)

        _title = a.getString(R.styleable.ShowBillChart_title)
        if (_title == null) {
            _title = ""
        }

        _chartMode = when (a.getInt(R.styleable.ShowBillChart_chartMode, 3)) {
            0 -> ChartMode.Pie
            1 -> ChartMode.BrokenLine
            2 -> ChartMode.Cylindrical
            3 -> ChartMode.List
            else -> ChartMode.List
        }

        _timeMode = when (a.getInt(R.styleable.ShowBillChart_timeMode, 0)) {
            0 -> TimeMode.Day
            1 -> TimeMode.Week
            2 -> TimeMode.Month
            3 -> TimeMode.Year
            else -> TimeMode.Month
        }

        _titleColor = a.getResourceId(R.styleable.ShowBillChart_titleColor, R.color.white)

        a.recycle()

        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }

        invalidateTitleTextPaintAndMeasurements()
    }


    @SuppressLint("NewApi")
    private fun invalidateTitleTextPaintAndMeasurements() {
        textPaint?.let {
            it.textSize = 48.0F
            it.color = resources.getColor(_titleColor, null)
            textWidth = it.measureText(title)
            textHeight = it.fontMetrics.bottom
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        Log.d(TAG, "onDraw")

        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        title?.let {
            canvas.drawText(it, paddingLeft.toFloat(), (textHeight + paddingTop), textPaint as Paint)
        }

        data?.let {
            // TODO 完成表格的绘制
            Log.d(TAG, "ChartMode: $chartMode")
            Log.d(TAG, "TimeMode: $timeMode")

            when (chartMode) {
                ChartMode.BrokenLine -> {
                    drawBrokenLineChart()
                }

                ChartMode.Pie -> {
                    drawPieChart()
                }

                ChartMode.Cylindrical -> {
                    drawCylindricalChart()
                }

                else -> {
                    drawListChart()
                }
            }
        }

    }

    /**
     * 折线图表格
     */
    private fun drawBrokenLineChart() {

    }

    /**
     * 饼图表格
     */
    private fun drawPieChart() {

    }

    /**
     * 柱形图表格
     */
    private fun drawCylindricalChart() {


    }

    /**
     * 列表表格
     */
    private fun drawListChart() {
        getTimeModeArray()
//        timeModeBill
//        timeModeList
//        timeModeSum
    }

    private fun clearTimeModeData() {
        timeModeBill.clear()
        timeModeList.clear()
        timeModeSum.clear()
    }

    private fun getTimeModeArray() {
        if (_data == null || data!!.size == 0) {
            return
        }
        Log.d(TAG, "data size: ${data!!.size}")

        clearTimeModeData()

        when (timeMode) {
            TimeMode.Day -> {
                val dateSet = HashSet<Date>()
                for (i in 0 until _data!!.size) {
                    dateSet.add(DateConverter.cutToDate(_data!![i].time!!))
                    timeModeSum.add(HashMap())
                    timeModeSum[i][IOType.Income] = 0.0
                    timeModeSum[i][IOType.Outcome] = 0.0
                    timeModeSum[i][IOType.Unset] = 0.0
                }
                timeModeList = ArrayList(dateSet)
                Log.d(TAG, "date size: ${timeModeList.size}")
                timeModeList.sort()
                timeModeList.reverse()
                for (i in 0 until timeModeList.size) {
                    val tData = ArrayList<BillRecord>()
                    for (j in 0 until _data!!.size) {
                        if (DateConverter.equalDate(_data!![j].time!!, timeModeList[i])) {
                            tData.add(_data!![j])
                            when (_data!![j].ioType) {
                                IOType.Income -> timeModeSum[i][IOType.Income] =
                                    timeModeSum[i][IOType.Income]!! + _data!![j].amount
                                IOType.Outcome -> timeModeSum[i][IOType.Outcome] =
                                    timeModeSum[i][IOType.Outcome]!! + _data!![j].amount
                                else -> timeModeSum[i][IOType.Unset] =
                                    timeModeSum[i][IOType.Unset]!! + _data!![j].amount
                            }
                        }
                    }

                    tData.sortByDescending { it.time }
                    timeModeBill.add(tData)
                }
            }

            TimeMode.Week -> {
                val weekSet = HashSet<Date>()
                for (i in 0 until _data!!.size) {
                    weekSet.add(DateConverter.cutToWeek(_data!![i].time!!))
                }
                timeModeList = ArrayList(weekSet)
                Log.d(TAG, "week size: ${timeModeList.size}")
                timeModeList.sort()
                timeModeList.reverse()
                for (i in 0 until timeModeList.size) {
                    timeModeSum.add(HashMap())
                    timeModeSum[i][IOType.Income] = 0.0
                    timeModeSum[i][IOType.Outcome] = 0.0
                    timeModeSum[i][IOType.Unset] = 0.0
                    val tData = ArrayList<BillRecord>()
                    for (j in 0 until _data!!.size) {
                        if (DateConverter.equalWeek(_data!![j].time!!, timeModeList[i])) {
                            tData.add(_data!![j])
                            when (_data!![j].ioType) {
                                IOType.Income -> timeModeSum[i][IOType.Income] =
                                    timeModeSum[i][IOType.Income]!! + _data!![j].amount
                                IOType.Outcome -> timeModeSum[i][IOType.Outcome] =
                                    timeModeSum[i][IOType.Outcome]!! + _data!![j].amount
                                else -> timeModeSum[i][IOType.Unset] =
                                    timeModeSum[i][IOType.Unset]!! + _data!![j].amount
                            }
                        }
                    }

                    tData.sortByDescending { it.time }
                    timeModeBill.add(tData)
                }
            }

            TimeMode.Month -> {
                val monthSet = HashSet<Date>()
                for (i in 0 until _data!!.size) {
                    monthSet.add(DateConverter.cutToMonth(_data!![i].time!!))
                }
                timeModeList = ArrayList(monthSet)
                Log.d(TAG, "month size: ${timeModeList.size}")
                timeModeList.sort()
                timeModeList.reverse()
                for (i in 0 until timeModeList.size) {
                    timeModeSum.add(HashMap())
                    timeModeSum[i][IOType.Income] = 0.0
                    timeModeSum[i][IOType.Outcome] = 0.0
                    timeModeSum[i][IOType.Unset] = 0.0
                    val tData = ArrayList<BillRecord>()
                    for (j in 0 until _data!!.size) {
                        if (DateConverter.equalMonth(_data!![j].time!!, timeModeList[i])) {
                            tData.add(_data!![j])
                            when (_data!![j].ioType) {
                                IOType.Income -> timeModeSum[i][IOType.Income] =
                                    timeModeSum[i][IOType.Income]!! + _data!![j].amount
                                IOType.Outcome -> timeModeSum[i][IOType.Outcome] =
                                    timeModeSum[i][IOType.Outcome]!! + _data!![j].amount
                                else -> timeModeSum[i][IOType.Unset] =
                                    timeModeSum[i][IOType.Unset]!! + _data!![j].amount
                            }
                        }
                    }

                    tData.sortByDescending { it.time }
                    timeModeBill.add(tData)
                }
            }

            TimeMode.Year -> {
                val yearSet = HashSet<Date>()
                for (i in 0 until _data!!.size) {
                    yearSet.add(DateConverter.cutToYear(_data!![i].time!!))
                }
                timeModeList = ArrayList(yearSet)
                Log.d(TAG, "year size: ${timeModeList.size}")
                timeModeList.sort()
                timeModeList.reverse()
                for (i in 0 until timeModeList.size) {
                    timeModeSum.add(HashMap())
                    timeModeSum[i][IOType.Income] = 0.0
                    timeModeSum[i][IOType.Outcome] = 0.0
                    timeModeSum[i][IOType.Unset] = 0.0
                    val tData = ArrayList<BillRecord>()
                    for (j in 0 until _data!!.size) {
                        if (DateConverter.equalYear(_data!![j].time!!, timeModeList[i])) {
                            tData.add(_data!![j])
                            when (_data!![j].ioType) {
                                IOType.Income -> timeModeSum[i][IOType.Income] =
                                    timeModeSum[i][IOType.Income]!! + _data!![j].amount
                                IOType.Outcome -> timeModeSum[i][IOType.Outcome] =
                                    timeModeSum[i][IOType.Outcome]!! + _data!![j].amount
                                else -> timeModeSum[i][IOType.Unset] =
                                    timeModeSum[i][IOType.Unset]!! + _data!![j].amount
                            }
                        }
                    }

                    tData.sortByDescending { it.time }
                    timeModeBill.add(tData)
                }
            }
        }

    }

    companion object {
        const val TAG = "SBI"
    }

    data class Position(var x: Float, var y: Float)
}
