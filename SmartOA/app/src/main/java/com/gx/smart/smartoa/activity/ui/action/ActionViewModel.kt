package com.gx.smart.smartoa.activity.ui.action

import android.view.View
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.data.network.api.AppActivityService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.core.grpc.lib.common.QueryDto
import com.gx.wisestone.work.app.grpc.activity.ActivityCommonResponse
import kotlinx.android.synthetic.main.fragment_mine_action.*

class ActionViewModel : ViewModel() {
//    private fun findMyApplyInfos(query: QueryDto) {
//        AppActivityService.getInstance()
//            .findMyApplyInfos(query, object : CallBack<ActivityCommonResponse>() {
//                override fun callBack(result: ActivityCommonResponse?) {
//                    if (currentPage == 0) {
//                        if (readAllFlag) {
//                            refreshLayout.finishRefresh(1000 * 2)
//                            readAllFlag = false
//                        } else {
//                            refreshLayout.finishRefresh()
//                        }
//                    } else {
//                        refreshLayout.finishLoadmore()
//                    }
//
//                    if (result == null) {
//                        ToastUtils.showLong("查询活动超时!")
//                        return
//                    }
//                    if (result.code == 100) {
//                        val list = result.contentList
//                        if (list.isEmpty() && currentPage == 0) {
//                            emptyLayout.visibility = View.VISIBLE
//                        } else {
//                            emptyLayout.visibility = View.GONE
//                            if (list.isNotEmpty()) {
//                                currentPage++
//                                adapter.mList.toMutableList().apply {
//                                    addAll(list)
//                                    adapter.mList = this
//                                }
//                                adapter.notifyDataSetChanged()
//                            }
//
//                        }
//
//                    } else {
//                        emptyLayout.visibility = View.VISIBLE
//                        ToastUtils.showLong(result.msg)
//                    }
//                }
//
//            })
//    }
}
