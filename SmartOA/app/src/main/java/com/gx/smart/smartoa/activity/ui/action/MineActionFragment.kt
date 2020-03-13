package com.gx.smart.smartoa.activity.ui.action


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.eventbus.EventBusMessageConstant
import com.gx.smart.smartoa.R
import com.gx.smart.common.AppConfig
import com.gx.smart.smartoa.databinding.FragmentMineActionBinding
import com.gx.wisestone.work.app.grpc.activity.ActivityCommonResponse
import com.gx.wisestone.work.app.grpc.employee.AppMyCompanyResponse
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.android.synthetic.main.fragment_mine_action.emptyLayout
import kotlinx.android.synthetic.main.fragment_mine_action.refreshLayout
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_common_title.title
import kotlinx.android.synthetic.main.list_action_layout.recyclerView

/**
 * A simple [Fragment] subclass.
 */
class MineActionFragment : Fragment(), View.OnClickListener {
    private var fromMine: Boolean = false//是否是? "我的活动"
    private var fromMore: Boolean = false //首页活动更多
    private var readAllFlag: Boolean = false// 是否全部已读
    private lateinit var adapter: ActionAdapter
    private val onItemClick = object : ActionAdapter.OnItemClickListener {

        override fun onItemClick(view: View, position: Int) {
            joinCompanyContinue(position)
        }

    }

    companion object {
        fun newInstance() = MineActionFragment()
        const val ACTION_TYPE = 1
    }

    private val viewModel by lazy { ViewModelProvider(this).get(ActionViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fromMine = activity?.intent?.hasExtra(MineActionActivity.FROM_MINE) ?: false
        fromMore = activity?.intent?.hasExtra(MineActionActivity.FROM_MORE) ?: false
        viewModel.fromMine.value = fromMine
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBindingUtil = DataBindingUtil.inflate<FragmentMineActionBinding>(
            inflater,
            R.layout.fragment_mine_action,
            container,
            false
        )
        dataBindingUtil.lifecycleOwner = this
        dataBindingUtil.viewModel = viewModel
        return dataBindingUtil.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTitle()
        initContent()
        initObserve()
    }

    private fun initObserve() {
        viewModel.activityData.observe(this, Observer {
            callBackHandler(it)
        })
        viewModel.companyData.observe(this, Observer {
            callBackHandler(it)
        })
    }

    private fun initTitle() {
        when {
            fromMine -> {
                title.visibility = View.VISIBLE
                left_nav_image_view?.let {
                    it.visibility = View.VISIBLE
                    it.setOnClickListener(this)
                }
                center_title.let {
                    it.visibility = View.VISIBLE
                    it.text = getString(R.string.mine_action)
                }
            }
            fromMore -> {
                title.visibility = View.VISIBLE
                left_nav_image_view?.let {
                    it.visibility = View.VISIBLE
                    it.setOnClickListener(this)
                }
                center_title.let {
                    it.visibility = View.VISIBLE
                    it.text = getString(R.string.action)
                }
            }
            else -> {
                title.visibility = View.GONE
            }
        }

    }

    private fun initContent() {
        adapter = ActionAdapter()
        adapter.onItemClick = onItemClick
        recyclerView.adapter = adapter
        refreshLayout.autoRefresh()
    }

    var activityFlag: Boolean = false //从详情页返回要刷新数据
    override fun onStart() {
        super.onStart()
        if (activityFlag) {
            refreshLayout.autoRefresh()
            activityFlag = false
        }
    }

    override fun onStop() {
        super.onStop()
        activityFlag = true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }

    }

    private fun callBackHandler(result: AppMyCompanyResponse?) {
        if (result == null) {
            refreshLayout.finishRefresh()
            ToastUtils.showLong("查询我的企业超时!")
            return
        }
        if (result.code == 100) {
            val employeeList = result.employeeInfoList
            if (employeeList.isNotEmpty()) {
                val employeeInfo = employeeList[0]
                SPUtils.getInstance()
                    .put(AppConfig.EMPLOYEE_ID, employeeInfo.employeeId)
                SPUtils.getInstance()
                    .put(AppConfig.COMPANY_SYS_TENANT_NO, employeeInfo.tenantNo)
                SPUtils.getInstance()
                    .put(
                        AppConfig.SMART_HOME_SN,
                        employeeInfo.appDepartmentInfo.smartHomeSn
                    )
                SPUtils.getInstance()
                    .put(
                        AppConfig.ROOM_ID,
                        employeeInfo.appDepartmentInfo.smartHomeId
                    )
                SPUtils.getInstance()
                    .put(AppConfig.PLACE_NAME, employeeInfo.buildingName)
                SPUtils.getInstance()
                    .put(AppConfig.COMPANY_NAME, employeeInfo.companyName)
                SPUtils.getInstance()
                    .put(AppConfig.COMPANY_APPLY_STATUS, employeeInfo.status)
                viewModel.findMyApplyInfos()
            } else {
                refreshLayout.finishRefresh()
                ToastUtils.showLong("企业申请还没通过!")
                emptyLayout.visibility = View.VISIBLE
            }

        } else {
            emptyLayout.visibility = View.VISIBLE
            ToastUtils.showLong(result.msg)
        }
    }

    private fun callBackHandler(result: ActivityCommonResponse?) {
        if (viewModel.currentPage.value == 0) {
            //点击全部已读延迟2秒
            if (readAllFlag) {
                refreshLayout.finishRefresh(1000 * 2)
            } else {
                refreshLayout.finishRefresh()
            }
        } else {
            refreshLayout.finishLoadmore()
        }
        if (result == null) {
            ToastUtils.showLong("查询活动超时!")
            return
        }
        if (result.code == 100) {
            val list = result.contentList
            if (list.isEmpty() && viewModel.currentPage.value == 0) {
                emptyLayout.visibility = View.VISIBLE
            } else {
                emptyLayout.visibility = View.GONE
                if (list.isNotEmpty()) {
                    adapter.mList.toMutableList().apply {
                        //当page == 0, 清空集合再添加
                        if (viewModel.currentPage.value == 0) {
                            clear()
                        }
                        addAll(list)
                        adapter.mList = this
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    //已经加载全部数据
                    refreshLayout.isLoadmoreFinished = true
                }
            }
        } else {
            emptyLayout.visibility = View.VISIBLE
            ToastUtils.showLong(result.msg)
        }
    }

    fun readAllMessage() {
        refreshLayout.autoRefresh()
        readAllFlag = true
        val list = adapter.mList
        if (list.isEmpty()) {
            return
        }
        for (item in list) {
            if (!item.hasRead) {
                viewModel.messageRead(item.activityId, 3)
            }

        }
    }

    //加入公司
    private fun joinCompanyContinue(position: Int) {
        val buildingSysTenantNo =
            SPUtils.getInstance().getInt(AppConfig.BUILDING_SYS_TENANT_NO, 0)
        val companySysTenantNo =
            SPUtils.getInstance().getInt(AppConfig.COMPANY_SYS_TENANT_NO, 0)
        if (buildingSysTenantNo == companySysTenantNo) {
            when (SPUtils.getInstance().getInt(AppConfig.COMPANY_APPLY_STATUS, 0)) {
                1 -> LiveEventBus.get(
                    EventBusMessageConstant.COMPANY_APPLY_STATUS_KEY,
                    Int::class.java
                )
                    .post(1)
                2 -> goActionDetail(position)
                else -> LiveEventBus.get(
                    EventBusMessageConstant.COMPANY_APPLY_STATUS_KEY,
                    Int::class.java
                )
                    .post(3)
            }

        } else {
            LiveEventBus.get(EventBusMessageConstant.COMPANY_APPLY_STATUS_KEY, Int::class.java)
                .post(3)
        }
    }

    private fun goActionDetail(position: Int) {
        val item = adapter.mList[position]
        if (!item.hasRead) {
            viewModel.messageRead(item.activityId, 3)
        }
        val args = Bundle()
        args.putString(MineActionDetailFragment.ARG_TITLE, item.title)
        val startTime = TimeUtils.millis2String(item.startTime, "yyyy.MM.dd")
        val endTime = TimeUtils.millis2String(item.endTime, "yyyy.MM.dd")
        args.putString(
            MineActionDetailFragment.ARG_TIME,
            "$startTime - $endTime"
        )
        args.putString(MineActionDetailFragment.ARG_CONTENT, item.content)
        args.putLong(MineActionDetailFragment.ARG_ACTIVITY_ID, item.activityId)
        if (fromMine || fromMore) {
            findNavController().navigate(
                R.id.action_mineActionFragment_to_mineActionDetailFragment,
                args
            )
        } else {
            findNavController().navigate(R.id.action_global_mineActionActivity, args)
        }
    }


}
