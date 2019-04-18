package tty.balanceyourio.converter

import tty.balanceyourio.R

class PxlIconConverter:IconConverter{
    override fun getIconRes(index: Int): Int {
        return when(index){
            CALENDAR -> R.drawable.ic_pxl_calendar
            CAMERA -> R.drawable.ic_pxl_camera
            CAR -> R.drawable.ic_pxl_car
            CARD -> R.drawable.ic_pxl_card
            CATEGORY -> R.drawable.ic_pxl_category
            CLASSIFY -> R.drawable.ic_pxl_classify
            COSMETOLOGY -> R.drawable.ic_pxl_cosmetology
            EXCHANGE -> R.drawable.ic_pxl_exchange
            FOSTER -> R.drawable.ic_pxl_foster
            GAME -> R.drawable.ic_pxl_game
            INVITATION -> R.drawable.ic_pxl_invitation
            LOCATION -> R.drawable.ic_pxl_location
            TAG -> R.drawable.ic_pxl_tag
            WASH -> R.drawable.ic_pxl_wash
            else -> -1
        }
    }

    override val count: Int
        get() = 14


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
    }
}