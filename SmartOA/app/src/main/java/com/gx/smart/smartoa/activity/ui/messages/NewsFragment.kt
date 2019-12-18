package com.gx.smart.smartoa.activity.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.api.AppInformationService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.information.AppInformationResponse
import kotlinx.android.synthetic.main.news_fragment.*

class NewsFragment : Fragment() {
    private var mViewModel: NewsViewModel? = null
    private lateinit var adapter: NewsAdapter

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
                val args = Bundle()
                args.putString(DetailFragment.ARG_TITLE, item.title)
                args.putString(DetailFragment.ARG_CONTENT, item.content)
                Navigation.findNavController(view!!).navigate(R.id.action_newsFragment_to_detailFragment)
            }

        }
        adapter.setOnItemClick(onItemClick)
        recyclerView.adapter = adapter
        getInformation()
    }

    private fun getInformation() {
        AppInformationService.getInstance()
            .getInformation(object : CallBack<AppInformationResponse>() {
                override fun callBack(result: AppInformationResponse?) {
                    if (result == null) {
                        ToastUtils.showLong("获取消息超时!")
                        return
                    }
                    if (result?.code == 100) {
                        val appInformationNoticeRecordDtoList =
                            result.appInformationNoticeRecordDtoOrBuilderList.toList()
                        if(appInformationNoticeRecordDtoList.isEmpty()){
                        }
                        adapter.setList(appInformationNoticeRecordDtoList)
                        adapter.notifyDataSetChanged()
                    } else {
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
    }
}