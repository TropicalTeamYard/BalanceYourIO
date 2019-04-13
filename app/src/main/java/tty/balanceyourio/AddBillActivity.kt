package tty.balanceyourio

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_bill.*
import tty.util.AddBillRecyclerViewAdapter
import tty.util.DataOperator
import java.util.ArrayList
import java.util.HashMap

class AddBillActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {
    var nowMoney=8
    var nowProgress=8
    var shouldChange=true
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        //Log.d("ABA", "stop: "+seekBar?.progress)
        shouldChange=false
        seekBar?.progress=8
        nowProgress=8

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        //Log.d("ABA", "start: "+seekBar?.progress)
    }

    @SuppressLint("SetTextI18n")
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        //Log.d("ABA", "changed: "+seekBar?.progress)
        if(shouldChange){
            if(nowProgress<seekBar?.progress!!){
                nowMoney++
            } else if(nowProgress> seekBar.progress) {
                nowMoney--
            }
            nowProgress= seekBar.progress
            Log.d("ABA", "now:$nowMoney")
            add_show_now_money.text= "￥ ： $nowMoney"
        } else {
            shouldChange=true
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
    }

}
