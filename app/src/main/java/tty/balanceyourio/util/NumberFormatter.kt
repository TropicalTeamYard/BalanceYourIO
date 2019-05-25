package tty.balanceyourio.util

import android.content.Context
import java.text.DecimalFormat

object NumberFormatter {
    val decimalFormat2 = DecimalFormat("#.##")
    val decimalFormat0 = DecimalFormat("#")
    fun logToDouble(d: Double): Double{
        return Math.pow(Math.E, Math.abs(d))
    }

    fun dp2px(context: Context, dpValue: Float) : Int {
        val scale = context.resources.displayMetrics.density;
        return ((dpValue * scale + 0.5f).toInt())
    }

    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
}

