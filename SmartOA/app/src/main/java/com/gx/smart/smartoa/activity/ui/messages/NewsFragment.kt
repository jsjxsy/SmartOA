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
import com.gx.smart.smartoa.data.network.api.AppInformationService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.core.grpc.lib.common.QueryDto
import com.gx.wisestone.work.app.grpc.information.AppInformationResponse
import com.gx.wisestone.work.app.grpc.information.MessageReadResponse
import kotlinx.android.synthetic.main.news_fragment.*

class NewsFragment : Fragment() {
    private var mViewModel: NewsViewModel? = null
    private lateinit var adapter: NewsAdapter
    private var currentPage = 0

    companion object {
        fun newInstance() = NewsFragment()
        const val NOTICE_NEWS = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.news_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)
        initContent()
    }

    private fun initContent() {
        adapter = NewsAdapter()
        val onItemClick = object : NewsAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                val list = adapter.getList()
                val item = list!![position]
                if (!item.hasRead) {
                    messageRead(item.id, 1)
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
            getInformation(query)
        }
        refreshLayout.setOnLoadmoreListener {
            val query = QueryDto
                .newBuilder()
                .setPageSize(10)
                .setPage(currentPage)
                .build()
            getInformation(query)
        }
        refreshLayout.autoRefresh()
    }

    private fun getInformation(query: QueryDto) {
        AppInformationService.getInstance()
            .getInformation(query, object : CallBack<AppInformationResponse>() {
                override fun callBack(result: AppInformationResponse?) {
                    if(currentPage == 0) {
                        refreshLayout.finishRefresh()
                    }else{
                        refreshLayout.finishLoadmore()
                    }

                    if (result == null) {
                        ToastUtils.showLong("获取消息超时!")
                        return
                    }
                    if (result?.code == 100) {
                        val appInformationNoticeRecordDtoList =
                            result.appInformationNoticeRecordDtoOrBuilderList.toList()
                        if (appInformationNoticeRecordDtoList.isEmpty() && currentPage == 0) {
                            emptyLayout.visibility = View.VISIBLE
                        } else {
                            emptyLayout.visibility = View.GONE
                            if(appInformationNoticeRecordDtoList.isNotEmpty()) {
                                currentPage ++
                                adapter.addList(appInformationNoticeRecordDtoList)
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
        val list = adapter.getList()
        if (list == null || list.isEmpty()) {
            return
        }
        for (item in list) {
            if (!item.hasRead) {
                messageRead(item.id, 1)
            }
        }

        refreshLayout.autoRefresh(1000 * 2)
    }
}