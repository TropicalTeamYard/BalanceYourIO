package tty.balanceyourio.converter

import tty.balanceyourio.R

/**
 * 在数据库中存储的类型名称用类型对应的数字代替，使用时根据@fun getResId(index: Int)获得res
 */

class PxlIconConverter:RConverter{
    override fun getResID(index: Int): Int {
        return when(index){
            CALENDAR -> R.drawable.ic_pxl_calendar
            CAMERA -> R.drawable.ic_pxl_camera
            CAR -> R.drawable.type_transportation
            CARD -> R.drawable.ic_pxl_card
            CATEGORY -> R.drawable.ic_pxl_category
            CLASSIFY -> R.drawable.ic_pxl_classify
            COSMETOLOGY -> R.drawable.ic_pxl_cosmetology
            EXCHANGE -> R.drawable.ic_pxl_exchange
            FOSTER -> R.drawable.ic_pxl_foster
            GAME -> R.drawable.type_game
            INVITATION -> R.drawable.ic_pxl_invitation
            LOCATION -> R.drawable.ic_pxl_location
            TAG -> R.drawable.ic_pxl_tag
            WASH -> R.drawable.ic_pxl_wash
            EDUCATION -> R.drawable.type_education
            FOOD -> R.drawable.type_food
            SHOPPING -> R.drawable.type_shopping
            FRUIT -> R.drawable.type_fruit
            INVESTMENT -> R.drawable.type_investment
            OTHERS -> R.drawable.type_others
            CREDIT_CARD -> R.drawable.ic_credit_card
            else -> -1
        }
    }

    override val count: Int
        get() = 20


    companion object {
        const val CALENDAR = 0
        const val CAMERA = 1
        const val CAR = 2
        const val CARD = 3
        const val CATEGORY = 4
        const val CLASSIFY = 5
        const val COSMETOLOGY = 6
        const val EXCHANGE = 7
        const val FOSTER = 8
        const val GAME = 9
        const val INVITATION = 10
        const val LOCATION = 11
        const val TAG = 12
        const val WASH = 13
        const val EDUCATION = 14
        const val FOOD = 15
        const val SHOPPING = 16
        const val FRUIT = 17
        const val INVESTMENT = 18
        const val OTHERS = 19
        const val CREDIT_CARD = 20
    }
}