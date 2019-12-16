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
import kotlinx.android.synthetic.main.fragment_mine_action_detail.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class MineActionDetailFragment : Fragment(), View.OnClickListener {

    private var title: String? = null
    private var time: String? = null
    private var content: String? = null
    private var comment: String? = null
    private var activityId: Long? = null
    private var flag: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            time = it.getString(ARG_TIME)
            content = it.getString(ARG_CONTENT)
            comment = it.getString(ARG_COMMENT)
            activityId = it.getLong(ARG_ACTIVITY_ID)
        }
    }

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
        titleContent.text = title
        timeContent.text = time
        contentContent.text = content
        commentText.text = comment
        submit.setOnClickListener(this)
        findApplyInfo(activityId!!)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.submit -> {
                if (flag) {
                    apply()
                } else {
                    cancelApply()
                }

            }
            R.id.left_nav_image_view -> activity?.onBackPressed()

        }
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
                        ToastUtils.showLong("取消报名成功")
                        activity?.finish()
                    } else {
                        ToastUtils.showLong(result.msg)
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
                        ToastUtils.showLong("申请报名成功")
                        activity?.finish()
                    } else {
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
    }


    private fun findApplyInfo(activityId: Long) {
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
                                it.text = getString(R.string.apply_action)
                                it.setBackgroundResource(R.color.background_style_two)
                            } else {
                                it.text = getString(R.string.cancel_action)
                                it.setBackgroundResource(R.color.background_style_eight)
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
        const val ARG_COMMENT = "comment"
        const val ARG_ACTIVITY_ID = "activity_id"
    }
}
