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
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.drakeet.multitype.MultiTypeAdapter
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.MainActivity
import com.gx.smart.smartoa.activity.ui.features.Divider
import com.gx.smart.smartoa.activity.ui.features.DividerViewBinder
import com.gx.smart.smartoa.activity.ui.features.HomeCompanyAdvise
import com.gx.smart.smartoa.activity.ui.features.HomeCompanyAdviseViewBinder
import com.gx.smart.smartoa.activity.ui.messages.MessageActivity
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AppEmployeeService
import com.gx.smart.smartoa.data.network.api.UserCenterService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import com.gx.wisestone.work.app.grpc.employee.AppMyCompanyResponse
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_common_title.*


class HomeFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
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
    }

    private lateinit var redPotView: View
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
        context = requireActivity()
        initTitleView()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRefreshLayout = refreshLayout

        homeHeadViewBinder = HomeHeadViewBinder()
        homeHeadViewBinder.fragmentManager = fragmentManager!!
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
            hasNotReadMessage()
            homeHeadViewBinder.carouselFigure()
            homeActionViewBinder.findAllApplyInfos()
            myCompany()
        }
        refreshLayout.isEnableLoadmore = false
    }


    private fun initTitleView() {
        title.setBackgroundColor(Color.TRANSPARENT)
        left_nav_text_view?.let {
            it.visibility = View.VISIBLE
            it.text = SPUtils.getInstance().getString(AppConfig.PLACE_NAME, "")

        }
        right_nav_Image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        redPotView = id_message_red_point
        hasNotReadMessage()
    }

    private fun hasNotReadMessage() {
        UserCenterService.getInstance().hasNotReadMessage(object : CallBack<CommonResponse>() {
            override fun callBack(result: CommonResponse?) {
                if (result?.code == 100) {
                    val flag = result.dataMap["hasNotReadMessage"]
                    if (flag == "true") {
                        redPotView.visibility = View.VISIBLE
                    } else {
                        redPotView.visibility = View.GONE
                    }

                }
            }

        })
    }


    private fun myCompany() {
        AppEmployeeService.getInstance()
            .myCompany(
                object : CallBack<AppMyCompanyResponse>() {
                    override fun callBack(result: AppMyCompanyResponse?) {
                        if (result?.code == 100) {
                            val employeeList = result.employeeInfoList
                            if (employeeList.isNotEmpty()) {
                                val employeeInfo = employeeList[0]
                                AppConfig.employeeId = employeeInfo.employeeId
                                AppConfig.currentSysTenantNo = employeeInfo.tenantNo
                                AppConfig.SMART_HOME_SN = employeeInfo.appDepartmentInfo.smartHomeSn
                                AppConfig.ROOM_ID = employeeInfo.appDepartmentInfo.smartHomeId
                                SPUtils.getInstance()
                                    .put(AppConfig.PLACE_NAME, employeeInfo.buildingName)
                                SPUtils.getInstance()
                                    .put(AppConfig.COMPANY_APPLY_STATUS, employeeInfo.status)
                            }
                        }
                    }

                })
    }

}
