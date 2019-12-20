package com.gx.smart.smartoa.activity.ui.repair

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.api.AppRepairService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.repair.RepairCommonResponse
import kotlinx.android.synthetic.main.fragment_repair_record_list.*
import kotlinx.android.synthetic.main.layout_common_title.*

class RepairRecordFragment : Fragment(), View.OnClickListener {

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
        adapter = RepairRecordAdapter()
        recyclerView.adapter = adapter
        queryMyRepair()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }


    private fun queryMyRepair() {
        AppRepairService.getInstance()
            .queryMyRepair(
                object : CallBack<RepairCommonResponse>() {
                    override fun callBack(result: RepairCommonResponse?) {
                        if (result == null) {
                            ToastUtils.showLong("查询超时!")
                            return
                        }
                        if (result?.code == 100) {
                            val list = result.repairInfoOrBuilderList.toList()
                            if (list.isEmpty()) {
                                emptyLayout.visibility = View.VISIBLE
                            } else {
                                emptyLayout.visibility = View.GONE
                                adapter.mList = list
                                adapter.notifyDataSetChanged()
                            }
                        } else {
                            ToastUtils.showLong(result.msg)
                        }
                    }

                })
    }


}
