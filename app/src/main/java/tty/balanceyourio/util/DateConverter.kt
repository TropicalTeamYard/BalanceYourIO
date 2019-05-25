package tty.balanceyourio.util

import android.annotation.TargetApi
import android.os.Build
import tty.balanceyourio.model.TimeMode
import java.text.SimpleDateFormat
import java.util.*

object DateConverter {

    fun getString(d: Date): String{
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(d)
    }

    fun getDate(s: String): Date{
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(s)
    }

    /**
     * 获取日期字符串的友好格式
     */
    fun getFriendDateString(d:Date):String{

        val str:String
        val dArray = arrayOf("日","一","二","三","四","五","六")
        val calendar = Calendar.getInstance()
        val cYear:Int = calendar.get(Calendar.YEAR)
        calendar.time = d
        val dayOfWeek:Int = calendar.get(Calendar.DAY_OF_WEEK)

        str = "${calendar.get(Calendar.MONTH) + 1}月${calendar.get(Calendar.DAY_OF_MONTH)}日 星期${dArray[calendar.get(Calendar.DAY_OF_WEEK)-1]}"

        return if (cYear == calendar.get(Calendar.YEAR)){
            str
        } else {
            "${calendar.get(Calendar.YEAR)}年$str"
        }

    }

    fun getSimpleString(d: Date): String{
        val calendar: Calendar= Calendar.getInstance()
        calendar.time=d
        val h=calendar.get(Calendar.HOUR_OF_DAY)
//        Log.d("DC", "Hour: $h")
        return SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(d)+" "+when(h){
            in 6..10->"上午"
            in 11..12->"中午"
            in 13..18->"下午"
            in 19..23->"晚上"
            in 0..5->"凌晨"
            else->"UNDEFINED"
        }
    }

    fun getSimpleDate(s : String): Date{
        val time=s.split(" ")
        if(time.size==2){
            val calendar = Calendar.getInstance()
            try {
                val d: Date = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(s)
                calendar.time=d
                calendar.set(Calendar.HOUR_OF_DAY, when(time[1]){
                    "上午" -> 8
                    "中午" -> 11
                    "下午" -> 15
                    "晚上" -> 21
                    else -> 0
                })
            } catch (e: Exception){
                return Date()
            }
            return calendar.time
        } else {
            return Date()
        }
    }

    fun equalDate(d1:Date, d2:Date): Boolean{
        val calendar = Calendar.getInstance()
        calendar.time = d1
        val year:Int
        val month:Int
        val day:Int
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.time = d2
        return (year == calendar.get(Calendar.YEAR) &&
                month == calendar.get(Calendar.MONTH) &&
                day == calendar.get(Calendar.DAY_OF_MONTH))
    }

    fun cutToDate(d:Date): Date{
        val calendar = Calendar.getInstance()
        calendar.time = d

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),  0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    fun cutToMonth(d:Date): Date{
        val calendar = Calendar.getInstance()
        calendar.time = d

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1,  0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    fun equalMonth(d1:Date, d2:Date): Boolean{
        val calendar = Calendar.getInstance()
        calendar.time = d1
        val year:Int
        val month:Int
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        calendar.time = d2
        return (year == calendar.get(Calendar.YEAR) &&
                month == calendar.get(Calendar.MONTH))
    }

    fun cutToYear(d:Date): Date{
        val calendar = Calendar.getInstance()
        calendar.time = d
        calendar.set(calendar.get(Calendar.YEAR), 0, 1,  0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    fun equalYear(d1:Date, d2:Date): Boolean{
        val calendar = Calendar.getInstance()
        calendar.time = d1
        val year = calendar.get(Calendar.YEAR)
        calendar.time = d2
        return year == calendar.get(Calendar.YEAR)
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun cutToWeek(d:Date): Date{
        val calendar = Calendar.getInstance()
        calendar.time = d
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),  0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.setWeekDate(calendar.weekYear, calendar.get(Calendar.WEEK_OF_YEAR), 1)
        return calendar.time
    }

    fun equalWeek(d1: Date, d2: Date): Boolean{
        val calendar1=Calendar.getInstance()
        val calendar2=Calendar.getInstance()
        calendar1.time=d1
        calendar2.time=d2
        return calendar1.get(Calendar.WEEK_OF_YEAR) ==calendar2.get(Calendar.WEEK_OF_YEAR)
    }

    fun getXAxisDate(d: Date, timeMode: TimeMode): String{
        val calendar = Calendar.getInstance()
        val cYear=calendar.get(Calendar.YEAR)
        calendar.time=d
        return when(timeMode){
            TimeMode.Day -> {
                if(calendar.get(Calendar.YEAR)==cYear){
                    "${calendar.get(Calendar.MONTH)+1}.${calendar.get(Calendar.DAY_OF_MONTH)}"
                } else {
                    "${calendar.get(Calendar.YEAR)%10}.${calendar.get(Calendar.MONTH)+1}.${calendar.get(Calendar.DAY_OF_MONTH)}"
                }
            }
            TimeMode.Week -> {
                "第${calendar.get(Calendar.WEEK_OF_YEAR)}周"
            }
            TimeMode.Month -> {
                "${calendar.get(Calendar.YEAR)}.${calendar.get(Calendar.MONTH)+1}"
            }
            TimeMode.Year -> {
                "${calendar.get(Calendar.YEAR)}"
            }
        }

    }

    const val TAG = "DC"
}