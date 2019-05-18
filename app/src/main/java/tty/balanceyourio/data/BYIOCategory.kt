package tty.balanceyourio.data

import tty.balanceyourio.converter.CategoryConverter

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

    fun getIconIndex(name:String):Int
    {
        for (i in tty.balanceyourio.data.BYIOCategory.getInstance().outcome){
            if (i.name == name)
                return i.icon
        }
        for (i in tty.balanceyourio.data.BYIOCategory.getInstance().income){
            if (i.name == name)
                return i.icon
        }
        return -1
    }

    companion object {
        private val default: BYIOCategory = BYIOCategory(
            //outcome branch
            BYIOType(
                BYIONode("key." + CategoryConverter.Food, 15),
                BYIONode("key." + CategoryConverter.Fruit, 17),
                BYIONode("key." + CategoryConverter.Transportation, 2),
                BYIONode("key." + CategoryConverter.Shopping, 16),
                BYIONode("key." + CategoryConverter.Investment, 18),
                BYIONode("key." + CategoryConverter.Entertainment, 9),
                BYIONode("key." + CategoryConverter.Education, 14),
                BYIONode("key." + CategoryConverter.Others, 19)
            ),
            BYIOType(
                BYIONode("key." + CategoryConverter.Wages, 19),
                BYIONode("key." + CategoryConverter.PartTime, 19),
                BYIONode("key." + CategoryConverter.Interest, 19)
            )
        )

        private var current: BYIOCategory? = null

        fun getInstance(): BYIOCategory
        {
            if (current == null)
                current = default
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
