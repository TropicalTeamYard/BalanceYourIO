package tty.balanceyourio.model

import tty.balanceyourio.util.DateConverter
import java.util.*

/**
 * 记录的集合，用于统计一个时间段的记录
 */
class BillRecordUnit
{
    lateinit var startTime:Date
    lateinit var timeMode: TimeMode

    lateinit var data:ArrayList<BillRecord>

    fun isEmpty():Boolean{
        return data.size == 0
    }

    var outcomeSum = 0.0
    var incomeSum = 0.0
    var othersSum = 0.0
}





