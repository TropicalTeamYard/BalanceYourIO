package tty.balanceyourio.provider

import android.content.Context
import tty.balanceyourio.data.BYIOCategory

class IOTypeProvider(var context: Context){



    val outComeTypeList:ArrayList<HashMap<String,Any>>
    get() {
        val list = ArrayList<HashMap<String,Any>>()
        for (node in BYIOCategory.getInstance().outcome){
            val map = HashMap<String,Any>()
            map["type"] = 1
            map["class"] = node.name
            map["icon"] = node.icon
            map["chosen"] = false
            list.add(map)
        }
        return list
    }

    val inComeTypeList:ArrayList<HashMap<String,Any>>
    get() {
        val list = ArrayList<HashMap<String,Any>>()
        for (node in BYIOCategory.getInstance().income){
            val map = HashMap<String,Any>()
            map["type"] = 0
            map["class"] = node.name
            map["icon"] = node.icon
            map["chosen"] = false
            list.add(map)
        }
        return list
    }
}