package tty.balanceyourio.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ExpandableListView

class TopBottomLoadListView(context: Context?, attrs: AttributeSet?) : ExpandableListView(context, attrs) {

    private var maxOverScrollYDis = 0

    private var y1: Float = 0F
    private var y2: Float = 0F

    override fun overScrollBy(
        deltaX: Int, deltaY: Int,
        scrollX: Int, scrollY: Int,
        scrollRangeX: Int, scrollRangeY: Int,
        maxOverScrollX: Int, maxOverScrollY: Int,
        isTouchEvent: Boolean
    ): Boolean {
        return super.overScrollBy(
            deltaX, deltaY,
            scrollX, scrollY,
            scrollRangeX, scrollRangeY,
            maxOverScrollX, maxOverScrollYDis / 3,
            isTouchEvent)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        performClick()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> y1 = ev.getY(0)
            MotionEvent.ACTION_MOVE -> {
                y2 = ev.getY(0)
                maxOverScrollYDis = Math.abs (y2 - y1).toInt()
            }
            MotionEvent.ACTION_UP -> {
                maxOverScrollYDis = 0
                Log.d(TAG, "ACTION UP")
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    companion object{
        const val TAG = "TBLLV"
    }
}