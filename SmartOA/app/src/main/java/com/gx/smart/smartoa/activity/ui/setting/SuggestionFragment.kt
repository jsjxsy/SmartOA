package com.gx.smart.smartoa.activity.ui.setting


import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.lib.http.api.UserCenterService
import com.gx.smart.lib.http.base.CallBack
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import kotlinx.android.synthetic.main.fragment_suggestion.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class SuggestionFragment : BaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggestion, container, false)
    }

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTitle()
        initContent()
    }


    override fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.suggestion)
        }
    }

    override fun initContent() {
        submit.setOnClickListener(this)
        suggestionEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val length = s?.length ?: 0
                if (length > 0) {
                    wordCount.text = "$length/140"
                }
                suggestionEdit.setSelection(length)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.submit -> applySuggestion()
        }
    }


    private fun applySuggestion() {
        val content = suggestionEdit.text.toString()
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showLong("提交的建议不能为空!")
        } else {
            loadingView.visibility = View.VISIBLE
            UserCenterService.getInstance()
                .addOpinion(content, object : CallBack<CommonResponse>() {
                    override fun callBack(result: CommonResponse?) {
                        if (!ActivityUtils.isActivityAlive(activity)) {
                            return
                        }
                        loadingView.visibility = View.GONE
                        if (result == null) {
                            ToastUtils.showLong("提交建议超时")
                            return
                        }

                        if (result.code == 100) {
                            ToastUtils.showLong("提交建议成功")
                            activity?.onBackPressed()
                        } else {
                            ToastUtils.showLong(result.msg)
                        }
                    }

                })
        }
    }


}
