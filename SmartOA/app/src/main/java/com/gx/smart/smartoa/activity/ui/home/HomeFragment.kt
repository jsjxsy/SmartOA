package com.gx.smart.smartoa.activity.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.drakeet.multitype.MultiTypeAdapter
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.MainActivity
import com.gx.smart.smartoa.activity.ui.features.*
import com.gx.smart.smartoa.activity.ui.messages.MessageActivity
import com.gx.smart.smartoa.data.network.api.AppActivityService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.core.grpc.lib.common.QueryDto
import com.gx.wisestone.work.app.grpc.activity.ActivityCommonResponse
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.right_nav_Image_view -> startActivity(
                Intent(
                    activity,
                    MessageActivity::class.java
                )
            )
        }

    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var homeHeadViewBinder: HomeHeadViewBinder
    private lateinit var homeActionViewBinder: HomeActionViewBinder
    private lateinit var viewModel: HomeViewModel
    private val adapter = MultiTypeAdapter()
    private val items = ArrayList<Any>()
    private lateinit var context: FragmentActivity
    private lateinit var mRefreshLayout: SmartRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.TRANSPARENT
    }

    override fun onStart() {
        super.onStart()
        activity?.window?.statusBarColor = Color.TRANSPARENT
        (activity as MainActivity).stateSetting()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
        context = requireActivity()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRefreshLayout = refreshLayout

        homeHeadViewBinder = HomeHeadViewBinder()
        adapter.register(homeHeadViewBinder)

        homeActionViewBinder = HomeActionViewBinder()
        homeActionViewBinder.mRefreshLayout = mRefreshLayout
        adapter.register(homeActionViewBinder)

        adapter.register(DividerViewBinder())
        adapter.register(HomeCompanyAdviseViewBinder())
        homeRecyclerView.adapter = adapter

        items.add(HomeHead())
        items.add(Divider())
        items.add(HomeActionRecommend())
        items.add(Divider())

        val advises = arrayListOf(
            CompanyAdvise(R.mipmap.home_banner_test, "1 广信篮球队报名开始啦！", "2019-10-10 14:39"),
            CompanyAdvise(R.mipmap.home_banner_test, "2 广信篮球队报名开始啦！", "2019-10-10 14:39"),
            CompanyAdvise(R.mipmap.home_banner_test, "3 广信篮球队报名开始啦！", "2019-10-10 14:39")
        )
        items.add(HomeCompanyAdvise(advises))
        items.add(Divider())
        adapter.items = items
        adapter.notifyDataSetChanged()


        refreshLayout.setOnRefreshListener {
            homeHeadViewBinder.hasNotReadMessage()
            homeHeadViewBinder.carouselFigure()
            homeActionViewBinder.findAllApplyInfos()
        }
        refreshLayout.isEnableLoadmore = false
    }



}
