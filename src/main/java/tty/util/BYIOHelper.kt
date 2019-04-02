package tty.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BYIOHelper(context : Context): SQLiteOpenHelper(context, DB_NAME,null,DB_VERSION) {
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_BILL)
    }

    companion object {
        const val DB_VERSION=1
        const val DB_NAME="byio.db"
        const val CREATE_TABLE_BILL= "create table bill(" +
                        "_id integer primary key autoincrement, " +
                        "tag varchar(12) not null, " +
                        "time varchar(20) not null, " +
                        "iotype int(1) not null, " +
                        "goodstype varchar(12) not null, " +
                        "amount double not null, " +
                        "channel varchar(20) not null, " +
                        "remark tinytext" +
                        ")"
    }
}

