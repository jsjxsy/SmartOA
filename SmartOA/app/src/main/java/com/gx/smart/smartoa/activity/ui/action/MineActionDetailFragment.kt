package com.gx.smart.smartoa.activity.ui.action


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.api.AppActivityService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.activity.ActivityCommonResponse
import com.gx.wisestone.work.app.grpc.activity.AppActivityApplyResponse
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.list_action_layout.*

/**
 * A simple [Fragment] subclass.
 */
class MineActionDetailFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine_action_detail, container, false)
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
            it.text = getString(R.string.action_detail)
        }
    }

    private fun initContent() {
        val adapter = ActionAdapter()
        adapter.mList = arrayListOf(Action("", ""))
        recyclerView.adapter = adapter
    }

    override fun onClick(v: View?) {

    }


    private fun cancelApply() {
        AppActivityService.getInstance()
            .cancelApply(object : CallBack<ActivityCommonResponse>() {
                override fun callBack(result: ActivityCommonResponse?) {
                    if (result == null) {
                        ToastUtils.showLong("取消报名超时!")
                        return
                    }
                    if (result?.code == 100) {
                    }
                }

            })
    }


    private fun apply() {
        AppActivityService.getInstance()
            .apply(object : CallBack<AppActivityApplyResponse>() {
                override fun callBack(result: AppActivityApplyResponse?) {
                    if (result == null) {
                        ToastUtils.showLong("报名超时!")
                        return
                    }
                    if (result?.code == 100) {

                    }
                }

            })
    }
}
