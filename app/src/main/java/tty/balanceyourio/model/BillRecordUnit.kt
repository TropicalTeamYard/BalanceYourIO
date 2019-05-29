package tty.balanceyourio.model

import java.util.*
import kotlin.collections.ArrayList

/**
 * 记录的集合，用于统计一个时间段的记录
 */
class BillRecordUnit
{
    lateinit var startTime: Date
    lateinit var timeMode: TimeMode

    var data:ArrayList<BillRecord> = ArrayList()

    fun isEmpty():Boolean{
        return data.isEmpty()
    }

    var outcomeSum = 0.0
    var incomeSum = 0.0
    var othersSum = 0.0
}





