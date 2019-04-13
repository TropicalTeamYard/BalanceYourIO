package tty.balanceyourio

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_bill.*
import tty.util.AddBillRecyclerViewAdapter
import tty.util.DataOperator
import java.text.DecimalFormat
import java.util.ArrayList
import java.util.HashMap

class AddBillActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener,
    TextWatcher {
    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(shouldInputMoneyChange){
            val decimalFormat = DecimalFormat("0.00")
            try {
                if(s?.length!! >0&&s.toString().toDouble()>=0){
                    nowMoney=s.toString().toDouble()
                    nowMoney=decimalFormat.format(nowMoney).toDouble()
                    add_show_now_money.text= "￥ $nowMoney"
                } else if(s.isEmpty()) {
                    nowMoney=0.0
                    add_show_now_money.text= "￥ $nowMoney"
                }
                if(nowMoney>999999){
                    nowMoney=999999.0
                    add_show_now_money.text= "￥ $nowMoney"
                    add_input_money.setText("999999")
                    add_input_money.setSelection(add_input_money.text.length)
                }
            } catch (e : NumberFormatException) {
                //e.printStackTrace()
                if(nowMoney>0){
                    add_show_now_money.text= "￥ $nowMoney"
                } else {
                    add_input_money.setText("")
                }

            }
        } else {
            shouldInputMoneyChange=true
        }


    }

    var nowMoney=8.0
    var nowProgress=8.0
    private var shouldMoneyChange=true
    private var shouldInputMoneyChange=true
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        //Log.d("ABA", "stop: "+seekBar?.progress)
        shouldMoneyChange=false
        shouldInputMoneyChange=false
        seekBar?.progress=8
        nowProgress= 8.0
        add_input_money.setText("")

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        //Log.d("ABA", "start: "+seekBar?.progress)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        //Log.d("ABA", "changed: "+seekBar?.progress)
        shouldInputMoneyChange=false
        if(shouldMoneyChange){
            if(nowProgress<seekBar?.progress!!){
                nowMoney++
            } else if(nowProgress> seekBar.progress) {
                nowMoney--
            }
            nowProgress= seekBar.progress.toDouble()
            if(nowMoney<=0){
                nowMoney= 0.0
            } else if(nowMoney>999999){
                nowMoney=999999.0
            }
            Log.d("ABA", "now:$nowMoney")
            add_show_now_money.text= "￥ $nowMoney"
        } else {
            shouldMoneyChange=true
        }

    }

    private val TAG = "AddBillActivity"
    private lateinit var recyclerView: RecyclerView
    private lateinit var data: ArrayList<HashMap<String, Any>>
    private lateinit var adapter: AddBillRecyclerViewAdapter
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId){
            R.id.add_bill_radio_income -> {
                Toast.makeText(this, "Outcome chosen", Toast.LENGTH_SHORT).show()
                data=DataOperator.getIncomeClassList(this)
                adapter.setData(data)
                adapter.notifyDataSetChanged()

            }
            R.id.add_bill_radio_outcome -> {
                Toast.makeText(this, "Income chosen", Toast.LENGTH_SHORT).show()
                data=DataOperator.getOutcomeClassList(this)
                adapter.setData(data)
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
        data=DataOperator.getOutcomeClassList(this)
        recyclerView = findViewById(R.id.add_bill_rec_view)
        adapter = AddBillRecyclerViewAdapter(data)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        add_sb_money.setOnSeekBarChangeListener(this)
        add_show_now_money.text="￥ $nowMoney"
        add_input_money.addTextChangedListener(this)
    }

    companion object {
        const val TAG = "AddBillActivity"
    }
}
