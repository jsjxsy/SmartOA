package com.gx.smart.smartoa.activity.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.action.MineActionFragment
import com.gx.smart.smartoa.data.network.api.AppInformationService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.information.MessageReadResponse
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
        viewModel = ViewModelProviders.of(this).get(MessagesViewModel::class.java)
        // TODO: Use the ViewModel
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
        mPagerAdapter = PageAdapter(childFragmentManager!!)
        for (i in titles.indices) {
            mPagerAdapter.addPage(PageAdapter.PageFragmentContent(titles[i], i + 1))
        }
        viewPager.adapter = mPagerAdapter
        viewPager.offscreenPageLimit = 3
        mPagerAdapter.notifyDataSetChanged()
        id_message_tab.setupWithViewPager(viewPager)
    }

    private fun initContent() {
        var flag = (activity?.intent?.hasExtra(MessageActivity.INTENT_KEY)!! &&
                activity?.intent?.getStringExtra(MessageActivity.INTENT_KEY)!! == MessageActivity.INTENT_MESSAGE)

        if (flag) {
            viewPager.currentItem = 1
        }

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                var fragment = mPagerAdapter.getItem(position)
                right_nav_text_view.setOnClickListener {
                    when(fragment) {
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
