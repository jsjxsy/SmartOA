package com.gx.smart.smartoa.activity.ui.setting


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.api.UserCenterService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import kotlinx.android.synthetic.main.fragment_suggestion.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class SuggestionFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggestion, container, false)
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
            it.text = getString(R.string.suggestion)
        }
    }

    private fun initContent() {
        submit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.submit -> applySuggestion()
        }
    }


    private fun applySuggestion() {
        val content = suggestionEdit.text.toString()
        if(TextUtils.isEmpty(content)){
            ToastUtils.showLong("提交的建议不能为空!")
        }else{
            loadingView.visibility = View.VISIBLE
            UserCenterService.getInstance().addOpinion(content, object: CallBack<CommonResponse>() {
                override fun callBack(result: CommonResponse?) {
                    loadingView.visibility = View.GONE
                    if(result == null) {
                        ToastUtils.showLong("提交建议超时")
                        return
                    }

                    if(result?.code == 100) {
                        ToastUtils.showLong("提交建议成功")
                        activity?.onBackPressed()
                    }else{
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
        }
    }


}
