package tty.balanceyourio

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
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_bill.*
import tty.util.AddBillRecyclerViewAdapter
import tty.util.DataOperator
import java.util.ArrayList
import java.util.HashMap

class AddBillActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener {

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
        recyclerView.layoutManager = GridLayoutManager(this, 2)
    }

    companion object {
        const val TAG = "AddBillActivity"
    }
}
