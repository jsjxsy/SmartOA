package com.gx.smart.smartoa.activity.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.api.UserCenterService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import kotlinx.android.synthetic.main.fragment_account_unregister.*
import kotlinx.android.synthetic.main.layout_common_title.*


class AccountUnregisterFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_unregister, container, false)
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
            it.text = getString(R.string.account_unregister)
        }
    }

    private fun initContent() {
        cancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.cancel -> {
                unRegister()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AccountUnregisterFragment()
    }


    private fun unRegister() {
        loadingView.visibility = View.VISIBLE
        UserCenterService.getInstance().unRegister(object : CallBack<CommonResponse>() {
            override fun callBack(result: CommonResponse?) {
                loadingView.visibility = View.GONE
                if (result == null) {
                    ToastUtils.showLong("注销账号超时!")
                    return
                }
                if (result?.code == 100) {
                    ToastUtils.showLong("注销账号成功")
                } else {
                    ToastUtils.showLong(result.msg)
                }
            }

        })
    }
}
