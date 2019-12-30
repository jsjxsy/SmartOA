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
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.WebViewActivity
import com.gx.smart.smartoa.data.network.ApiConfig
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AppActivityService
import com.gx.smart.smartoa.data.network.api.AppEmployeeService
import com.gx.smart.smartoa.data.network.api.AppInformationService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.core.grpc.lib.common.QueryDto
import com.gx.wisestone.work.app.grpc.activity.ActivityCommonResponse
import com.gx.wisestone.work.app.grpc.employee.AppMyCompanyResponse
import com.gx.wisestone.work.app.grpc.information.MessageReadResponse
import kotlinx.android.synthetic.main.fragment_mine_action.*
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_common_title.title
import kotlinx.android.synthetic.main.list_action_layout.recyclerView
import kotlinx.android.synthetic.main.register_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class MineActionFragment : Fragment(), View.OnClickListener {
    var flag: Boolean = false
    lateinit var adapter: ActionAdapter

    companion object {
        fun newInstance() = MineActionFragment()
        const val ACTION_TYPE = 2
    }


    private lateinit var viewModel: ActionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flag = activity?.intent?.hasExtra("fromMine") ?: false
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
        } else {
            title.visibility = View.GONE
        }

    }

    private fun initContent() {
        registerAgreement.setOnClickListener(this)
        adapter = ActionAdapter()
        val onItemClick = object : ActionAdapter.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                val item = adapter.mList!![position]
                messageRead(item.activityId, 3)
                val args = Bundle()
                args.putString(MineActionDetailFragment.ARG_TITLE, item.title)
                args.putString(
                    MineActionDetailFragment.ARG_TIME,
                    "${item.startTime - item.endTime}"
                )
                args.putString(MineActionDetailFragment.ARG_CONTENT, item.content)
                args.putLong(MineActionDetailFragment.ARG_ACTIVITY_ID, item.activityId)
                if (flag) {
                    findNavController().navigate(
                        R.id.action_mineActionFragment_to_mineActionDetailFragment,
                        args
                    )
                } else {
                    findNavController().navigate(R.id.action_global_mineActionActivity, args)
                }

            }

        }
        adapter.onItemClick = onItemClick
        recyclerView.adapter = adapter
        refreshLayout.setOnRefreshListener {
            if (flag) {
                myCompany()
            } else {
                findAllActivityInfos()
            }
        }
        refreshLayout.autoRefresh()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.registerAgreement -> goWebView(ApiConfig.WEB_AGREEMENT_URL)
        }

    }


    private fun goWebView(url: String) {
        val intent = Intent(ActivityUtils.getTopActivity(), WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.URL, url)
        ActivityUtils.startActivity(intent)
    }


    private fun findMyApplyInfos() {
        val query = QueryDto.newBuilder()
            .setPage(0)
            .setPageSize(10)
            .build()
        AppActivityService.getInstance()
            .findMyApplyInfos(query, object : CallBack<ActivityCommonResponse>() {
                override fun callBack(result: ActivityCommonResponse?) {
                    refreshLayout.finishRefresh()
                    if (result == null) {
                        ToastUtils.showLong("查询活动超时!")
                        return
                    }
                    if (result?.code == 100) {
                        val list = result?.contentList
                        if (list.isEmpty()) {
                            emptyLayout.visibility = View.VISIBLE
                        } else {
                            emptyLayout.visibility = View.GONE
                            adapter.mList = list
                            adapter.notifyDataSetChanged()
                        }

                    } else {
                        emptyLayout.visibility = View.VISIBLE
                        ToastUtils.showLong(result?.msg)
                    }
                }

            })
    }


    private fun messageRead(messageId: Long, type: Int) {
        AppInformationService.getInstance()
            .messageRead(messageId, type, object : CallBack<MessageReadResponse>() {
                override fun callBack(result: MessageReadResponse?) {
                    if (result == null) {
                        ToastUtils.showLong("查询活动超时!")
                        return
                    }
                    if (result?.code == 100) {
                        //ToastUtils.showLong("成功")
                    } else {
                        ToastUtils.showLong(result?.msg)
                    }
                }

            })
    }


    private fun myCompany() {
        AppEmployeeService.getInstance()
            .myCompany(
                object : CallBack<AppMyCompanyResponse>() {
                    override fun callBack(result: AppMyCompanyResponse?) {
                        if (result == null) {
                            refreshLayout.finishRefresh()
                            ToastUtils.showLong("查询我的企业超时!")
                            return
                        }
                        if (result?.code == 100) {
                            val employeeList = result.employeeInfoList
                            if (employeeList.isNotEmpty()) {
                                val employeeInfo = employeeList[0]
                                AppConfig.employeeId = employeeInfo.employeeId
                                AppConfig.currentSysTenantNo = employeeInfo.tenantNo
                                AppConfig.SMART_HOME_SN = employeeInfo.appDepartmentInfo.smartHomeSn
                                AppConfig.ROOM_ID = employeeInfo.appDepartmentInfo.smartHomeId
                                findMyApplyInfos()
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


    private fun findAllActivityInfos() {
        AppActivityService.getInstance()
            .findAllActivityInfos(0, object : CallBack<ActivityCommonResponse>() {
                override fun callBack(result: ActivityCommonResponse?) {
                    refreshLayout.finishRefresh()
                    if (result == null) {
                        ToastUtils.showLong("查询活动超时!")
                        return
                    }
                    if (result?.code == 100) {
                        val list = result.contentList
                        if (list.isEmpty()) {
                            emptyLayout.visibility = View.VISIBLE
                        } else {
                            emptyLayout.visibility = View.GONE
                            adapter.mList = list
                            adapter.notifyDataSetChanged()
                        }
                    } else {
                        emptyLayout.visibility = View.VISIBLE
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
    }


    fun readAllMessage() {
        val list = adapter.mList
        if (list == null || list.isEmpty()) {
            return
        }
        for( item in list) {
            messageRead(item.activityId, 1)
        }

    }


}
