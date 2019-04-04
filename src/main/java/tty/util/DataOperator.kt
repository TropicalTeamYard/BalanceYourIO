package tty.util

import android.content.Context
import tty.balanceyourio.R

class DataOperator{
    companion object {
        fun getIncomeClassList(context: Context):ArrayList<String>{
            val incomeData=ArrayList<String>()
            incomeData.add(context.getString(R.string.food))
            return incomeData
        }
    }
}