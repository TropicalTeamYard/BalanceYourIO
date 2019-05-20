package tty.balanceyourio.page

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_bill.*
import tty.balanceyourio.R
import tty.balanceyourio.adapter.AddBillIconAdapter
import tty.balanceyourio.converter.PxlIconConverter
import tty.balanceyourio.data.BYIOHelper
import tty.balanceyourio.model.BillRecord
import tty.balanceyourio.model.IOType
import tty.balanceyourio.provider.IOTypeProvider
import tty.balanceyourio.util.DateConverter
import java.text.DecimalFormat
import java.util.*

class AddBillActivity : AppCompatActivity(),
    RadioGroup.OnCheckedChangeListener,
//    SeekBar.OnSeekBarChangeListener,
    TextWatcher, View.OnClickListener,
    AddBillIconAdapter.OnItemClickListener,
    ChooseDateFragment.SendDate {

    override fun getDate(date: Date) {
        isDateChoose=true
        this.date=date
        add_bill_show_date.text=DateConverter.getSimpleString(date)
        Log.d(TAG, DateConverter.getString(this.date))
    }

    override fun onItemClick(v: View?, position: Int) {
        Log.d(TAG, "pos: $position")
        for(p in data){
            p["chosen"]=false
        }
        data[position]["chosen"]=true
        adapter.notifyDataSetChanged()
    }

    private var isDateChoose=false
    private var date:Date= Date()

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.add_bill_bt_save -> {
                if(!modeUpdate){
                    Log.d(TAG, "SAVE")
                    try {
                        var flag=false
                        var type=""
                        for(p in data){
                            if(p["chosen"] as Boolean){
                                flag=true
                                type= p["class"] as String
                                break
                            }
                        }
                        if(flag){
                            add_bill_bt_save.isClickable=false
                            Log.i(TAG,
                                "mode: ${
                                when(add_bill_radio_group.checkedRadioButtonId){
                                    R.id.add_bill_radio_income -> "income"
                                    R.id.add_bill_radio_outcome -> "outcome"
                                    R.id.add_bill_radio_others -> "others"
                                    else -> "#UNSET"
                                }}, " +
                                        "type: $type, amount: $nowMoney")
                            //region 与数据库的交互
                            billRecord = BillRecord()
                            billRecord.id = -1
                            billRecord.tag = "#UNSET"
                            billRecord.time = date
                            Log.d(TAG,"time: ${billRecord.time}")
                            billRecord.amount = nowMoney
                            billRecord.goodsType = type
                            billRecord.ioType = when(add_bill_radio_group.checkedRadioButtonId){
                                R.id.add_bill_radio_income ->IOType.Income
                                R.id.add_bill_radio_outcome ->IOType.Outcome
                                else -> IOType.Unset
                            }
                            billRecord.channel = "#UNSET"
                            billRecord.remark = if(add_bill_ed_remark.text.toString().isNotEmpty()){add_bill_ed_remark.text.toString()} else { resources.getString(R.string.blank) }

                            val helper = BYIOHelper(this)
                            helper.setBill(billRecord)

                            //Log.d(TAG,"添加了一条记录")

                            //endregion
                            //helper.printBill()

                            Toast.makeText(this,
                                "mode: ${
                                when(add_bill_radio_group.checkedRadioButtonId){
                                    R.id.add_bill_radio_income -> "income"
                                    R.id.add_bill_radio_outcome -> "outcome"
                                    R.id.add_bill_radio_others -> "others"
                                    else -> "#UNSET"
                                }}, " +
                                        "type: $type, amount: $nowMoney", Toast.LENGTH_SHORT).show()
                            finish()
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            Toast.makeText(this, "类型不能为空！", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: TypeCastException){ }
                } else {
                    var flag=false
                    var type=""
                    for(p in data){
                        if(p["chosen"] as Boolean){
                            flag=true
                            type= p["class"] as String
                            break
                        }
                    }
                    if(flag){
                        add_bill_bt_save.isClickable=false
                        Log.i(TAG,
                            "mode: ${
                                    when(add_bill_radio_group.checkedRadioButtonId){
                                        R.id.add_bill_radio_income -> "income"
                                        R.id.add_bill_radio_outcome -> "outcome"
                                        R.id.add_bill_radio_others -> "others"
                                        else -> "#UNSET"
                                    }}, " +
                                    "type: $type, amount: $nowMoney")
                        //region 与数据库的交互
                        val record = BillRecord()
                        record.id = -1
                        record.tag = "#UNSET"
                        record.time = date
                        Log.d(TAG,"time: ${record.time}")
                        record.amount = nowMoney
                        record.goodsType = type
                        record.ioType = when(add_bill_radio_group.checkedRadioButtonId){
                            R.id.add_bill_radio_income ->IOType.Income
                            R.id.add_bill_radio_outcome ->IOType.Outcome
                            else -> IOType.Unset
                        }
                        record.channel = "#UNSET"
                        record.remark = if(add_bill_ed_remark.text.toString().isNotEmpty()){add_bill_ed_remark.text.toString()} else { "（无）" }

                        val helper = BYIOHelper(this)
                        helper.setBill(record)

                        Log.d(TAG,"添加了一条记录")

                        //endregion
                        //helper.printBill()

                        Toast.makeText(this,
                            "mode: ${
                                    when(add_bill_radio_group.checkedRadioButtonId){
                                        R.id.add_bill_radio_income -> "income"
                                        R.id.add_bill_radio_outcome -> "outcome"
                                        R.id.add_bill_radio_others -> "others"
                                        else -> "#UNSET"
                                    }}, " +
                                    "type: $type, amount: $nowMoney", Toast.LENGTH_SHORT).show()
                        finish()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "类型不能为空！", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(shouldInputMoneyChange){
            try {
                if(s?.length!! >0&&s.toString().toDouble()>=0){
                    nowMoney=s.toString().toDouble()
                    nowMoney=decimalFormat.format(nowMoney).toDouble()
                    add_show_now_money.text= "${resources.getString(R.string.currency)} $nowMoney"
                } else if(s.isEmpty()) {
                    nowMoney=0.0
                    add_show_now_money.text= "${resources.getString(R.string.currency)} $nowMoney"
                }
                if(nowMoney>999999){
                    nowMoney=999999.0
                    add_show_now_money.text= "${resources.getString(R.string.currency)} $nowMoney"
                    add_input_money.setText("999999")
                    add_input_money.setSelection(add_input_money.text.length)
                }
            } catch (e : NumberFormatException) {
                if(nowMoney>0){
                    add_show_now_money.text= "${resources.getString(R.string.currency)} $nowMoney"
                } else {
                    add_input_money.setText("")
                }

            }
        } else {
            shouldInputMoneyChange=true
        }


    }

    private var nowMoney = 0.0
//    private var nowProgress= max_val
//    private var shouldMoneyChange=true
    private var shouldInputMoneyChange=true
    private val decimalFormat = DecimalFormat("0.00")

//    override fun onStopTrackingTouch(seekBar: SeekBar?) {
//        shouldMoneyChange=false
//        shouldInputMoneyChange=false
//        seekBar?.progress = max_val.toInt() * 2
//        nowProgress = max_val
//        add_input_money.setText("")
//
//    }
//
//    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
//
//    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//        shouldInputMoneyChange=false
//        if(shouldMoneyChange){
//            if(nowProgress<seekBar?.progress!!* delta){
//                nowMoney+= delta
//            } else if(nowProgress> seekBar.progress* delta) {
//                nowMoney-= delta
//            }
//            nowProgress= seekBar.progress.toDouble()* delta
//            nowMoney=decimalFormat.format(nowMoney).toDouble()
//            if(nowMoney<=0){
//                nowMoney= 0.0
//            } else if(nowMoney>999999){
//                nowMoney=999999.0
//            }
//            //Log.d("ABA", "now: $nowMoney")
//            add_show_now_money.text= "${resources.getString(R.string.currency)} $nowMoney"
//        } else {
//            shouldMoneyChange=true
//        }
//
//    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var data: ArrayList<HashMap<String, Any>>
    private lateinit var adapter:AddBillIconAdapter
    private var modeUpdate=false
    private var billRecordId:Int=0
    private var billRecord: BillRecord?=null

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId){
            R.id.add_bill_radio_income -> {
                data = IOTypeProvider(this).incomeTypeList
                adapter.source = data
                adapter.notifyDataSetChanged()
            }
            R.id.add_bill_radio_outcome -> {
                data = IOTypeProvider(this).outcomeTypeList
                adapter.source = data
                adapter.notifyDataSetChanged()
            }
            R.id.add_bill_radio_others -> {
                data = IOTypeProvider(this).othersTypeList
                adapter.source = data
                adapter.notifyDataSetChanged()
            }
            else -> {
                Log.i(TAG, checkedId.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bill)
        add_bill_radio_group.setOnCheckedChangeListener(this)


        //DONE("与数据库交互，获取BYIOCategory")
        data = IOTypeProvider(this).outcomeTypeList

        recyclerView = add_bill_rec_view
        adapter = AddBillIconAdapter(data, PxlIconConverter())
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
        val layoutManager=GridLayoutManager(this, 6)
        layoutManager.orientation=GridLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

//        add_sb_money.setOnSeekBarChangeListener(this)
        add_show_now_money.text="${resources.getString(R.string.currency)} $nowMoney"
        add_input_money.addTextChangedListener(this)
        add_bill_bt_save.setOnClickListener(this)
        adapter.setOnItemClickListener(this)
        add_bill_choose_date.setOnClickListener {
            val dialog=ChooseDateFragment()
            dialog.show(this.supportFragmentManager,"CDF")
        }

        //如果是从更新入口进入则执行恢复数据
        if(intent.extras!=null&&intent.hasExtra("update")){
            modeUpdate=true
            billRecordId = intent.getIntExtra("id", -1)
            billRecord=BYIOHelper(this).getBill(billRecordId)
            if(billRecord!=null){
                add_bill_ed_remark.setText(billRecord!!.remark)
                date= billRecord!!.time!!
                add_bill_show_date.text=DateConverter.getSimpleString(date)
                add_bill_radio_group.check(when(billRecord!!.ioType){
                    IOType.Outcome -> R.id.add_bill_radio_outcome
                    IOType.Income -> R.id.add_bill_radio_income
                    IOType.Unset -> R.id.add_bill_radio_others
                })

                //TODO @CHT 完成通过可以转化到选中POSITION

//                try {
//                    val goodsTypeString=intent.getStringExtra("goodstype")
//                    val goodTypeKey=goodsTypeString.split(".")[1].toInt()
//                    Log.d(TAG, "TYPE: $goodsTypeString $goodTypeKey")
//                    Log.d(TAG, "pos: $goodTypeKey")
//                    for(p in data){
//                        p["chosen"]=false
//                    }
//                    data[goodTypeKey]["chosen"]=true
//                    adapter.notifyDataSetChanged()
//                } catch (e: Exception){ Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show() }
                add_input_money.setText("${billRecord!!.amount}")
            }

        }
        Log.d(TAG, "isUpdate $modeUpdate id: $billRecordId")

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_bill, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.menu_add_bill_settings -> {
                Toast.makeText(this, "编辑分类", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }

    companion object {
        const val TAG = "ABA"
//        const val delta = 0.5
//        const val max_val:Double = 4.0
    }
}
