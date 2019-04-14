package tty.model

import java.util.*

class BillRecord
{
    var id:Int = 0
    var tag:String? = null
    var time:Date? = null
    var ioType:IOType = IOType.Unset
    var goodsType:String? = null
    var amount:Double? = 0.0
    var channel:String? = null
    var remark:String? = null
}