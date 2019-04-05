package tty.util

import android.content.Context
import android.graphics.BitmapFactory
import tty.balanceyourio.R

class DataOperator{
    companion object {
        fun getOutcomeClassList(context: Context):ArrayList<HashMap<String, Any>>{
            val outcomeData=ArrayList<HashMap<String, Any>>()
            var map=HashMap<String, Any>()
            map["class"] = context.getString(R.string.food)
            map["icon"] = BitmapFactory.decodeResource(context.resources,R.drawable.fork)
            outcomeData.add(map)
            map=HashMap()
            map["class"] = context.getString(R.string.traffic)
            map["icon"] = BitmapFactory.decodeResource(context.resources,R.drawable.fork)
            outcomeData.add(map)
            map=HashMap()
            map["class"] = context.getString(R.string.education)
            map["icon"] = BitmapFactory.decodeResource(context.resources,R.drawable.fork)
            outcomeData.add(map)
            return outcomeData
        }

        fun getIncomeClassList(context: Context):ArrayList<HashMap<String, Any>>{
            val incomeData=ArrayList<HashMap<String, Any>>()
            var map=HashMap<String, Any>()
            map["class"] = context.getString(R.string.part_time)
            map["icon"] = BitmapFactory.decodeResource(context.resources,R.drawable.swap)
            incomeData.add(map)
            map=HashMap()
            map["class"] = context.getString(R.string.wages)
            map["icon"] = BitmapFactory.decodeResource(context.resources,R.drawable.swap)
            incomeData.add(map)
            map=HashMap()
            map["class"] = context.getString(R.string.living_expenses)
            map["icon"] = BitmapFactory.decodeResource(context.resources,R.drawable.swap)
            incomeData.add(map)
            return incomeData
        }
    }
}