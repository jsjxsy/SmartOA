package com.gx.smart.smartoa.activity.ui.login

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AuthApiService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask
import com.gx.smart.smartoa.utils.DataCheckUtil
import com.gx.wisestone.uaa.grpc.lib.auth.LoginResp
import com.gx.wisestone.uaa.grpc.lib.auth.VerifyCodeResp

class LoginViewModel : ViewModel() {
    var phone = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")
    var refreshing = MutableLiveData<Boolean>()

    private var verifyTask: GrpcAsyncTask<String, Void, VerifyCodeResp>? = null
    private var verifyCallBack: CallBack<VerifyCodeResp?>? = null
    //获取登录验证码
    private val targetType = 1
    private val purpose = 1

    fun setPhone() {
        phone.value = SPUtils.getInstance().getString(AppConfig.SH_USER_ACCOUNT, "")
    }

    fun login() {

    }

    fun getVerifyCode() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络连接不可用")
            return
        }
        if (TextUtils.isEmpty(phone.value)) {
            ToastUtils.showLong("手机号不能为空")
            return
        }
        if (phone.value?.length != 11 || !DataCheckUtil.isMobile(phone.value)) {
            ToastUtils.showLong("非法手机号")
            return
        }

        verifyCodeCallBack()
        if (GrpcAsyncTask.isFinish(verifyTask)) {
            verifyTask =
                AuthApiService.getInstance()
                    .verifyCode(phone.value, targetType, purpose, verifyCallBack)
        }


    }

    override fun toString(): String {
        return "phone $phone password $password"
    }




    /*******************************************获取验证码回调 */
    private fun verifyCodeCallBack() {
        verifyCallBack = object : CallBack<VerifyCodeResp?>() {
            override fun callBack(result: VerifyCodeResp?) {

                if (result == null) {
                    ToastUtils.showLong("验证码请求超时")
                    return
                }
                val msg = result.dataMap["errMsg"]
                if (result.code == 100) {
                    //mTime?.start()
                    ToastUtils.showLong("获取验证码成功")
                } else {
                    ToastUtils.showLong(msg)
                    //mLoadingView.visibility = View.GONE
                }
            }
        }
    }
}
