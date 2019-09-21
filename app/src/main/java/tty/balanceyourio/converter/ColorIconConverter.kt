package tty.balanceyourio.converter

import tty.balanceyourio.R


class ColorIconConverter : RConverter {
    override val count: Int
        get() = 24

    override fun getResID(index: Int): Int {
        return when (index) {
            ADD_USER -> R.drawable.ic_color_adduser
            CALENDAR -> R.drawable.ic_color_calendar
            CONCAT -> R.drawable.ic_color_concat
            EXPLORE -> R.drawable.ic_color_explore
            FAVOR -> R.drawable.ic_color_favor
            FAVUSER -> R.drawable.ic_color_favuser
            HONOR -> R.drawable.ic_color_honor
            INFO -> R.drawable.ic_color_info
            LOVE -> R.drawable.ic_color_love
            MAP -> R.drawable.ic_color_map
            MEDAL -> R.drawable.ic_color_medal
            MEDICINE -> R.drawable.ic_color_medicine
            PHONE -> R.drawable.ic_color_phone
            PIC -> R.drawable.ic_color_pic
            PLAY -> R.drawable.ic_color_play
            READ -> R.drawable.ic_color_read
            SAFETY -> R.drawable.ic_color_safety
            SCHOOL -> R.drawable.ic_color_school
            SHOPPING -> R.drawable.ic_color_shopping
            STAMP -> R.drawable.ic_color_stamp
            TAG -> R.drawable.ic_color_tag
            USER -> R.drawable.ic_color_user
            USERCONFIG -> R.drawable.ic_color_userconfig
            VISITOR -> R.drawable.ic_color_visitor
            else -> -1
        }
    }

    companion object {
        const val ADD_USER = 0
        const val CALENDAR = 1
        const val CONCAT = 2
        const val EXPLORE = 3
        const val FAVOR = 4
        const val FAVUSER = 5
        const val HONOR = 6
        const val INFO = 7
        const val LOVE = 8
        const val MAP = 9
        const val MEDAL = 10
        const val MEDICINE = 11
        const val PHONE = 12
        const val PIC = 13
        const val PLAY = 14
        const val READ = 15
        const val SAFETY = 16
        const val SCHOOL = 17
        const val SHOPPING = 18
        const val STAMP = 19
        const val TAG = 20
        const val USER = 21
        const val USERCONFIG = 22
        const val VISITOR = 23
    }
}