package com.gx.smart.smartoa.activity.ui.messages

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration

import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.environmental.AirConditioner
import com.gx.smart.smartoa.activity.ui.environmental.AirConditionerViewBinder
import com.gx.smart.smartoa.base.BaseFragment
import kotlinx.android.synthetic.main.air_conditioner_fragment.*
import kotlinx.android.synthetic.main.all_fragment.*

class AllFragment : BaseFragment() {

    companion object {
        fun newInstance() = AllFragment()
        const val ALL_TYPE = 1
    }

    private lateinit var viewModel: AllViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AllViewModel::class.java)
        // TODO: Use the ViewModel
        val divider = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        val drawable = resources.getDrawable(R.drawable.shape_environmental_control_line, null)
        divider.setDrawable(drawable)
        allRecyclerView?.addItemDecoration(divider)

        adapter.register(AllOneViewBinder())
        adapter.register(AllTwoViewBinder())
        allRecyclerView?.adapter = adapter

        val textItem1 = AllOne()
        val textItem2 = AllTwo()
        val textItem3 = AllOne()

        items.add(textItem1)
        items.add(textItem2)
        items.add(textItem3)

        adapter.items = items
        adapter.notifyDataSetChanged()
    }

}
