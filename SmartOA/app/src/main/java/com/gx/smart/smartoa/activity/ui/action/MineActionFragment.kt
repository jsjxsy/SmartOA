package com.gx.smart.smartoa.activity.ui.action


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AppActivityService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.core.grpc.lib.common.QueryDto
import com.gx.wisestone.work.app.grpc.activity.ActivityCommonResponse
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.list_action_layout.*

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
        adapter = ActionAdapter()
        val onItemClick = object : ActionAdapter.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                val item = adapter.mList!![position]
                val args = Bundle()
                args.putString(MineActionDetailFragment.ARG_TITLE, item.title)
                args.putString(
                    MineActionDetailFragment.ARG_TIME,
                    "${item.startTime - item.endTime}"
                )
                args.putString(MineActionDetailFragment.ARG_CONTENT, item.content)
                args.putString(MineActionDetailFragment.ARG_COMMENT, item.content)
                args.putLong(MineActionDetailFragment.ARG_ACTIVITY_ID, item.id)
                findNavController().navigate(R.id.action_newsFragment_to_detailFragment)
            }

        }
        adapter.onItemClick = onItemClick
        recyclerView.adapter = adapter
        if (flag){
            findMyApplyInfos()
        }else{
            findAllApplyInfos()
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }


    }


    private fun findMyApplyInfos() {
        val employeeId = SPUtils.getInstance().getLong(AppConfig.EMPLOYEE_ID, 0)
        if (employeeId.equals(0)) {
            ToastUtils.showLong("企业申请还没通过!")
            return
        }
        AppConfig.employeeId = employeeId
        val query = QueryDto.newBuilder()
            .setPage(1)
            .setPageSize(10)
            .build()
        AppActivityService.getInstance()
            .findMyApplyInfos(query, object : CallBack<ActivityCommonResponse>() {
                override fun callBack(result: ActivityCommonResponse?) {
                    if (result == null) {
                        ToastUtils.showLong("查询活动超时!")
                        return
                    }
                    if (result?.code == 100) {
                        adapter.mList = result.contentList
                    } else {
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
    }


    private fun findAllApplyInfos() {
        val query = QueryDto.newBuilder()
            .setPage(1)
            .setPageSize(10)
            .build()
        AppActivityService.getInstance()
            .findAllApplyInfos(query, object : CallBack<ActivityCommonResponse>() {
                override fun callBack(result: ActivityCommonResponse?) {
                    if (result == null) {
                        ToastUtils.showLong("查询活动超时!")
                        return
                    }
                    if (result?.code == 100) {
                        adapter.mList = result.contentList

                    } else {
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
    }


}
