package com.gx.smart.smartoa.activity.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.action.MineActionFragment
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.messages_fragment.*

class MessagesFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }


    companion object {
        fun newInstance() = MessagesFragment()
    }

    private lateinit var viewModel: MessagesViewModel
    private lateinit var mPagerAdapter: PageAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.messages_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[MessagesViewModel::class.java]
        initTitle()
        initData()
        initContent()
    }

    private fun initTitle() {
        left_nav_image_view.visibility = View.VISIBLE
        left_nav_image_view.setOnClickListener(this)
        right_nav_text_view.visibility = View.VISIBLE
        right_nav_text_view.text = getString(R.string.all_read)
        center_title.visibility = View.VISIBLE
        center_title.text = getString(R.string.message_notice)
    }

    private fun initData() {

        val titles = resources.getStringArray(R.array.message_items)
        mPagerAdapter = PageAdapter(activity!!)
        viewPager.adapter = mPagerAdapter
        viewPager.offscreenPageLimit = 3
        viewPager.isUserInputEnabled = true
        mPagerAdapter.notifyDataSetChanged()
        TabLayoutMediator(tabLayout, viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = titles[position]
            }).attach()
    }

    private fun initContent() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                var fragment = mPagerAdapter.fragments[position]
                right_nav_text_view.setOnClickListener {
                    when (fragment) {
                        is NewsFragment -> {
                            fragment.readAllMessage()
                        }
                        is MineActionFragment -> {
                            fragment.readAllMessage()
                        }
                        is NoticeFragment -> {
                            fragment.readAllMessage()
                        }
                    }
                }
            }
        })
    }


}
