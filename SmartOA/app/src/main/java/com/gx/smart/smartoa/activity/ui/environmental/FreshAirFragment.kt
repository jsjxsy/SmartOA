package com.gx.smart.smartoa.activity.ui.environmental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.gx.smart.smartoa.R
import com.gx.smart.lib.base.BaseFragment
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevDto
import kotlinx.android.synthetic.main.fragment_fresh_air.*

class FreshAirFragment(private val freshAirList: List<DevDto>) : BaseFragment() {

    companion object {
        const val FRESH_AIR_TYPE = 3
    }

    private lateinit var viewModel: FreshAirViewModel
    var fragment: EnvironmentalControlFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fresh_air, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FreshAirViewModel::class.java)
        // TODO: Use the ViewModel
        val divider = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        val drawable = resources.getDrawable(R.drawable.shape_environmental_control_line, null)
        divider.setDrawable(drawable)
        freshAirRecyclerView!!.addItemDecoration(divider)
        val freshAirViewBinder = FreshAirViewBinder()
        freshAirViewBinder.fragment = fragment
        adapter.register(freshAirViewBinder)
        freshAirRecyclerView?.adapter = adapter

        for (freshAir in freshAirList) {
            val textItem1 = FreshAir(freshAir)
            items.add(textItem1)
        }

        adapter.items = items
        adapter.notifyDataSetChanged()
    }

}
