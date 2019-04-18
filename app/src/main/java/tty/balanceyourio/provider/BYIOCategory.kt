package tty.balanceyourio.provider

import tty.balanceyourio.R
import tty.balanceyourio.converter.ColorIconConverter
import tty.balanceyourio.converter.PxlIconConverter

class BYIOCategory(var outcome: BYIOType, var income: BYIOType)
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
        private val default: BYIOCategory = BYIOCategory(
            //outcome branch
            BYIOType(
                BYIONode("key." + R.string.food, PxlIconConverter.CARD),
                BYIONode("key." + R.string.fruit, PxlIconConverter.WASH),
                BYIONode("key." + R.string.transportation, PxlIconConverter.CAR),
                BYIONode("key." + R.string.shopping, PxlIconConverter.CALENDAR),
                BYIONode("key." + R.string.investment, PxlIconConverter.EXCHANGE),
                BYIONode("key." + R.string.entertainment, PxlIconConverter.GAME),
                BYIONode("key." + R.string.education, PxlIconConverter.CAMERA),
                BYIONode("key." + R.string.others, PxlIconConverter.COSMETOLOGY)
            ),
            BYIOType(
                BYIONode("key." + R.string.wages, 0),
                BYIONode("key." + R.string.part_time, 0),
                BYIONode("key." + R.string.interest, 0)
            )
        )

        private var current: BYIOCategory? = null

        fun getInstance(): BYIOCategory
        {
            if (current == null)
                current =
                    default
            return current!!
        }
    }
}

/**
 * 用于支持输入输出类型的操作
 */
class BYIOType(vararg items: BYIONode):Iterable<BYIONode>
{
    override fun iterator(): Iterator<BYIONode> {
        return items.iterator()
    }

    private var items:List<BYIONode> = items.toList()

    override fun toString(): String {
        var str =""
        for (node in items){
            str += "- ${node.name}\n"
        }
        return str
    }
}

class BYIONode(var name: String, var icon: Int)
