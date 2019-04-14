package tty.util

import android.view.ViewGroup
import tty.balanceyourio.R

class BYIOCategory(var outcome:BYIOType,var income:BYIOType)
{
    override fun toString(): String {
        var str = ""
        str+="-----支出-----"
        str+=outcome.toString()
        str+="-----收入-----"
        str+=income.toString()

        return str
    }

    companion object {
        val default:BYIOCategory = BYIOCategory(
            //outcome branch
            BYIOType(
                BYIONode("key." + R.string.food,0),
                BYIONode("key." + R.string.fruit,0),
                BYIONode("key." + R.string.transportation,0),
                BYIONode("key." + R.string.shopping,0),
                BYIONode("key." + R.string.investment,0),
                BYIONode("key." + R.string.entertainment,0),
                BYIONode("key." + R.string.education,0),
                BYIONode("key." + R.string.others,0)
            ),
            BYIOType(
                BYIONode("key." + R.string.wages,0),
                BYIONode("key." + R.string.part_time,0),
                BYIONode("key." + R.string.interest,0)
            )
        )

        private var current:BYIOCategory? = null

        fun getInstance():BYIOCategory
        {
            if (current== null)
                current = default
            return current!!
        }
    }
}

/**
 * 用于支持输入输出类型的操作
 */
class BYIOType(vararg items:BYIONode):Iterable<BYIONode>
{
    override fun iterator(): Iterator<BYIONode> {
        return items.iterator()
    }

    var items:List<BYIONode> = items.toList()

    override fun toString(): String {
        var str:String =""
        for (node in items){
            str += "- ${node.name}\n"
        }
        return str
    }
}

class BYIONode(var name: String, var icon: Int)
