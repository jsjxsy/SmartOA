package com.gx.smart.smartoa.activity.ui.environmental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import androidx.lifecycle.ViewModelProvider
import com.drakeet.multitype.MultiTypeAdapter
import com.gx.smart.smartoa.R
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevDto
import kotlinx.android.synthetic.main.light_fragment.*

class LightFragment(private val lightList: List<DevDto>) : BaseFragment() {
    companion object {
        const val LIGHT_TYPE = 0
    }

    private lateinit var viewModel: LightViewModel
    private val adapter = MultiTypeAdapter()
    val items = ArrayList<Any>()
    var fragment: EnvironmentalControlFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.light_fragment, container, false)
    }

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LightViewModel::class.java)
        val itemOneViewBinder = LightItemOneViewBinder()
        itemOneViewBinder.fragment = fragment
        adapter.register(itemOneViewBinder)
        val itemTwoViewBinder = LightItemTwoViewBinder()
        itemTwoViewBinder.fragment = fragment
        adapter.register(LightItemTwoViewBinder())
        lightRecyclerView.adapter = adapter


        for (light in lightList) {
            when (light.category) {
                "single_zf_switch", "single_fire_switch", "mechanical_switch", "l_color_dimmer_controller" -> {
                    val textItem1 = LightItemOne(light)
                    items.add(textItem1)
                }
                else -> {
                    val textItem2 = LightItemTwo(light)
                    items.add(textItem2)
                }
            }
        }

        adapter.items = items
        adapter.notifyDataSetChanged()
    }
}
