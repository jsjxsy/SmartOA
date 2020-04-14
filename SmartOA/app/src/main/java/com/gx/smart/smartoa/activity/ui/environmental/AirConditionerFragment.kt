package com.gx.smart.smartoa.activity.ui.environmental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.drakeet.multitype.MultiTypeAdapter
import com.gx.smart.smartoa.R
import com.gx.smart.lib.base.BaseFragment
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevDto
import kotlinx.android.synthetic.main.air_conditioner_fragment.*

class AirConditionerFragment(private val airConditionerList: List<DevDto>) : BaseFragment() {
    private val adapter = MultiTypeAdapter()
    private val items = ArrayList<Any>()
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

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AirConditionerViewModel::class.java)
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
