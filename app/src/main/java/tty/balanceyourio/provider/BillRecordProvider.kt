package tty.balanceyourio.provider

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import tty.balanceyourio.data.BYIOHelper
import tty.balanceyourio.model.BillRecord
import tty.balanceyourio.model.BillRecordUnit
import tty.balanceyourio.model.IOType
import tty.balanceyourio.model.TimeMode
import tty.balanceyourio.util.DateConverter
import java.util.*
import kotlin.collections.ArrayList

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
        private fun joinGroupByDay(start:Date, end:Date,data:ArrayList<BillRecord>,sort:Boolean = true):ArrayList<BillRecordUnit>{
            if (sort)
                data.sortByDescending { it.time }

            var cTime:Date? = null
            val calendar = Calendar.getInstance()

            var cBillRecordUnit :BillRecordUnit? = null
            val result = ArrayList<BillRecordUnit>()
            for (i in 0 until data.size){
                if (cTime == null || DateConverter.cutToDate(data[i].time!!) != cTime){
                    if (cTime == null)
                        cTime = end
                    else
                    {
                        calendar.time = cTime
                        calendar.add(Calendar.DATE,-1)
                        cTime = calendar.time
                    }

                    if (cBillRecordUnit!=null)
                        result.add(cBillRecordUnit)

                    cBillRecordUnit = BillRecordUnit()
                    cBillRecordUnit.timeMode = TimeMode.Day
                    cBillRecordUnit.startTime = cTime!!
                    cBillRecordUnit.incomeSum = 0.0
                    cBillRecordUnit.outcomeSum = 0.0
                    cBillRecordUnit.othersSum = 0.0
                }
                if (cTime == data[i].time){
                    cBillRecordUnit!!.data.add(data[i])
                    when(data[i].ioType){
                        IOType.Income->{
                            cBillRecordUnit.incomeSum += data[i].amount
                        }
                        IOType.Outcome->{
                            cBillRecordUnit.outcomeSum += data[i].amount
                        }
                        else->{
                            cBillRecordUnit.othersSum += data[i].amount
                        }
                    }
                }
            }
            while (cTime != null && cTime >= start)
            {
                calendar.time = cTime
                calendar.add(Calendar.DATE,-1)
                cBillRecordUnit = BillRecordUnit()
                cBillRecordUnit.timeMode = TimeMode.Day
                cBillRecordUnit.startTime = cTime
                cBillRecordUnit.incomeSum = 0.0
                cBillRecordUnit.outcomeSum = 0.0
                cBillRecordUnit.othersSum = 0.0
                result.add(cBillRecordUnit)
            }

            return result
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun clearEmptyUnit(data:ArrayList<BillRecordUnit>)
        {
            data.removeIf{ it.isEmpty() }
        }
    }
}