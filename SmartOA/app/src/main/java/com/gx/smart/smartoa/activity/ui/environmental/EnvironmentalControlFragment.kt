package com.gx.smart.smartoa.activity.ui.environmental

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drakeet.multitype.MultiTypeAdapter

import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.evnironmental_control_fragment.*

class EnvironmentalControlFragment : Fragment() {

    companion object {
        fun newInstance() = EnvironmentalControlFragment()
    }
    private lateinit var mPagerAdapter: PageAdapter

    private lateinit var viewModel: EnvironmentalControlViewModel
    private val adapter = MultiTypeAdapter()
    private val items = ArrayList<Any>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.evnironmental_control_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EnvironmentalControlViewModel::class.java)
        // TODO: Use the ViewModel
        initHead()
        initData()
    }

    private fun initHead() {
        adapter.register(LightHeadItemViewBinder())
        id_environmental_control_head.adapter = adapter
        val headTextItem1 = LightHeadItem("研发大灯1", R.drawable.ic_light_all_open)
        val headTextItem2 = LightHeadItem("研发大灯2", R.drawable.ic_light_all_close)
        val headTextItem3 = LightHeadItem("研发大灯3", R.drawable.ic_sleep)
        val headTextItem4 = LightHeadItem("研发大灯3", R.drawable.ic_work_off)
        val headTextItem5 = LightHeadItem("研发大灯3", R.drawable.ic_tea)
        items.add(headTextItem1)
        items.add(headTextItem2)
        items.add(headTextItem3)
        items.add(headTextItem4)
        items.add(headTextItem5)
        adapter.items = items
        adapter.notifyDataSetChanged()
    }

    private fun initData() {

        val titles = resources.getStringArray(R.array.environmental_control_items)
        mPagerAdapter = PageAdapter(fragmentManager!!)
        for (i in 0 until titles.size){
            mPagerAdapter.addPage(PageAdapter.PageFragmentContent(titles[i], i))
            mPagerAdapter.notifyDataSetChanged()
        }
        viewPager.adapter = mPagerAdapter
        viewPager.offscreenPageLimit = 3
        id_environmental_control_tab.setupWithViewPager(viewPager)
    }

}
