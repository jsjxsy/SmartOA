package com.gx.smart.smartoa.activity.ui.environmental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.drakeet.multitype.MultiTypeAdapter
import com.gx.smart.smartoa.R
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevDto
import kotlinx.android.synthetic.main.light_fragment.*

class LightFragment(private val lightList: List<DevDto>) : Fragment() {
    companion object {
        const val LIGHT_TYPE = 0
    }

    private lateinit var viewModel: LightViewModel
    private val adapter = MultiTypeAdapter()
    private val items = ArrayList<Any>()
    var fragment: EnvironmentalControlFragment? = null
    var mLightList: List<DevDto>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.light_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LightViewModel::class.java)
        val itemOneViewBinder = LightItemOneViewBinder()
        itemOneViewBinder.fragment = fragment
        adapter.register(itemOneViewBinder)
        val itemTwoViewBinder = LightItemTwoViewBinder()
        itemTwoViewBinder.fragment = fragment
        adapter.register(LightItemTwoViewBinder())
        lightRecyclerView.adapter = adapter
        mLightList = lightList
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
