package com.gx.smart.smartoa.activity.ui.environmental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.drakeet.multitype.MultiTypeAdapter
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.light_fragment.*

class LightFragment : Fragment() {

    companion object {
        fun newInstance() = LightFragment()
        const val LIGHT_TYPE = 0
    }

    private lateinit var viewModel: LightViewModel
    private val adapter = MultiTypeAdapter()
    private val items = ArrayList<Any>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.light_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LightViewModel::class.java)
        adapter.register(LightItemOneViewBinder())
        adapter.register(LightItemTwoViewBinder())
        lightRecyclerView.adapter = adapter

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
