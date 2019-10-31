package com.gx.smart.smartoa.activity.ui.environmental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.gx.smart.smartoa.R

class LightFragment : Fragment() {

    companion object {
        fun newInstance() = LightFragment()
        const val LIGHT_TYPE = 0
    }

    private var lightRecyclerView: RecyclerView? = null
    private lateinit var viewModel: LightViewModel
    private val adapter = MultiTypeAdapter()
    private val items = ArrayList<Any>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.light_fragment, container, false)
        lightRecyclerView = rootView.findViewById<RecyclerView>(R.id.lightRecyclerView)
        //添加自定义分割线
        val divider = DividerItemDecoration(activity,DividerItemDecoration.VERTICAL)
        val drawable = resources.getDrawable(R.drawable.shape_environmental_control_line, null)
        divider.setDrawable(drawable)
        lightRecyclerView!!.addItemDecoration(divider)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LightViewModel::class.java)
        // TODO: Use the ViewModel

        adapter.register(LightItemOneViewBinder())
        adapter.register(LightItemTwoViewBinder())
        lightRecyclerView?.adapter = adapter

        val textItem1 = LightItemOne("研发大灯1")
        val textItem2 = LightItemOne("研发大灯2")
        val textItem3 = LightItemOne("研发大灯3")

        items.add(textItem1)
        items.add(textItem2)
        items.add(textItem3)

        val textItem21 = LightItemTwo("研发调光面板")
        val textItem22 = LightItemTwo("研发调光面板")
        val textItem23 = LightItemTwo("研发调光面板")

        items.add(textItem21)
        items.add(textItem22)
        items.add(textItem23)

        adapter.items = items
        adapter.notifyDataSetChanged()
    }

}
