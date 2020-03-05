package com.gx.smart.smartoa.activity.ui.action

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.common.AppConfig
import com.gx.smart.lib.http.api.AppActivityService
import com.gx.smart.lib.http.api.AppEmployeeService
import com.gx.smart.lib.http.api.AppInformationService
import com.gx.smart.lib.http.base.CallBack
import com.gx.wisestone.core.grpc.lib.common.QueryDto
import com.gx.wisestone.work.app.grpc.activity.ActivityCommonResponse
import com.gx.wisestone.work.app.grpc.activity.ActivityRequest
import com.gx.wisestone.work.app.grpc.employee.AppMyCompanyResponse
import com.gx.wisestone.work.app.grpc.information.MessageReadResponse
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

class ActionViewModel : ViewModel() {
    var fromMine = MutableLiveData<Boolean>(false)//是否是? "我的活动"
//    private var fromMore = MutableLiveData<Boolean>(false) //首页活动更多
//    private var readAllFlag: Boolean = false// 是否全部已读

    var currentPage = MutableLiveData(0) //当前第几页
    var activityData = MutableLiveData<ActivityCommonResponse?>()  //活动数据，返回结果
    var companyData = MutableLiveData<AppMyCompanyResponse?>() //公司数据
    val refreshListener = OnRefreshListener {
        currentPage.value = 0
        if (fromMine.value!!) {
            myCompany()
        } else {
            findAllActivityInfos()
        }
    }

    val loadMoreListener = OnLoadmoreListener{
        currentPage.value?.plus(1)
        if (fromMine.value!!) {
            findMyApplyInfos()
        } else {
            findAllActivityInfos()
        }
    }

    /**
     * 获取个人活动列表
     */
    fun findMyApplyInfos() {
        val query = QueryDto.newBuilder()
            .setPage(currentPage.value!!)
            .setPageSize(10)
            .build()

        AppActivityService.getInstance()
            .findMyApplyInfos(query, object : CallBack<ActivityCommonResponse>() {
                override fun callBack(result: ActivityCommonResponse?) {
                    activityData.value = result
                }
            })
    }


    /**
     * 获取所有活动
     */
    private fun findAllActivityInfos() {
        val companyId = SPUtils.getInstance()
            .getLong(AppConfig.COMPANY_STRUCTURE_ID, 0L)

        val request = ActivityRequest.newBuilder()
            .setAuthorCompanyId(companyId)
            .build()

        val query = QueryDto.newBuilder()
            .setPage(currentPage.value!!)
            .setPageSize(10)
            .build()

        AppActivityService.getInstance()
            .findAllActivityInfos(request, query, object : CallBack<ActivityCommonResponse>() {
                override fun callBack(result: ActivityCommonResponse?) {
                    activityData.value = result
                }

            })
    }

    /**
     *已读
     */
    fun messageRead(messageId: Long, type: Int) {

        AppInformationService.getInstance()
            .messageRead(messageId, type, object : CallBack<MessageReadResponse>() {
                override fun callBack(result: MessageReadResponse?) {

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

    /**
     * 公司
     */
    private fun myCompany() {
        AppEmployeeService.getInstance()
            .myCompany(
                object : CallBack<AppMyCompanyResponse>() {
                    override fun callBack(result: AppMyCompanyResponse?) {
                        companyData.value = result
                    }
                })
    }

}
