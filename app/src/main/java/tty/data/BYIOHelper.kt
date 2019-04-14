package tty.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import tty.model.BillRecord
import tty.model.IOType
import java.text.SimpleDateFormat
import java.util.*

class BYIOHelper(context : Context): SQLiteOpenHelper(context, DB_NAME,null, DB_VERSION) {
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_BILL)
        db?.execSQL(CREATE_TABLE_SETTINGS)
    }


    //region 账单读写操作
    fun setBill(record: BillRecord) {
        val contentValues:ContentValues = ContentValues()
        if (record.tag!=null)
            contentValues.put("tag",record.tag!!)
        if (record.time!= null)
            contentValues.put("time",SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(record.time!!))
        if (record.ioType!= IOType.Unset)
            contentValues.put("iotype",when (record.ioType){
                IOType.Income-> 1
                IOType.OutCome -> 2
                else -> 0
            })
        if (record.goodsType!= null)
            contentValues.put("goodstype",record.goodsType!!)
        if (record.amount!=null){
            contentValues.put("amount",record.amount!!)
        }
        if (record.channel!=null){
            contentValues.put("channel",record.channel!!)
        }
        if (record.remark!= null){
            contentValues.put("remark",record.remark!!)
        }

        if (record.id== -1){
            writableDatabase.insert(NAME_BILL,null,contentValues)
        } else  {
            writableDatabase.update(NAME_BILL,contentValues,"id = ?", arrayOf(record.id.toString()))
        }

    }
    fun printBill(){
        val cursor = readableDatabase.rawQuery("select * from $NAME_BILL", arrayOf<String>())
        if (cursor.moveToFirst() && cursor.count > 0){
            do {
                Log.d(TAG,"id: ${cursor.getInt(cursor.getColumnIndex("_id"))} " +
                        "time: ${cursor.getString(cursor.getColumnIndex("time"))} " +
                        "iotype: ${cursor.getInt(cursor.getColumnIndex("iotype"))} " +
                        "goodstype: ${cursor.getString(cursor.getColumnIndex("goodstype"))} " +
                        "amount: ${cursor.getString(cursor.getColumnIndex("amount"))}")
            } while (cursor.moveToNext())
        }

        cursor.close()
    }



    //endregion


    //region 设置的读写操作[封装的API]
    fun getSettingsValue(key:String,default:String):String?{
        val cursor:Cursor =  readableDatabase.rawQuery("select value from settings where name = ? limit 1", arrayOf(key))
        var result:String = default
        if (cursor.moveToFirst() && (cursor.count > 0)){
            result = cursor.getString(cursor.getColumnIndex("name"))
        }
        cursor.close()

        return result
    }

    fun setSettingsValue(key:String,value:String){
        val cursor:Cursor = readableDatabase.rawQuery("select value from settings where name= ? limit 1", arrayOf(key))
        if (cursor.moveToFirst() && (cursor.count > 0)){
            //说明已经有储存的数据
            val values = ContentValues()
            values.put("value",value)
            writableDatabase.update("settings",values,"name = ? limit 1", arrayOf(key))
        }
        else
        {
            val values = ContentValues()
            values.put("name",key)
            values.put("value",value)
            val _newId:Long = writableDatabase.insert("settings",null,values)
            if (_newId == -1L){
                Log.d(TAG,"尝试在settings表中新建一条记录，但出现错误")
            }
            else{
                Log.d(TAG,"在settings表中新建了一条记录，id为$_newId")
            }
        }

        cursor.close()
    }
    //endregion

    companion object {
        const val TAG="DBH"
        const val DB_VERSION=1
        const val DB_NAME="byio.db"
        const val NAME_BILL = "bill"
        private const val NAME_SETTINGS = "settings"

        const val CREATE_TABLE_BILL= "create table $NAME_BILL(" +
                        "_id integer primary key autoincrement, " +
                        "tag varchar(12) not null, " +
                        "time varchar(20) not null, " +
                        "iotype int(1) not null, " +
                        "goodstype varchar(12) not null, " +
                        "amount double not null, " +
                        "channel varchar(20) not null, " +
                        "remark tinytext" +
                        ")"
        //region 数据库更改:VERSION=2
        const val CREATE_TABLE_SETTINGS = "create table $NAME_SETTINGS(_id integer primary key autoincrement,name varchar(20) not null,value text not null)"
        //endregion
    }
}

