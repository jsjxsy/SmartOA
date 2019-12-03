package com.gx.smart.smartoa.activity.ui.environmental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.drakeet.multitype.MultiTypeAdapter
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.evnironmental_control_fragment.*
import kotlinx.android.synthetic.main.layout_common_title.*

class EnvironmentalControlFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }

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
        initTitle()
        initHead()
        initData()
    }

    private fun initTitle() {
        left_nav_image_view.visibility = View.VISIBLE
        left_nav_image_view.setOnClickListener(this)
        center_title.visibility = View.VISIBLE
        center_title.text = getString(R.string.environmental_control)
    }

    private fun initHead() {
        refreshLayout.setOnRefreshListener { refreshLayout.finishRefresh(2000) }
        refreshLayout.isEnableLoadmore = false

        adapter.register(LightHeadItemViewBinder())
        id_environmental_control_head.adapter = adapter
        val headTextItem1 = LightHeadItem("灯光全关", R.drawable.ic_light_all_open)
        val headTextItem2 = LightHeadItem("灯光全开", R.drawable.ic_light_all_close)
        val headTextItem3 = LightHeadItem("午休模式", R.drawable.ic_sleep)
        val headTextItem4 = LightHeadItem("下班模式", R.drawable.ic_work_off)
        val headTextItem5 = LightHeadItem("下午茶", R.drawable.ic_tea)
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
        for (i in 0 until titles.size) {
            mPagerAdapter.addPage(PageAdapter.PageFragmentContent(titles[i], i))
        }
        viewPager.adapter = mPagerAdapter
        viewPager.offscreenPageLimit = 3
        mPagerAdapter.notifyDataSetChanged()
        id_environmental_control_tab.setupWithViewPager(viewPager)
    }

}
