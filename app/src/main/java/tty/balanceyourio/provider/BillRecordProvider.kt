package tty.balanceyourio.provider

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import tty.balanceyourio.data.BYIOHelper
import tty.balanceyourio.model.*
import tty.balanceyourio.util.DateConverter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BillRecordProvider(var context:Context)
{
    //var data:ArrayList<BillRecord>?=null

    /**
     * 获取所有的记账记录
     * 不推荐使用该方法，该方法只用于测试
     */
    fun getAllBillRecord():ArrayList<BillRecord>{
        return BYIOHelper(context).getBill()
    }

    /**
     * 获取最近days天的记账记录
     * @param days 最近的天数，包括今天
     */
    fun getLatestBillRecords(days:Int = 7):ArrayList<BillRecordUnit>
    {
        return ArrayList()
    }

    /**
     * 获取所有的记账记录按照天分组，该方法仅用于前期测试
     */
    fun getAllBillRecordsGroupByDay():ArrayList<BillRecordUnit>{
        val data:ArrayList<BillRecord> = BYIOHelper(context).getBill()

        if (data.size > 0)
        {
            data.sortByDescending { it.time }
            return joinGroupByDay(data.last().time!!,data.first().time!!,data,false)
        }
        else {
            return ArrayList()
        }
    }

    fun getBillRecordsGroupByDay(start:Date, end:Date):ArrayList<BillRecordUnit> {
        val data:ArrayList<BillRecord> = BYIOHelper(context).getBill(start,end)
        //data.sortByDescending { it.time }
        return joinGroupByDay(start,end,data)
    }

    companion object{
        fun joinGroupByDay(data:ArrayList<BillRecord>):ArrayList<BillRecordUnit>{

            data.sortByDescending { it.time }

            var start :Date = DateConverter.cutToDate(Date())
            var end:Date = DateConverter.cutToDate(Date())

            if (!data.isEmpty()){
                start = DateConverter.cutToDate( data.last().time!!)
                end = if (DateConverter.cutToDate(data.first().time!!) > end) DateConverter.cutToDate(data.first().time!!) else end
            }

            val calendar = Calendar.getInstance()
            calendar.time = end
            calendar.add(Calendar.DATE,-7)
            start = if (calendar.time < start) calendar.time else start

            return joinGroupByDay(start,end,data,false)
        }
        fun joinGroupByDay(start:Date, end:Date,data:ArrayList<BillRecord>,sort:Boolean = true):ArrayList<BillRecordUnit>{
            if (sort)
                data.sortByDescending { it.time }

            val cTime:Calendar = Calendar.getInstance()
            cTime.time = DateConverter.cutToDate(end)
            val result= ArrayList<BillRecordUnit>()
            var index = 0

            while (cTime.time >= DateConverter.cutToDate(start))
            {
                val selected=ArrayList<BillRecord>()

                while(index < data.size && DateConverter.cutToDate(data[index].time!!) >= cTime.time){
                    if (DateConverter.cutToDate( data[index].time !!) == cTime.time){
                        selected.add(data[index])
                    }
                    index ++;
                }

                val unit = BillRecordUnit()
                unit.data = selected
                unit.timeMode = TimeMode.Day
                unit.startTime = cTime.time
                unit.incomeSum = selected.sumByDouble {
                    if (it.ioType == IOType.Income)
                        it.amount
                    else 0.0
                }
                unit.outcomeSum = selected.sumByDouble {
                    if (it.ioType == IOType.Outcome)
                        it.amount
                    else 0.0
                }

                result.add(unit)

                cTime.add(Calendar.DATE,-1)
            }
            return result
        }
        fun joinGroupByWeek(data:ArrayList<BillRecord>):ArrayList<BillRecordUnit>{
            data.sortByDescending { it.time }

            var start :Date = DateConverter.cutToWeek(Date())
            var end:Date = DateConverter.cutToWeek(Date())
            if (!data.isEmpty()){
                start = DateConverter.cutToWeek( data.last().time!!)
                end = if (DateConverter.cutToWeek(data.first().time!!) > end) DateConverter.cutToWeek(data.first().time!!) else end
            }

            val calendar = Calendar.getInstance()
            calendar.time = end
            calendar.add(Calendar.WEEK_OF_YEAR,-5)

            start = if (calendar.time < start) calendar.time else start

            return joinGroupByWeek(start,end,data,false)
        }
        fun joinGroupByWeek(start:Date, end:Date, data:ArrayList<BillRecord>, sort:Boolean = true):ArrayList<BillRecordUnit>{
            if (sort)
                data.sortByDescending { it.time }

            val cTime:Calendar = Calendar.getInstance()
            cTime.time = DateConverter.cutToWeek(end)
            val result= ArrayList<BillRecordUnit>()
            var index = 0

            while (cTime.time >= DateConverter.cutToWeek(start))
            {
                val selected=ArrayList<BillRecord>()

                while(index < data.size && DateConverter.cutToWeek(data[index].time!!) >= cTime.time){
                    if (DateConverter.cutToWeek( data[index].time !!) == cTime.time){
                        selected.add(data[index])
                    }
                    index ++;
                }

                val unit = BillRecordUnit()
                unit.data = selected
                unit.timeMode = TimeMode.Day
                unit.startTime = cTime.time
                unit.incomeSum = selected.sumByDouble {
                    if (it.ioType == IOType.Income)
                        it.amount
                    else 0.0
                }
                unit.outcomeSum = selected.sumByDouble {
                    if (it.ioType == IOType.Outcome)
                        it.amount
                    else 0.0
                }

                result.add(unit)

                cTime.add(Calendar.WEEK_OF_YEAR,-1)
            }
            return result
        }
        fun joinGroupByMonth(data:ArrayList<BillRecord>):ArrayList<BillRecordUnit>{
            data.sortByDescending { it.time }

            var start :Date = DateConverter.cutToMonth(Date())
            var end:Date = DateConverter.cutToMonth(Date())
            if (!data.isEmpty()){
                start = DateConverter.cutToMonth( data.last().time!!)
                end = if (DateConverter.cutToMonth(data.first().time!!) > end) DateConverter.cutToMonth(data.first().time!!) else end
            }

            val calendar = Calendar.getInstance()
            calendar.time = end
            calendar.add(Calendar.MONTH,-4)

            start = if (calendar.time < start) calendar.time else start

            return joinGroupByMonth(start,end,data,false)
        }
        fun joinGroupByMonth(start:Date,end:Date,data:ArrayList<BillRecord>,sort:Boolean = true):ArrayList<BillRecordUnit>{
            if (sort)
                data.sortByDescending { it.time }

            val cTime:Calendar = Calendar.getInstance()
            cTime.time = DateConverter.cutToMonth(end)
            val result= ArrayList<BillRecordUnit>()
            var index = 0
            while (cTime.time >= DateConverter.cutToMonth(start))
            {
                val selected=ArrayList<BillRecord>()

                while(index < data.size && DateConverter.cutToMonth(data[index].time!!) >= cTime.time){
                    if (DateConverter.cutToMonth( data[index].time !!) == cTime.time){
                        selected.add(data[index])
                    }
                    index ++;
                }

                val unit = BillRecordUnit()
                unit.data = selected
                unit.timeMode = TimeMode.Day
                unit.startTime = cTime.time
                unit.incomeSum = selected.sumByDouble {
                    if (it.ioType == IOType.Income)
                        it.amount
                    else 0.0
                }
                unit.outcomeSum = selected.sumByDouble {
                    if (it.ioType == IOType.Outcome)
                        it.amount
                    else 0.0
                }

                result.add(unit)

                cTime.add(Calendar.MONTH,-1)
            }
            return result
        }
        fun joinGroupByYear(data:ArrayList<BillRecord>):ArrayList<BillRecordUnit>{
            data.sortByDescending { it.time }

            var start :Date = DateConverter.cutToYear(Date())
            var end:Date = DateConverter.cutToYear(Date())
            if (!data.isEmpty()){
                start = DateConverter.cutToYear( data.last().time!!)
                end = if (DateConverter.cutToYear(data.first().time!!) > end) DateConverter.cutToYear(data.first().time!!) else end
            }


            val calendar = Calendar.getInstance()
            calendar.time = end
            calendar.add(Calendar.YEAR,-3)

            start = if (calendar.time < start) calendar.time else start

            return joinGroupByYear(start,end,data,false)
        }
        fun joinGroupByYear(start:Date,end:Date,data:ArrayList<BillRecord>,sort:Boolean = true):ArrayList<BillRecordUnit>{
            if (sort)
                data.sortByDescending { it.time }

            val cTime:Calendar = Calendar.getInstance()
            cTime.time = DateConverter.cutToYear(end)
            val result= ArrayList<BillRecordUnit>()
            var index = 0
            while (cTime.time >= DateConverter.cutToYear(start))
            {
                val selected=ArrayList<BillRecord>()

                while(index < data.size && DateConverter.cutToYear(data[index].time!!) >= cTime.time){
                    if (DateConverter.cutToYear( data[index].time !!) == cTime.time){
                        selected.add(data[index])
                    }
                    index ++;
                }

                val unit = BillRecordUnit()
                unit.data = selected
                unit.timeMode = TimeMode.Day
                unit.startTime = cTime.time
                unit.incomeSum = selected.sumByDouble {
                    if (it.ioType == IOType.Income)
                        it.amount
                    else 0.0
                }
                unit.outcomeSum = selected.sumByDouble {
                    if (it.ioType == IOType.Outcome)
                        it.amount
                    else 0.0
                }

                result.add(unit)

                cTime.add(Calendar.YEAR,-1)
            }
            return result
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun clearEmptyUnit(data:ArrayList<BillRecordUnit>) {
            data.removeIf{ it.isEmpty() }
        }

        //TODO("在DataFragment中进行优化，从而不需要使用转换函数")
        /**
         * 为了兼容wcf的数据分析器而做的转换函数
         */
        fun toAnalysis(data:ArrayList<BillRecordUnit>):Tuple<ArrayList<List<BillRecord>>,ArrayList<HashMap<IOType,Double>>,ArrayList<Date>> {
            val a = ArrayList<List< BillRecord>>()
            val b = ArrayList<HashMap<IOType,Double>>()
            val c = ArrayList<Date>()

            for ( i in 0 until  data.size){
                a.add(data[i].data)
                val map = HashMap<IOType,Double>()
                map[IOType.Income] = data[i].incomeSum
                map[IOType.Outcome] = data[i].outcomeSum
                map[IOType.Other] = data[i].othersSum
                b.add(map)
                c.add(data[i].startTime)
            }

            return Tuple(a,b,c)
        }
    }
}