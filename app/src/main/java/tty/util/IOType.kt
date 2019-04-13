package tty.util

import android.view.ViewGroup

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
                BYIONode("餐饮",0,
                    BYIONodeItem("三餐",0),
                    BYIONodeItem("零食",0),
                    BYIONodeItem("水果",0)
                ),
                BYIONode("交通",0,
                    BYIONodeItem("公交",0,false),
                    BYIONodeItem("地铁",0,false),
                    BYIONodeItem("飞机",0,false),
                    BYIONodeItem("火车/高铁",0,false)
                ),
                BYIONode("购物",0,
                    BYIONodeItem("电子产品",0),
                    BYIONodeItem("日用平",0),
                    BYIONodeItem("玩具",0),
                    BYIONodeItem("衣物",0),
                    BYIONodeItem("奢侈品",0),
                    BYIONodeItem("其他",0)),
                BYIONode("投资",0),
                BYIONode("娱乐",0),
                BYIONode("文教",0),
                BYIONode("其他",0)
            ),
            BYIOType(
                BYIONode("工资",0),
                BYIONode("投资",0),
                BYIONode("利息",0)
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
            for (nodeitem in node.items)
                str+= "    - ${nodeitem.name}\n"
        }
        return str
    }
}

class BYIONode(var name: String, var icon: Int, vararg items: BYIONodeItem) {
    var items:List<BYIONodeItem> = items.toList()
}

class BYIONodeItem(var name:String,var icon:Int,var state:Boolean = true)
{
}