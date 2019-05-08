package tty.balanceyourio.util

import java.text.SimpleDateFormat
import java.util.*

object DateConverter {

    fun getString(d: Date): String{
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(d)
    }

    fun getDate(s: String): Date{
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(s)
    }

    fun getSimpleString(d: Date): String{
        val calendar: Calendar= Calendar.getInstance()
        calendar.time=d
        val h=calendar.get(Calendar.HOUR)
        return SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(d)+when(h){
            in 6..10->"上午"
            in 11..12->"中午"
            in 13..18->"下午"
            else->"UNDEFINED"
        }
    }

    fun getSimpleDate(s: String): Date{
        return SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(s)
    }
}