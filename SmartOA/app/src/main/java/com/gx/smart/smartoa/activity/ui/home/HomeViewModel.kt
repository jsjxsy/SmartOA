package com.gx.smart.smartoa.activity.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.eventbus.EventBusMessageConstant
import com.gx.smart.smartoa.R
import com.gx.smart.common.AppConfig
import com.gx.smart.lib.http.api.AppActivityService
import com.gx.smart.lib.http.api.AppEmployeeService
import com.gx.smart.lib.http.api.AppFigureService
import com.gx.smart.lib.http.api.UserCenterService
import com.gx.smart.lib.http.base.CallBack
import com.gx.wisestone.core.grpc.lib.common.QueryDto
import com.gx.wisestone.work.app.grpc.activity.ActivityCommonResponse
import com.gx.wisestone.work.app.grpc.activity.ActivityRequest
import com.gx.wisestone.work.app.grpc.activity.AppActivityDto
import com.gx.wisestone.work.app.grpc.appfigure.ImagesInfoOrBuilder
import com.gx.wisestone.work.app.grpc.appfigure.ImagesResponse
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import com.gx.wisestone.work.app.grpc.employee.AppMyCompanyResponse
import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

class HomeViewModel : ViewModel() {

    var listNetwork = listOf<ImagesInfoOrBuilder>()
    val listLocal = arrayListOf(
        R.mipmap.home_banner_test,
        R.mipmap.home_banner_test,
        R.mipmap.home_banner_test
    )

    var listAction = listOf<AppActivityDto>()
    var unReadMessage = MutableLiveData<Boolean>(false)
    var dataChange = MutableLiveData<Boolean>(false)
    //smart refreshListener 下拉刷新监听
    val refreshListener = OnRefreshListener { onRefresh() }

    private fun carouselFigure() {
        AppFigureService.getInstance().carouselFigure(object : CallBack<ImagesResponse?>() {
            override fun callBack(result: ImagesResponse?) {
                if (result?.code == 100) {
                    val list = result.imagesInfoOrBuilderList.toList()
                    listNetwork.toMutableList().apply {
                        clear()
                        addAll(list)
                        listNetwork = this
                    }
                }
            }
        })
    }

    private fun onRefresh() {
        hasNotReadMessage()
        carouselFigure()
        findAllApplyInfos()
        myCompany()
    }

    /**
     * 推荐活动
     */
    private fun findAllApplyInfos() {
        val query = QueryDto.newBuilder()
            .setPage(0)
            .setPageSize(3)
            .build()
        val companyId = SPUtils.getInstance()
            .getLong(AppConfig.COMPANY_STRUCTURE_ID, 0L)
        var request = ActivityRequest.newBuilder().setAuthorCompanyId(companyId)
            .build()

        AppActivityService.getInstance()
            .findAllActivityInfos(request, query, object : CallBack<ActivityCommonResponse>() {
                override fun callBack(result: ActivityCommonResponse?) {
                    LiveEventBus
                        .get(EventBusMessageConstant.REFRESH_KEY)
                        .post(true)
                    if (result == null) {
                        ToastUtils.showLong("查询活动超时!")
                        return
                    }
                    if (result.code == 100) {
                        listAction = result.contentList.toList()
                    } else {
                        ToastUtils.showLong(result.msg)
                    }
                    dataChange.value = true
                }

            })
    }


    /**
     * 首页消息是否已读未读
     */
    private fun hasNotReadMessage() {
        UserCenterService.getInstance().hasNotReadMessage(object : CallBack<CommonResponse>() {
            override fun callBack(result: CommonResponse?) {
                if (result?.code == 100) {
                    val flag = result.dataMap["hasNotReadMessage"]
                    unReadMessage.value = flag == "true"
                }
            }

        })
    }

    /**
     * 获取最新公司信息，为功能权限验证作准备
     */
    private fun myCompany() {
        AppEmployeeService.getInstance()
            .myCompany(
                object : CallBack<AppMyCompanyResponse>() {
                    override fun callBack(result: AppMyCompanyResponse?) {
                        if (result?.code == 100) {
                            val employeeList = result.employeeInfoList
                            if (employeeList.isNotEmpty()) {
                                val employeeInfo = employeeList[0]
                                SPUtils.getInstance()
                                    .put(AppConfig.EMPLOYEE_ID, employeeInfo.employeeId)
                                SPUtils.getInstance()
                                    .put(
                                        AppConfig.COMPANY_STRUCTURE_ID,
                                        employeeInfo.companyStructureId
                                    )
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
                                    .put(AppConfig.COMPANY_PLACE_NAME, employeeInfo.buildingName)
                                SPUtils.getInstance()
                                    .put(AppConfig.COMPANY_NAME, employeeInfo.companyName)
                                SPUtils.getInstance()
                                    .put(AppConfig.COMPANY_APPLY_STATUS, employeeInfo.status)
                            }
                        }
                    }

                })
    }

}
