package com.gx.smart.smartoa.activity.ui.home

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.gx.smart.eventbus.EventBusMessageConstant
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.MainActivity
import com.gx.smart.smartoa.activity.ui.company.MineCompanyActivity
import com.gx.smart.smartoa.activity.ui.features.Divider
import com.gx.smart.smartoa.activity.ui.features.DividerViewBinder
import com.gx.smart.smartoa.activity.ui.home.company.CompanyAdvise
import com.gx.smart.smartoa.activity.ui.home.company.HomeCompanyAdvise
import com.gx.smart.smartoa.activity.ui.home.company.HomeCompanyAdviseViewBinder
import com.gx.smart.smartoa.activity.ui.messages.MessageActivity
import com.gx.smart.smartoa.base.BaseFragment
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.databinding.FragmentHomeBinding
import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_common_title.*


class HomeFragment : BaseFragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_text_view -> {
                mineCompanyActivity()
            }
            R.id.right_nav_Image_view ->
                ActivityUtils.startActivity(
                    Intent(
                        ActivityUtils.getTopActivity(),
                        MessageActivity::class.java
                    )
                )
        }

    }

    companion object {
        fun newInstance() = HomeFragment()
        const val PLACE_REQUEST = 11
    }

    private lateinit var redPotView: View
    private lateinit var homeHeadViewBinder: HomeHeadViewBinder
    private lateinit var homeActionViewBinder: HomeActionViewBinder
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


    private val viewModel by lazy { ViewModelProviders.of(this).get(HomeViewModel::class.java) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeFragmentBinding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        homeFragmentBinding.lifecycleOwner = this
        homeFragmentBinding.viewModel = viewModel
        return homeFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTitleView()
        initRecyclerView()
        initEventBus()
        initObserver()
    }

    private fun initObserver() {
        viewModel.dataChange.observe(this, Observer {
            if (it) {
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.unReadMessage.observe(this, Observer {
            when {
                it -> {
                    redPotView.visibility = View.VISIBLE
                }
                else -> {
                    redPotView.visibility = View.GONE
                }
            }

        })

    }

    private fun initEventBus() {
        LiveEventBus.get(EventBusMessageConstant.REFRESH_KEY, Boolean::class.java)
            .observe(this, Observer {
                if (it) {
                    mRefreshLayout.finishRefresh()
                }
            })
    }

    private fun initRecyclerView() {
        mRefreshLayout = refreshLayout
        homeHeadViewBinder = HomeHeadViewBinder(viewModel)
        adapter.register(homeHeadViewBinder)

        homeActionViewBinder = HomeActionViewBinder(viewModel)
        adapter.register(homeActionViewBinder)

        adapter.register(DividerViewBinder())
        adapter.register(HomeCompanyAdviseViewBinder())
        homeRecyclerView.adapter = adapter

        items.add(HomeHead())
        items.add(Divider())
        items.add(HomeActionRecommend())
        items.add(Divider())

        val advises = arrayListOf(
            CompanyAdvise(
                R.mipmap.company_action,
                "航天广信 | 智慧办公综合服务平台为企业员工创造更大价值！",
                "2019.10.10"
            ),
            CompanyAdvise(
                R.mipmap.company_action,
                "航天广信 | 智慧办公综合服务平台为企业员工创造更大价值！",
                "2020.01.12"
            ),
            CompanyAdvise(
                R.mipmap.company_action,
                "航天广信 | 智慧办公综合服务平台为企业员工创造更大价值！",
                "2020.02.02"
            )
        )
        items.add(
            HomeCompanyAdvise(
                advises
            )
        )
        items.add(Divider())
        adapter.items = items
        adapter.notifyDataSetChanged()
        refreshLayout.autoRefresh()
    }


    private fun initTitleView() {
        title.setBackgroundColor(Color.TRANSPARENT)
        left_nav_text_view?.let {
            it.visibility = View.VISIBLE
            it.text = SPUtils.getInstance().getString(AppConfig.PLACE_NAME, "")
            it.setOnClickListener(this)

        }
        right_nav_Image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        redPotView = id_message_red_point
    }


    private fun mineCompanyActivity() {
        val intent = Intent(
            requireActivity(),
            MineCompanyActivity::class.java
        )
        intent.putExtra(MineCompanyActivity.FROM_HOME, MineCompanyActivity.FROM_HOME)
        ActivityUtils.startActivityForResult(activity!!, intent, PLACE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PLACE_REQUEST -> if (resultCode == Activity.RESULT_OK) {
                left_nav_text_view?.let {
                    it.text = SPUtils.getInstance().getString(AppConfig.PLACE_NAME, "")
                }
                refreshLayout.autoRefresh()
            }
        }
    }


}
