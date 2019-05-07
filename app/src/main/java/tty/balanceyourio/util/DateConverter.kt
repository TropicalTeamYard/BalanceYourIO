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
        return SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(d)
    }

    fun getSimpleDate(s: String): Date{
        return SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(s)
    }
}