package tty.balanceyourio.data

import android.util.Log
import android.util.SparseArray
import tty.balanceyourio.model.BillRecord
import tty.balanceyourio.model.IOType
import tty.balanceyourio.model.TimeMode
import tty.balanceyourio.model.Tuple
import tty.balanceyourio.util.DateConverter
import tty.balanceyourio.widget.ShowBillChart
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object BillRecordsProvider {

    fun getBillRecordsByTimeMode(billRecords: ArrayList<BillRecord>, timeMode: TimeMode): Tuple<ArrayList<ArrayList<BillRecord>>, ArrayList<HashMap<IOType, Double>>, ArrayList<Date>> {
        val timeModeBill=ArrayList<ArrayList<BillRecord>>()
        val timeModeSum=ArrayList<HashMap<IOType, Double>>()
        val timeModeList: ArrayList<Date>

        when(timeMode){
            TimeMode.Day -> {
                val dateSet= HashSet<Date>()
                for(i in 0 until billRecords.size){
                    dateSet.add(DateConverter.cutToDate(billRecords[i].time!!))
                }
                timeModeList = ArrayList(dateSet)
                Log.d(ShowBillChart.TAG, "date size: ${timeModeList.size}")
                timeModeList.sort()
                timeModeList.reverse()
                for(i in 0 until timeModeList.size){
                    timeModeSum.add(HashMap())
                    timeModeSum[i][IOType.Income]=0.0
                    timeModeSum[i][IOType.Outcome]=0.0
                    timeModeSum[i][IOType.Unset]=0.0
                    val tData=ArrayList<BillRecord>()
                    for(j in 0 until billRecords.size){
                        if(DateConverter.equalDate(billRecords[j].time!!, timeModeList[i])){
                            tData.add(billRecords[j])
                            when(billRecords[j].ioType){
                                IOType.Income -> timeModeSum[i][IOType.Income] = timeModeSum[i][IOType.Income]!! + billRecords[j].amount
                                IOType.Outcome -> timeModeSum[i][IOType.Outcome] = timeModeSum[i][IOType.Outcome]!! + billRecords[j].amount
                                else -> timeModeSum[i][IOType.Unset] = timeModeSum[i][IOType.Unset]!! + billRecords[j].amount
                            }
                        }
                    }

                    tData.sortByDescending { it.time }
                    timeModeBill.add(tData)
                }
            }

            TimeMode.Week -> {
                val weekSet= HashSet<Date>()
                for(i in 0 until billRecords.size){
                    weekSet.add(DateConverter.cutToWeek(billRecords[i].time!!))
                }
                timeModeList = ArrayList(weekSet)
                Log.d(ShowBillChart.TAG, "week size: ${timeModeList.size}")
                timeModeList.sort()
                timeModeList.reverse()
                for(i in 0 until timeModeList.size){
                    timeModeSum.add(HashMap())
                    timeModeSum[i][IOType.Income]=0.0
                    timeModeSum[i][IOType.Outcome]=0.0
                    timeModeSum[i][IOType.Unset]=0.0
                    val tData=ArrayList<BillRecord>()
                    for(j in 0 until billRecords.size){
                        if(DateConverter.equalWeek(billRecords[j].time!!, timeModeList[i])){
                            tData.add(billRecords[j])
                            when(billRecords[j].ioType){
                                IOType.Income -> timeModeSum[i][IOType.Income] = timeModeSum[i][IOType.Income]!! + billRecords[j].amount
                                IOType.Outcome -> timeModeSum[i][IOType.Outcome] = timeModeSum[i][IOType.Outcome]!! + billRecords[j].amount
                                else -> timeModeSum[i][IOType.Unset] = timeModeSum[i][IOType.Unset]!! + billRecords[j].amount
                            }
                        }
                    }

                    tData.sortByDescending { it.time }
                    timeModeBill.add(tData)
                }
            }

            TimeMode.Month -> {
                val monthSet= HashSet<Date>()
                for(i in 0 until billRecords.size){
                    monthSet.add(DateConverter.cutToMonth(billRecords[i].time!!))
                }
                timeModeList = ArrayList(monthSet)
                Log.d(ShowBillChart.TAG, "month size: ${timeModeList.size}")
                timeModeList.sort()
                timeModeList.reverse()
                for(i in 0 until timeModeList.size){
                    timeModeSum.add(HashMap())
                    timeModeSum[i][IOType.Income]=0.0
                    timeModeSum[i][IOType.Outcome]=0.0
                    timeModeSum[i][IOType.Unset]=0.0
                    val tData=ArrayList<BillRecord>()
                    for(j in 0 until billRecords.size){
                        if(DateConverter.equalMonth(billRecords[j].time!!, timeModeList[i])){
                            tData.add(billRecords[j])
                            when(billRecords[j].ioType){
                                IOType.Income -> timeModeSum[i][IOType.Income] = timeModeSum[i][IOType.Income]!! + billRecords[j].amount
                                IOType.Outcome -> timeModeSum[i][IOType.Outcome] = timeModeSum[i][IOType.Outcome]!! + billRecords[j].amount
                                else -> timeModeSum[i][IOType.Unset] = timeModeSum[i][IOType.Unset]!! + billRecords[j].amount
                            }
                        }
                    }

                    tData.sortByDescending { it.time }
                    timeModeBill.add(tData)
                }
            }

            TimeMode.Year -> {
                val yearSet= HashSet<Date>()
                for(i in 0 until billRecords.size){
                    yearSet.add(DateConverter.cutToYear(billRecords[i].time!!))
                }
                timeModeList = ArrayList(yearSet)
                Log.d(ShowBillChart.TAG, "year size: ${timeModeList.size}")
                timeModeList.sort()
                timeModeList.reverse()
                for(i in 0 until timeModeList.size){
                    timeModeSum.add(HashMap())
                    timeModeSum[i][IOType.Income]=0.0
                    timeModeSum[i][IOType.Outcome]=0.0
                    timeModeSum[i][IOType.Unset]=0.0
                    val tData=ArrayList<BillRecord>()
                    for(j in 0 until billRecords.size){
                        if(DateConverter.equalYear(billRecords[j].time!!, timeModeList[i])){
                            tData.add(billRecords[j])
                            when(billRecords[j].ioType){
                                IOType.Income -> timeModeSum[i][IOType.Income] = timeModeSum[i][IOType.Income]!! + billRecords[j].amount
                                IOType.Outcome -> timeModeSum[i][IOType.Outcome] = timeModeSum[i][IOType.Outcome]!! + billRecords[j].amount
                                else -> timeModeSum[i][IOType.Unset] = timeModeSum[i][IOType.Unset]!! + billRecords[j].amount
                            }
                        }
                    }

                    tData.sortByDescending { it.time }
                    timeModeBill.add(tData)
                }
            }
        }

        return Tuple(timeModeBill, timeModeSum, timeModeList)
    }

    fun getIOSumByGoodsType(billRecords: ArrayList<BillRecord>): SparseArray<Float> {
        val result = SparseArray<Float>()
        for(bill: BillRecord in billRecords){

        }
        return result
    }

}

enum class ArrayType{
    Sum,
    List,
    Bill
}