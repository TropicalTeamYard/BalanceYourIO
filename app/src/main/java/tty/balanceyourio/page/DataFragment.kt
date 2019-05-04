package tty.balanceyourio.page


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_data.*
import tty.balanceyourio.R
import tty.balanceyourio.adapter.ShowBillListAdapter

class DataFragment : Fragment() {
    lateinit var adapter:ShowBillListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        f_data_fab_add.setOnClickListener {
            startActivity(Intent(this.context, AddBillActivity::class.java))
        }
        adapter=ShowBillListAdapter(this.context!!)
        elv_show_bill_data.setAdapter(adapter)
        elv_show_bill_data.setGroupIndicator(null)
        try {
            elv_show_bill_data.expandGroup(0)
        } catch (e: IndexOutOfBoundsException){

        }

    }

    override fun onResume() {
        super.onResume()

    }

}
