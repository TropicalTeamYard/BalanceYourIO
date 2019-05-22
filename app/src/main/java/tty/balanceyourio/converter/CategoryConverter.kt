package tty.balanceyourio.converter

import tty.balanceyourio.R


class CategoryConverter: RConverter{
    override fun getResID(index: Int): Int {
        return when(index){
            Education -> R.string.education
            Entertainment -> R.string.entertainment
            Food -> R.string.food
            Fruit -> R.string.fruit
            Interest -> R.string.interest
            Investment -> R.string.investment
            LivingExpenses -> R.string.living_expenses
            Others -> R.string.others
            PartTime -> R.string.part_time
            Shopping -> R.string.shopping
            Wages -> R.string.wages
            Transportation -> R.string.transportation
            Repayment -> R.string.repayment
            Recharge -> R.string.recharge
            else -> -1
        }
    }

    override val count: Int
        get() = 11


    companion object {
        const val Education = 0
        const val Entertainment = 1
        const val Food = 2
        const val Fruit = 3
        const val Interest = 4
        const val Investment = 5
        const val LivingExpenses = 6
        const val Others = 7
        const val PartTime = 8
        const val Shopping = 9
        const val Wages = 10
        const val Transportation = 11
        const val Repayment = 12
        const val Recharge = 13
    }
}