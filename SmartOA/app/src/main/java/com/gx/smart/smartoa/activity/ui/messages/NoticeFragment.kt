package com.gx.smart.smartoa.activity.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AppInformationService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.core.grpc.lib.common.QueryDto
import com.gx.wisestone.work.app.grpc.information.AppAnnouncementResponse
import com.gx.wisestone.work.app.grpc.information.MessageReadResponse
import kotlinx.android.synthetic.main.fragment_mine_action.*
import kotlinx.android.synthetic.main.notice_fragment.*
import kotlinx.android.synthetic.main.notice_fragment.emptyLayout
import kotlinx.android.synthetic.main.notice_fragment.recyclerView
import kotlinx.android.synthetic.main.notice_fragment.refreshLayout

class NoticeFragment : Fragment() {

    companion object {
        fun newInstance() = NoticeFragment()
        const val NOTICE_TYPE = 3
    }

    private var readAllFlag: Boolean = false

    private lateinit var adapter: NoticeAdapter
    private lateinit var viewModel: NoticeViewModel
    private var currentPage = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notice_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NoticeViewModel::class.java)
        initContent()
    }

    private fun initContent() {
        adapter = NoticeAdapter()
        val onItemClick = object : NoticeAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                val list = adapter.getList()
                val item = list!![position]
                if (!item.hasRead) {
                    messageRead(item.id, 2)
                }
                val args = Bundle()
                args.putString(DetailFragment.ARG_TITLE, item.title)
                args.putString(DetailFragment.ARG_CONTENT, item.content)
                Navigation.findNavController(activity!!, R.id.messagesFragmentEnter)
                    .navigate(R.id.action_noticeFragment_to_detailFragment, args)
            }

        }
        adapter.setOnItemClick(onItemClick)
        recyclerView.adapter = adapter
        refreshLayout.setOnRefreshListener {
            adapter.clear()
            currentPage = 0
            val query = QueryDto
                .newBuilder()
                .setPageSize(10)
                .setPage(currentPage)
                .build()
            getAnnouncement(query)
        }
        refreshLayout.setOnLoadmoreListener {
            val query = QueryDto
                .newBuilder()
                .setPageSize(10)
                .setPage(currentPage)
                .build()
            getAnnouncement(query)
        }
        refreshLayout.autoRefresh()
    }


    private fun getAnnouncement(query: QueryDto) {
        if (AppConfig.employeeId == 0L) {
            refreshLayout.finishRefresh()
            emptyLayout.visibility = View.VISIBLE
            return
        }

        AppInformationService.getInstance()
            .getAnnouncement(query, object : CallBack<AppAnnouncementResponse>() {
                override fun callBack(result: AppAnnouncementResponse?) {
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
                        ToastUtils.showLong("获取消息超时!")
                        return
                    }
                    if (result?.code == 100) {
                        val appAnnouncementDtoList =
                            result.appAnnouncementDtoList.toList()
                        if (appAnnouncementDtoList.isEmpty() && currentPage == 0) {
                            emptyLayout.visibility = View.VISIBLE
                        } else {
                            emptyLayout.visibility = View.GONE
                            if (appAnnouncementDtoList.isNotEmpty()) {
                                currentPage++
                                adapter.addList(appAnnouncementDtoList)
                                adapter.notifyDataSetChanged()
                            }
                        }
                    } else {
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
    }


    private fun messageRead(messageId: Long, type: Int) {
        if (AppConfig.employeeId == 0L) {
            return
        }
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


    fun readAllMessage() {
        if (AppConfig.employeeId == 0L) {
            return
        }
        refreshLayout.autoRefresh()
        readAllFlag = true
        val list = adapter.getList()
        if (list == null || list.isEmpty()) {
            return
        }
        for (item in list) {
            if (!item.hasRead) {
                messageRead(item.id, 2)
            }
        }


    }


}
