package com.gx.smart.smartoa.activity.ui.environmental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.base.BaseFragment
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevDto
import kotlinx.android.synthetic.main.air_conditioner_fragment.*

class AirConditionerFragment(private val airConditionerList: List<DevDto>) : BaseFragment() {

    companion object {
        const val AIR_CONDITIONER_TYPE = 2
    }

    private lateinit var viewModel: AirConditionerViewModel
    var fragment: EnvironmentalControlFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.air_conditioner_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AirConditionerViewModel::class.java)
        val airConditionerViewBinder = AirConditionerViewBinder()
        airConditionerViewBinder.fragment = fragment
        adapter.register(airConditionerViewBinder)
        airConditionerRecyclerView?.adapter = adapter

        for (airConditioner in airConditionerList) {
            val textItem1 = AirConditioner(airConditioner)
            items.add(textItem1)
        }
        adapter.items = items
        adapter.notifyDataSetChanged()
    }

    fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }

}
