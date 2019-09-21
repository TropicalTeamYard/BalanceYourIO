package tty.balanceyourio.model

import java.util.*

/**
 * 一条账目记录
 */
class BillRecord {
    /**
     * 账目的id，若要向数据库添加一条记录，请指定id为-1
     */
    var id: Int = 0
    /**
     * 目前为缺省状态，在后期用于?replaceHolder?
     */
    var tag: String? = null
    /**
     * 账目的时间，在这里指添加记录的时间
     */
    var time: Date? = null
    /**
     * 账目的类型，为IOType枚举值，具体请看
     * @see IOType
     * 在数据库中 INCOME=1, OUTCOME=2, OTHER=0
     */
    var ioType: IOType = IOType.Unset
    /**
     *
     */
    var goodsType: String? = null
    /**
     * 账目的金额，在这里应该为非负值
     */
    var amount: Double = 0.0
    /**
     * 交易的渠道，目前为缺省状态?replaceHolder?
     */
    var channel: String? = null
    /**
     * 账目的备注
     */
    var remark: String? = null

    /**
     * 当前位置，主要用于和临时状态进行交互
     */
    var outcomeP: Int = -1
    var incomeP: Int = -1
    var otherP: Int = -1
    //var selectedIOType:IOType = IOType.Outcome

    val displayRemark: String
        get() {
            return toDisplayRemark(remark)
        }


    fun init() {
        id = -1
        tag = "#UNSET"
        time = Date()
        ioType = IOType.Outcome
        goodsType = null
        amount = 4.0
        channel = "#UNSET"
        remark = null
    }

    companion object {
        fun toDisplayRemark(remark: String?): String {
            return if (remark == null || remark == "") {
                "（无）"
            } else {
                remark
            }
        }

        const val defaultAmount: Double = 4.0
    }
}