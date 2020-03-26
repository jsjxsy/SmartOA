package com.gx.smart.smartoa.activity.ui.setting

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gx.smart.lib.base.BaseFragment
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.login.LoginActivity
import com.gx.smart.common.AppConfig
import com.gx.smart.lib.http.api.UserCenterService
import com.gx.smart.lib.http.base.CallBack
import com.gx.smart.lib.http.base.GrpcAsyncTask
import com.gx.smart.lib.widget.LoadingView
import com.gx.wisestone.uaa.grpc.lib.auth.VerifyCodeResp
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import kotlinx.android.synthetic.main.fragment_account_unregister.*
import kotlinx.android.synthetic.main.layout_common_title.*
import top.limuyang2.customldialog.IOSMsgDialog


class AccountUnregisterFragment : BaseFragment(), View.OnClickListener {

    private var mTime: TimeCount? = null
    private lateinit var verifyCodeText: TextView
    private lateinit var mLoadingView: LoadingView
    private var verifyTask: GrpcAsyncTask<String, Void, VerifyCodeResp>? = null
    private var verifyCallBack: CallBack<VerifyCodeResp?>? = null


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
        val phoneNumber = SPUtils.getInstance().getString(AppConfig.SH_USER_ACCOUNT, "")
        phone.setText(phoneNumber)
        phone.setSelection(phoneNumber.length)
        verifyCodeText = getVerifyCodeText
        getVerifyCodeText.setOnClickListener(this)
        initData()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.cancel -> {
                tipAction()
            }
            R.id.getVerifyCodeText -> getVerifyCodeAction()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AccountUnregisterFragment()
    }


    private fun unRegister() {
        val verifyCode = verifyCode.text.trim()
        if (verifyCode.isEmpty()) {
            ToastUtils.showLong("验证码不能为空!")
            return
        }
        if (verifyCode.isNotEmpty()) {
            ToastUtils.showLong("验证码不正确!")
            return
        }

        loadingView.visibility = View.VISIBLE
        UserCenterService.getInstance().unRegister(object : CallBack<CommonResponse>() {
            override fun callBack(result: CommonResponse?) {
                if (!ActivityUtils.isActivityAlive(activity)) {
                    return
                }
                loadingView.visibility = View.GONE
                if (result == null) {
                    ToastUtils.showLong("注销账号超时!")
                    return
                }
                if (result.code == 100) {
                    clearCache()
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    ActivityUtils.startActivity(intent)
                    ToastUtils.showLong("注销账号成功")
                    activity?.finish()
                } else {
                    ToastUtils.showLong(result.msg)
                }
            }

        })
    }

    private fun tipAction() {
        IOSMsgDialog.init(fragmentManager!!)
            .setTitle("注销账号")
            .setMessage("确定要注销账号吗？")
            .setNegativeButton("取消", View.OnClickListener {

            })
            .setPositiveButton("确定", View.OnClickListener {
                unRegister()
            }).show()
    }


    fun clearCache() {
        SPUtils.getInstance()
            .remove(AppConfig.EMPLOYEE_ID)
        SPUtils.getInstance()
            .remove(AppConfig.COMPANY_SYS_TENANT_NO)
        SPUtils.getInstance()
            .remove(AppConfig.SMART_HOME_SN)
        SPUtils.getInstance()
            .remove(AppConfig.ROOM_ID)
        SPUtils.getInstance()
            .remove(AppConfig.COMPANY_PLACE_NAME)
        SPUtils.getInstance()
            .remove(AppConfig.COMPANY_NAME)
        SPUtils.getInstance()
            .remove(AppConfig.SH_USER_ACCOUNT)
        SPUtils.getInstance()
            .remove(AppConfig.SH_USER_REAL_NAME)
        SPUtils.getInstance()
            .remove(AppConfig.SH_PASSWORD)
        SPUtils.getInstance()
            .remove(AppConfig.LOGIN_TOKEN)
        SPUtils.getInstance()
            .remove(AppConfig.USER_ID)
    }


    private fun getVerifyCodeAction() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络连接不可用")
            return
        }
        mTime?.start()
//        val targetType = 1
//        val purpose = 3
//        getVerifyCode()
//        if (GrpcAsyncTask.isFinish(verifyTask)) {
//            verifyTask = AuthApiService.getInstance()
//                .verifyCode(mPhone, targetType, purpose, verifyCallBack)
//        }
    }

    private fun initData() {
        verifyCodeText = getVerifyCodeText
        mTime = TimeCount(60000, 1000, verifyCodeText)
    }

    override fun onDestroy() {
        super.onDestroy()
        mTime?.cancel()
    }

    //获取验证码定时器
    class TimeCount(
        millisInFuture: Long,
        countDownInterval: Long,
        private val verifyCodeText: TextView
    ) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            verifyCodeText.text = "获取验证码"
            verifyCodeText.isClickable = true
        }

        override fun onTick(millisUntilFinished: Long) {
            verifyCodeText.isClickable = false
            verifyCodeText.text = String.format(
                "%s",
                millisUntilFinished.div(1000).toString() + "s"
            )
        }
    }


    /*******************************************获取验证码回调 */
    private fun getVerifyCode() {
        verifyCallBack = object : CallBack<VerifyCodeResp?>() {
            override fun callBack(result: VerifyCodeResp?) {
                if (!ActivityUtils.isActivityAlive(activity)) {
                    return
                }
                if (result == null) {
                    ToastUtils.showLong("验证码请求超时")
                    return
                }
                val msg = result.dataMap["errMsg"]
                if (result.code == 100) {
                    mTime?.start()
                    ToastUtils.showLong("获取验证码成功")
                } else {
                    ToastUtils.showLong(msg)
                    mLoadingView.visibility = View.GONE
                    val userId = result.dataMap["userId"]
                    if (!TextUtils.isEmpty(userId)) {
                        val intent = Intent(activity, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        ActivityUtils.startActivity(intent)
                        activity?.finish()
                    }
                }
            }
        }
    }
}
