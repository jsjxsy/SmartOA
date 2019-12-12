package com.gx.smart.smartoa.activity.ui.action


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.api.AppActivityService
import com.gx.smart.smartoa.data.network.api.base.CallBack
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
                args.putString(MineActionDetailFragment.ARG_TIME, "${item.startTime - item.endTime}")
                args.putString(MineActionDetailFragment.ARG_CONTENT, item.content)
                args.putString(MineActionDetailFragment.ARG_COMMENT, item.content)
                findNavController().navigate(R.id.action_newsFragment_to_detailFragment)
            }

        }
        adapter.onItemClick = onItemClick
        recyclerView.adapter = adapter
        findAllApplyInfos()
    }

    override fun onClick(v: View?) {

    }


    private fun findAllApplyInfos() {
        AppActivityService.getInstance()
            .findAllApplyInfos(object : CallBack<ActivityCommonResponse>() {
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
