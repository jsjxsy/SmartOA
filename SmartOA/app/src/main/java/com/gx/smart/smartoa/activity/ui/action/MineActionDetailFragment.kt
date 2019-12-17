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
import com.gx.wisestone.work.app.grpc.activity.AppActivityApplyResponse
import kotlinx.android.synthetic.main.fragment_mine_action_detail.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class MineActionDetailFragment : Fragment(), View.OnClickListener {

    private var title: String? = null
    private var time: String? = null
    private var content: String? = null
    private var activityId: Long? = null
    private var flag: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine_action_detail, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null && !arguments!!.isEmpty) {
            arguments?.let {
                title = it.getString(ARG_TITLE)
                time = it.getString(ARG_TIME)
                content = it.getString(ARG_CONTENT)
                activityId = it.getLong(ARG_ACTIVITY_ID, 0)
            }
        } else {
            activity?.intent?.let {
                title = it.getStringExtra(ARG_TITLE)
                time = it.getStringExtra(ARG_TIME)
                content = it.getStringExtra(ARG_CONTENT)
                activityId = it.getLongExtra(ARG_ACTIVITY_ID, 0)
            }
        }
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
        titleContent.text = title
        timeContent.text = time
        contentContent.text = content
        submit.setOnClickListener(this)
        findApplyInfo(activityId)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.submit -> {
                if (flag) {
                    cancelApply()
                } else {
                    apply()
                }

            }
            R.id.left_nav_image_view -> activity?.onBackPressed()

        }
    }


    private fun cancelApply() {
        if (activityId == null) {
            ToastUtils.showLong("活动为空!")
            return
        }
        AppActivityService.getInstance()
            .cancelApply(activityId!!, object : CallBack<AppActivityApplyResponse>() {
                override fun callBack(result: AppActivityApplyResponse?) {
                    if (result == null) {
                        ToastUtils.showLong("取消报名超时!")
                        return
                    }
                    if (result?.code == 100) {
                        ToastUtils.showLong("取消报名成功")
                        activity?.finish()
                    } else {
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
    }


    private fun apply() {
        if (activityId == null) {
            ToastUtils.showLong("活动为空!")
            return
        }
        val mark = commentTextContent.text.toString()
        AppActivityService.getInstance()
            .apply(activityId!!, mark, object : CallBack<AppActivityApplyResponse>() {
                override fun callBack(result: AppActivityApplyResponse?) {
                    if (result == null) {
                        ToastUtils.showLong("报名超时!")
                        return
                    }
                    if (result?.code == 100) {
                        ToastUtils.showLong("申请报名成功")
                        activity?.finish()
                    } else {
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
    }


    private fun findApplyInfo(activityId: Long?) {
        if (activityId == null) {
            ToastUtils.showLong("活动为空!")
            return
        }
        AppActivityService.getInstance()
            .findApplyInfo(activityId, object : CallBack<AppActivityApplyResponse>() {
                override fun callBack(result: AppActivityApplyResponse?) {
                    if (result == null) {
                        ToastUtils.showLong("报名超时!")
                        return
                    }
                    if (result?.code == 100) {
                        flag = result.contentOrBuilderList.size > 0
                        submit?.let {
                            if (flag) {
                                it.text = getString(R.string.cancel_action)
                                commentTextContent.setText(result.contentOrBuilderList[0].mark)
                                commentTextContent.isEnabled = false
                                it.setBackgroundResource(R.color.background_style_eight)
                            } else {
                                it.text = getString(R.string.apply_action)
                                commentText.isEnabled = true
                                it.setBackgroundResource(R.color.background_style_two)
                            }
                        }
                    } else {
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
    }

    companion object {
        const val ARG_TITLE = "title"
        const val ARG_TIME = "time"
        const val ARG_CONTENT = "content"
        const val ARG_ACTIVITY_ID = "activity_id"
    }
}
