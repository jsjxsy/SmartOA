package com.gx.smart.smartoa.activity.ui.action


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.company.MineCompanyActivity
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AppActivityService
import com.gx.smart.smartoa.data.network.api.AppEmployeeService
import com.gx.smart.smartoa.data.network.api.AppInformationService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.core.grpc.lib.common.QueryDto
import com.gx.wisestone.work.app.grpc.activity.ActivityCommonResponse
import com.gx.wisestone.work.app.grpc.activity.ActivityRequest
import com.gx.wisestone.work.app.grpc.employee.AppMyCompanyResponse
import com.gx.wisestone.work.app.grpc.information.MessageReadResponse
import kotlinx.android.synthetic.main.fragment_mine_action.emptyLayout
import kotlinx.android.synthetic.main.fragment_mine_action.refreshLayout
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_common_title.title
import kotlinx.android.synthetic.main.list_action_layout.recyclerView
import top.limuyang2.customldialog.IOSMsgDialog

/**
 * A simple [Fragment] subclass.
 */
class MineActionFragment : Fragment(), View.OnClickListener {
    var flag: Boolean = false
    lateinit var adapter: ActionAdapter
    var fromMore: Boolean = false

    companion object {
        fun newInstance() = MineActionFragment()
        const val ACTION_TYPE = 2
    }


    private var readAllFlag: Boolean = false
    private lateinit var viewModel: ActionViewModel
    private var currentPage = 0
    private var employeeId: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flag = activity?.intent?.hasExtra(MineActionActivity.FROM_MINE) ?: false
        fromMore = activity?.intent?.hasExtra(MineActionActivity.FROM_MORE) ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine_action, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ActionViewModel::class.java)
        val buildingSysTenantNo =
            SPUtils.getInstance().getInt(AppConfig.BUILDING_SYS_TENANT_NO, 0)
        val companySysTenantNo =
            SPUtils.getInstance().getInt(AppConfig.COMPANY_SYS_TENANT_NO, 0)
        if (buildingSysTenantNo == companySysTenantNo) {
            employeeId = SPUtils.getInstance().getLong(AppConfig.EMPLOYEE_ID, 0L)
        }
        initTitle()
        initContent()
    }

    private fun initTitle() {
        if (flag) {
            title.visibility = View.VISIBLE
            left_nav_image_view?.let {
                it.visibility = View.VISIBLE
                it.setOnClickListener(this)
            }
            center_title.let {
                it.visibility = View.VISIBLE
                it.text = getString(R.string.mine_action)
            }
        } else if (fromMore) {
            title.visibility = View.VISIBLE
            left_nav_image_view?.let {
                it.visibility = View.VISIBLE
                it.setOnClickListener(this)
            }
            center_title.let {
                it.visibility = View.VISIBLE
                it.text = getString(R.string.action)
            }
        } else {
            title.visibility = View.GONE
        }

    }

    private fun initContent() {
        adapter = ActionAdapter()
        val onItemClick = object : ActionAdapter.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                joinCompanyContinue(position)
            }

        }
        adapter.onItemClick = onItemClick
        recyclerView.adapter = adapter
        refreshLayout.setOnRefreshListener {
            currentPage = 0
            adapter.mList.toMutableList().apply {
                clear()
                adapter.mList = this
            }
            val query = QueryDto.newBuilder()
                .setPage(currentPage)
                .setPageSize(10)
                .build()
            if (flag) {
                myCompany(query)
            } else {
                findAllActivityInfos(query)
            }
        }
        refreshLayout.setOnLoadmoreListener {
            val query = QueryDto.newBuilder()
                .setPage(currentPage)
                .setPageSize(10)
                .build()
            if (flag) {
                findMyApplyInfos(query)
            } else {

                findAllActivityInfos(query)
            }
        }
        refreshLayout.autoRefresh()

    }

    var activityFlag: Boolean = false
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


    private fun findMyApplyInfos(query: QueryDto) {
        if (!ActivityUtils.isActivityAlive(activity)) {
            return
        }

        if (employeeId == 0L) {
            refreshLayout.finishRefresh()
            emptyLayout.visibility = View.VISIBLE
            return
        }

        AppActivityService.getInstance()
            .findMyApplyInfos(query, object : CallBack<ActivityCommonResponse>() {
                override fun callBack(result: ActivityCommonResponse?) {
                    if (!ActivityUtils.isActivityAlive(activity)) {
                        return
                    }
                    if (currentPage == 0) {
                        if (readAllFlag) {
                            refreshLayout.finishRefresh(1000 * 2)
                            readAllFlag = false
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
                        if (list.isEmpty() && currentPage == 0) {
                            emptyLayout.visibility = View.VISIBLE
                        } else {
                            emptyLayout.visibility = View.GONE
                            if (list.isNotEmpty()) {
                                currentPage++
                                adapter.mList.toMutableList().apply {
                                    addAll(list)
                                    adapter.mList = this
                                }
                                adapter.notifyDataSetChanged()
                            }

                        }

                    } else {
                        emptyLayout.visibility = View.VISIBLE
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
    }


    private fun messageRead(messageId: Long, type: Int) {
        if (employeeId == 0L) {
            return
        }

        AppInformationService.getInstance()
            .messageRead(messageId, type, object : CallBack<MessageReadResponse>() {
                override fun callBack(result: MessageReadResponse?) {
                    if (!ActivityUtils.isActivityAlive(activity)) {
                        return
                    }

                    if (result == null) {
                        ToastUtils.showLong("查询活动超时!")
                        return
                    }
                    if (result.code == 100) {
                        //ToastUtils.showLong("成功")
                    } else {
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
    }


    private fun myCompany(query: QueryDto) {
        AppEmployeeService.getInstance()
            .myCompany(
                object : CallBack<AppMyCompanyResponse>() {
                    override fun callBack(result: AppMyCompanyResponse?) {
                        if (!ActivityUtils.isActivityAlive(activity)) {
                            return
                        }
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
                                findMyApplyInfos(query)
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

                })
    }


    private fun findAllActivityInfos(query: QueryDto) {
        if (!ActivityUtils.isActivityAlive(activity)) {
            return
        }
        val companyId = SPUtils.getInstance()
            .getLong(AppConfig.COMPANY_STRUCTURE_ID, 0L)

        var request = ActivityRequest.newBuilder().setAuthorCompanyId(companyId)
            .build()

        AppActivityService.getInstance()
            .findAllActivityInfos(request, query, object : CallBack<ActivityCommonResponse>() {
                override fun callBack(result: ActivityCommonResponse?) {
                    if (!ActivityUtils.isActivityAlive(activity)) {
                        return
                    }
                    if (currentPage == 0) {
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
                        if (list.isEmpty() && currentPage == 0) {
                            emptyLayout.visibility = View.VISIBLE
                        } else {
                            emptyLayout.visibility = View.GONE
                            if (list.isNotEmpty()) {
                                currentPage++
                                adapter.mList.toMutableList().apply {
                                    addAll(list)
                                    adapter.mList = this
                                }
                                adapter.notifyDataSetChanged()
                            } else {
                                refreshLayout.isLoadmoreFinished = true
                            }
                        }
                    } else {
                        emptyLayout.visibility = View.VISIBLE
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
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
                messageRead(item.activityId, 3)
            }

        }
    }

    private fun joinCompanyContinue(position: Int) {
        val buildingSysTenantNo =
            SPUtils.getInstance().getInt(AppConfig.BUILDING_SYS_TENANT_NO, 0)
        val companySysTenantNo =
            SPUtils.getInstance().getInt(AppConfig.COMPANY_SYS_TENANT_NO, 0)
        if (buildingSysTenantNo == companySysTenantNo) {
            when (SPUtils.getInstance().getInt(AppConfig.COMPANY_APPLY_STATUS, 0)) {
                1 -> IOSMsgDialog.init(fragmentManager!!)
                    .setTitle("加入企业")
                    .setMessage("您申请的企业在审核中，请耐心等待")
                    .setPositiveButton("确定").show()
                2 -> goActionDetail(position)
                else -> IOSMsgDialog.init(fragmentManager!!)
                    .setTitle("加入企业")
                    .setMessage("您还未入驻任何企业，请先进行企业身份认证")
                    .setPositiveButton("马上认证", View.OnClickListener {
                        ActivityUtils.startActivity(
                            Intent(
                                ActivityUtils.getTopActivity(),
                                MineCompanyActivity::class.java
                            )
                        )
                    }).show()
            }

        } else {
            IOSMsgDialog.init(fragmentManager!!)
                .setTitle("加入企业")
                .setMessage("您还未入驻任何企业，请先进行企业身份认证")
                .setPositiveButton("马上认证", View.OnClickListener {
                    ActivityUtils.startActivity(
                        Intent(
                            ActivityUtils.getTopActivity(),
                            MineCompanyActivity::class.java
                        )
                    )
                }).show()
        }
    }

    private fun goActionDetail(position: Int) {
        val item = adapter.mList[position]
        if (!item.hasRead) {
            messageRead(item.activityId, 3)
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
        if (flag || fromMore) {
            findNavController().navigate(
                R.id.action_mineActionFragment_to_mineActionDetailFragment,
                args
            )
        } else {
            findNavController().navigate(R.id.action_global_mineActionActivity, args)
        }
    }


}
