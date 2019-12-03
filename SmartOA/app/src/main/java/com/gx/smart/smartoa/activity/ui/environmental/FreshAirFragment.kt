package com.gx.smart.smartoa.activity.ui.environmental

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration

import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_fresh_air.*

class FreshAirFragment : BaseFragment() {

    companion object {
        fun newInstance() = FreshAirFragment()
        const val FRESH_AIR_TYPE = 3
    }

    private lateinit var viewModel: FreshAirViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fresh_air, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FreshAirViewModel::class.java)
        // TODO: Use the ViewModel
        val divider = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        val drawable = resources.getDrawable(R.drawable.shape_environmental_control_line, null)
        divider.setDrawable(drawable)
        freshAirRecyclerView!!.addItemDecoration(divider)

        adapter.register(FreshAirViewBinder())
        freshAirRecyclerView?.adapter = adapter

        val textItem1 = FreshAir("新风1")
        val textItem2 = FreshAir("新风2")
        val textItem3 = FreshAir("新风3")

        items.add(textItem1)
        items.add(textItem2)
        items.add(textItem3)

        adapter.items = items
        adapter.notifyDataSetChanged()
    }

}
