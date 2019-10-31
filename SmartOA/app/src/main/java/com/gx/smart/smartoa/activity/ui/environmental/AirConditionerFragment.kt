package com.gx.smart.smartoa.activity.ui.environmental

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration

import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.base.BaseFragment
import kotlinx.android.synthetic.main.air_conditioner_fragment.*
import kotlinx.android.synthetic.main.curtain_fragment.*

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
        // TODO: Use the ViewModel
        val divider = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        val drawable = resources.getDrawable(R.drawable.shape_environmental_control_line, null)
        divider.setDrawable(drawable)
        airConditionerRecyclerView!!.addItemDecoration(divider)

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
