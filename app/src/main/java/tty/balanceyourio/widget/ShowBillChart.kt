package tty.balanceyourio.widget

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

class ShowBillChart : View {

    private var _title: String?=null
    private var _chartMode: String? = null

    private var textPaint: TextPaint? = null
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f

    private var _data: ArrayList<BillRecord>? = null

    private var positionStart=Position(0F, 0F)
    private var positionEnd=Position(0F, 0F)

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

    private fun dp2px(dpValue: Float) : Int {
        val scale = context.resources.displayMetrics.density;
        return ((dpValue * scale + 0.5f).toInt())
    }

    fun px2dp(pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        performClick()
        if(event?.y!! < textHeight+paddingTop){
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN->{
                //Log.d(TAG, "DOWN")
                positionStart.x=event.x
                positionStart.y=event.y

            }
            MotionEvent.ACTION_MOVE->{
                //Log.d(TAG, "MOVE")
                positionEnd.x=event.x
                positionEnd.y=event.x
                if(positionEnd.x-positionStart.x>dp2px(2F)) {
                    Log.d(TAG, "MOVE RIGHT")
                } else if(positionEnd.x-positionStart.x<dp2px(-2F)) {
                    Log.d(TAG, "MOVE LEFT")
                }
                positionStart.x=event.x
                positionStart.y=event.y
            }
            MotionEvent.ACTION_UP->{
                //Log.d(TAG, "UP")
//                positionEnd.x=event.x
//                positionEnd.y=event.x
//                if(positionEnd.x-positionStart.x>0){
//                    Log.d(TAG, "MOVE RIGHT")
//                } else {
//                    Log.d(TAG, "MOVE LEFT")
//                }
                Log.d(TAG, "init position")
                positionEnd.x=0F
                positionEnd.y=0F
                positionStart.x=0F
                positionStart.y=0F
            }
            else->{
                //Log.d(TAG, "Other")
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

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        return super.onTouchEvent(event)
//        Log.d(TAG,"X,Y=($x, $y)")
//    }
//
//    override fun setOnTouchListener(l: OnTouchListener?) {
//        super.setOnTouchListener(l)
//    }

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

    data class Position(var x: Float,var y:Float)
}
