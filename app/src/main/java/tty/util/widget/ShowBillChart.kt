package tty.util.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import tty.balanceyourio.R

class ShowBillChart : View {

    private var _title: String?=null
    private var _chartMode: String? = null

    private var textPaint: TextPaint? = null
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f

    private var _data: ArrayList<HashMap<String, Int>>? = null

    private var title: String?
        get() = _title
        set(value) {
            _title = value
            invalidateTitleTextPaintAndMeasurements()
        }

    /**
     * data should be
     */
    private var data: ArrayList<HashMap<String, Int>>?
        get() = _data
        set(value) {
            _data = value
        }

    private var chartMode: String?
        get() = _chartMode
        set(value) {
            _chartMode=when(value){
                "pie" -> "pie"
                "broken_line" -> "broken_line"
                "cylindrical" -> "cylindrical"
                else -> "broken_line"
            }

        }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.ShowBillChart, defStyle, 0)

        _title = a.getString(R.styleable.ShowBillChart_title)

        _chartMode = a.getString(R.styleable.ShowBillChart_chartMode)

        a.recycle()

        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }

        invalidateTitleTextPaintAndMeasurements()
    }


    private fun invalidateTitleTextPaintAndMeasurements() {
        textPaint?.let {
            it.textSize = 48.0F
            it.color = 0xaacc00cc.toInt()
            textWidth = it.measureText(title)
            textHeight = it.fontMetrics.bottom
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        title?.let {
            canvas.drawText(it, paddingLeft + (contentWidth - textWidth) / 2, (textHeight+paddingTop), textPaint)
        }

        data?.let {
            // TODO 完成表格的绘制
            Log.d(TAG, "chartMode: $chartMode")
            when(chartMode){
                "broken_line" -> {
                    drawBrokenLineChart()
                }

                "pie" ->{
                    drawPieChart()
                }

                "cylindrical" -> {
                    drawCylindricalChart()
                }

                else->{
                    drawBrokenLineChart()
                }
            }
        }

    }

    /**
     * 折线图表格
     */
    private fun drawBrokenLineChart(){

    }

    /**
     * 饼图表格
     */
    private fun drawPieChart(){

    }

    /**
     * 柱形图表格
     */
    private fun drawCylindricalChart(){

    }

    companion object{
        const val TAG = "SBI"
    }
}
