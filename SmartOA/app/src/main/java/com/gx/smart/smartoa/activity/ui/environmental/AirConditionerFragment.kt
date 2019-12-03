package com.gx.smart.smartoa.activity.ui.environmental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.base.BaseFragment
import kotlinx.android.synthetic.main.air_conditioner_fragment.*

class AirConditionerFragment : BaseFragment() {

    companion object {
        fun newInstance() = AirConditionerFragment()
        const val AIR_CONDITIONER_TYPE = 2
    }

    private lateinit var viewModel: AirConditionerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.air_conditioner_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AirConditionerViewModel::class.java)

        adapter.register(AirConditionerViewBinder())
        airConditionerRecyclerView?.adapter = adapter

        val textItem1 = AirConditioner("研发窗帘1")
        val textItem2 = AirConditioner("研发窗帘2")
        val textItem3 = AirConditioner("研发窗帘3")

        items.add(textItem1)
        items.add(textItem2)
        items.add(textItem3)

        adapter.items = items
        adapter.notifyDataSetChanged()
    }

}
