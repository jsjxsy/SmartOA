package com.gx.smart.smartoa.activity.ui.environmental

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.drakeet.multitype.MultiTypeAdapter

import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.curtain_fragment.*

class CurtainFragment : Fragment() {

    companion object {
        fun newInstance() = CurtainFragment()
        const val  CURTAIN_TYPE = 1
    }

    private lateinit var viewModel: CurtainViewModel
    private val adapter = MultiTypeAdapter()
    private val items = ArrayList<Any>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.curtain_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CurtainViewModel::class.java)
        // TODO: Use the ViewModel

        val divider = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        val drawable = resources.getDrawable(R.drawable.shape_environmental_control_line, null)
        divider.setDrawable(drawable)
        curtainRecyclerView!!.addItemDecoration(divider)

        adapter.register(CurtainItemViewBinder())
        curtainRecyclerView?.adapter = adapter

        val textItem1 = CurtainItem("研发窗帘1")
        val textItem2 = CurtainItem("研发窗帘2")
        val textItem3 = CurtainItem("研发窗帘3")

        items.add(textItem1)
        items.add(textItem2)
        items.add(textItem3)

        adapter.items = items
        adapter.notifyDataSetChanged()
    }

}
