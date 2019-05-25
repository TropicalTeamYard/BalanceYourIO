package tty.balanceyourio.util

import java.text.DecimalFormat

object NumberFormatter {
    val decimalFormat2 = DecimalFormat("#.##")
    val decimalFormat0 = DecimalFormat("#")
    fun logToDouble(d: Double): Double{
        return Math.pow(Math.E, Math.abs(d))
    }
}