package tty.util

import android.content.Context
import android.graphics.BitmapFactory
import tty.balanceyourio.R

class DataOperator{
    companion object {
        private fun getFriendString(context:Context,input:String):String{
            val value: String
            value = if (input.startsWith("key.")){
                val key:Int? =  input.substring(4).toIntOrNull()
                if (key != null)
                    context.getString(key)
                else
                    "KeyNotFound"
            } else {
                input
            }
            return value
        }

        fun getOutcomeTypeList(context: Context):ArrayList<HashMap<String, Any>>{
            val outcomeData=ArrayList<HashMap<String, Any>>()
            for (node in BYIOCategory.getInstance().outcome){
                val map = HashMap<String,Any>()
                map["class"] = getFriendString(context,node.name)
                //TODO("需要重写图标逻辑")
                map["icon"] = BitmapFactory.decodeResource(context.resources,R.drawable.fork)
                map["chosen"] = false
                outcomeData.add(map)
            }
            return outcomeData

//            var map=HashMap<String, Any>()
//            map["class"] = context.getString(R.string.food)
//            map["icon"] = BitmapFactory.decodeResource(context.resources,R.drawable.fork)
//            outcomeData.add(map)
//            map=HashMap()
//            map["class"] = context.getString(R.string.traffic)
//            map["icon"] = BitmapFactory.decodeResource(context.resources,R.drawable.fork)
//            outcomeData.add(map)
//            map=HashMap()
//            map["class"] = context.getString(R.string.education)
//            map["icon"] = BitmapFactory.decodeResource(context.resources,R.drawable.fork)
//            outcomeData.add(map)
//            return outcomeData
        }

        fun getIncomeTypeList(context: Context):ArrayList<HashMap<String, Any>>{
            val incomeData=ArrayList<HashMap<String, Any>>()
            for (node in BYIOCategory.getInstance().income){
                val map = HashMap<String,Any>()
                map["class"] = getFriendString(context,node.name)
                //TODO("需要重写图标逻辑")
                map["icon"] = BitmapFactory.decodeResource(context.resources,R.drawable.swap)
                map["chosen"] = false
                incomeData.add(map)
            }

            return incomeData

//            var map=HashMap<String, Any>()
//            map["class"] = context.getString(R.string.part_time)
//            map["icon"] = BitmapFactory.decodeResource(context.resources,R.drawable.swap)
//            incomeData.add(map)
//            map=HashMap()
//            map["class"] = context.getString(R.string.wages)
//            map["icon"] = BitmapFactory.decodeResource(context.resources,R.drawable.swap)
//            incomeData.add(map)
//            map=HashMap()
//            map["class"] = context.getString(R.string.living_expenses)
//            map["icon"] = BitmapFactory.decodeResource(context.resources,R.drawable.swap)
//            incomeData.add(map)
//            return incomeData
        }
    }
}