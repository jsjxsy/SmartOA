package com.gx.smart.smartoa.activity.ui.environmental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.drakeet.multitype.MultiTypeAdapter
import com.gx.smart.smartoa.R
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevDto
import kotlinx.android.synthetic.main.curtain_fragment.*

class CurtainFragment(private val curtainList: List<DevDto>) : Fragment() {

    companion object {
        const val CURTAIN_TYPE = 1
    }

    private lateinit var viewModel: CurtainViewModel
    private val adapter = MultiTypeAdapter()
    private val items = ArrayList<Any>()
    var fragment: EnvironmentalControlFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.curtain_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CurtainViewModel::class.java)
        val divider = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        val drawable = resources.getDrawable(R.drawable.shape_environmental_control_line, null)
        divider.setDrawable(drawable)
        curtainRecyclerView!!.addItemDecoration(divider)
        val curtainItemViewBinder = CurtainItemViewBinder()
        curtainItemViewBinder.fragment = fragment
        adapter.register(curtainItemViewBinder)
        curtainRecyclerView?.adapter = adapter

        for (curtain in curtainList) {
            val textItem1 = CurtainItem(curtain)
            items.add(textItem1)
        }
        adapter.items = items
        adapter.notifyDataSetChanged()
    }

}
