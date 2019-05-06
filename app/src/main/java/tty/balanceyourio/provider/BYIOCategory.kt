package tty.balanceyourio.provider

import tty.balanceyourio.R
import tty.balanceyourio.converter.CatagoryConverter
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
//        init {
//            nameDic.
//        }
//
//        private val nameDic:HashMap<Int,String> = HashMap();

        private val default: BYIOCategory = BYIOCategory(
            //outcome branch
            BYIOType(
                BYIONode("key." + CatagoryConverter.Food, PxlIconConverter.CARD),
                BYIONode("key." + CatagoryConverter.Fruit, PxlIconConverter.WASH),
                BYIONode("key." + CatagoryConverter.Transportation, PxlIconConverter.CAR),
                BYIONode("key." + CatagoryConverter.Shopping, PxlIconConverter.CALENDAR),
                BYIONode("key." + CatagoryConverter.Investment, PxlIconConverter.EXCHANGE),
                BYIONode("key." + CatagoryConverter.Entertainment, PxlIconConverter.GAME),
                BYIONode("key." + CatagoryConverter.Education, PxlIconConverter.CAMERA),
                BYIONode("key." + CatagoryConverter.Others, PxlIconConverter.COSMETOLOGY)
            ),
            BYIOType(
                BYIONode("key." + CatagoryConverter.Wages, 0),
                BYIONode("key." + CatagoryConverter.PartTime, 0),
                BYIONode("key." + CatagoryConverter.Interest, 0)
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
