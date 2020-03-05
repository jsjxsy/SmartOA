package com.gx.smart.repair.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.lib.http.base.CallBack
import com.gx.smart.repair.R
import com.gx.smart.repair.adapter.RepairRecordAdapter
import com.gx.smart.repair.network.AppRepairService
import com.gx.wisestone.core.grpc.lib.common.QueryDto
import com.gx.wisestone.work.app.grpc.repair.RepairCommonResponse
import kotlinx.android.synthetic.main.fragment_repair_record_list.*
import kotlinx.android.synthetic.main.layout_common_title.*

class RepairRecordFragment : Fragment(), View.OnClickListener {

    private var currentPage: Int = 0
    private lateinit var adapter: RepairRecordAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_repair_record_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTitle()
        initContent()
    }


    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.repair_record)
        }
    }

    private fun initContent() {
        val divider = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(divider)
        adapter = RepairRecordAdapter()
        recyclerView.adapter = adapter
        refreshLayout.setOnRefreshListener {
            currentPage = 0
            adapter.clear()
            val query = QueryDto
                .newBuilder()
                .setPageSize(10)
                .setPage(currentPage)
                .build()
            queryMyRepair(query)
        }
        refreshLayout.setOnLoadmoreListener {
            val query = QueryDto
                .newBuilder()
                .setPageSize(10)
                .setPage(currentPage)
                .build()
            queryMyRepair(query)
        }
        refreshLayout.autoRefresh()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }


    private fun queryMyRepair(query: QueryDto) {
        AppRepairService.getInstance()
            .queryMyRepair(
                query, object : CallBack<RepairCommonResponse>() {
                    override fun callBack(result: RepairCommonResponse?) {
                        if (!ActivityUtils.isActivityAlive(activity)) {
                            return
                        }
                        if (currentPage == 0) {
                            refreshLayout.finishRefresh()
                        } else {
                            refreshLayout.finishLoadmore()
                        }
                        if (result == null) {
                            ToastUtils.showLong("查询超时!")
                            return
                        }
                        if (result?.code == 100) {
                            val list = result.repairInfoOrBuilderList.toList()
                            if (list.isEmpty() && currentPage == 0) {
                                emptyLayout.visibility = View.VISIBLE
                            } else {
                                emptyLayout.visibility = View.GONE
                                if (list.isNotEmpty()) {
                                    currentPage++
                                    adapter.addList(list)
                                    adapter.notifyDataSetChanged()
                                } else {
                                    refreshLayout.isLoadmoreFinished = true
                                }

                            }
                        } else {
                            ToastUtils.showLong(result.msg)
                        }
                    }

                })
    }


}
