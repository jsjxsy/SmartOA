package com.gx.smart.smartoa.activity.ui.login.register

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.common.AppConfig
import com.gx.smart.lib.http.api.AuthApiService
import com.gx.smart.lib.http.api.UserCenterService
import com.gx.smart.lib.http.base.CallBack
import com.gx.smart.lib.http.base.GrpcAsyncTask
import com.gx.smart.smartoa.utils.DataCheckUtil
import com.gx.wisestone.uaa.grpc.lib.auth.LoginResp
import com.gx.wisestone.uaa.grpc.lib.auth.RegistResp
import com.gx.wisestone.work.app.grpc.appuser.AppInfoResponse

class RegisterViewModel : ViewModel() {
    var phone = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")
    var confirmPassword = MutableLiveData<String>("")
    var identityCode = MutableLiveData<String>("")

    var isLoading = MutableLiveData<Boolean>()

    private var registerBack: CallBack<RegistResp?>? = null
    private var authTask: GrpcAsyncTask<String, Void, RegistResp>? = null

    private var loginTask: GrpcAsyncTask<String, Void, LoginResp>? = null
    private var loginCallBack: CallBack<LoginResp?>? = null

    private var bindTask: GrpcAsyncTask<String, Void, AppInfoResponse>? = null
    private var bindCallBack: CallBack<AppInfoResponse?>? = null


    private fun register() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络连接不可用")
            return
        }
        val phone = phone.value.toString().trim()
        val password: String = password.value!!.trim()
        val confirmPassword: String = confirmPassword.value.toString().trim()
        val identityCode: String = identityCode.value.toString().trim()

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showLong("手机号不能为空")
            return
        }

        if (phone.length != 11 || !DataCheckUtil.isMobile(phone)) {
            ToastUtils.showLong("非法手机号")
            return
        }

        if (TextUtils.isEmpty(identityCode)) {
            ToastUtils.showLong("验证码不能为空")
            return
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtils.showLong("密码不能为空")
            return
        }
        if (password.length < 6 || password.length > 16) {
            ToastUtils.showLong("密码长度不得小于6位大于16位")
            return
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            ToastUtils.showLong("确认密码不能为空")
            return
        }

        if (password != confirmPassword) {
            ToastUtils.showLong("确认密码与密码不一致")
            return
        }

//        {
//            mLoadingView.visibility = View.VISIBLE
//            mLoadingView.setOnClickListener(null)
//            mLoadingView.setText("注册中")
//            mLoadingView.showLoading()
//            mLoadingView.postDelayed(
//                { mLoadingView.visibility = View.GONE },
//                SplashActivity.DELAY_TIME
//            )
//            passWord = password
//            appUserRegisterCallBack()
//            if (GrpcAsyncTask.isFinish(authTask)) {
//                authTask = AuthApiService.getInstance()
//                    .regist(phone, password, phone, identityCode, registerBack)
//            }
//        }

        appUserRegisterCallBack()
        if (GrpcAsyncTask.isFinish(authTask)) {
            authTask = AuthApiService.getInstance()
                .regist(
                    phone,
                    password,
                    phone,
                    identityCode,
                    registerBack
                )
        }
    }

    // TODO: Implement the ViewModel
    /*******************************************注册回调 */
    private fun appUserRegisterCallBack() {
        registerBack = object : CallBack<RegistResp?>() {
            override fun callBack(result: RegistResp?) {
                if (result == null) {
                    ToastUtils.showLong("注册请求超时")
                    isLoading.value = false
                    return
                }
                val msg = result.dataMap["errMsg"]
                if (result.code == 100) {
                    ToastUtils.showLong("注册成功")
                    val loginType = 2
                    authLoginCallBack()
                    if (GrpcAsyncTask.isFinish(loginTask)) {
                        loginTask = AuthApiService.getInstance()
                            .login(phone.value, password.value, loginType, loginCallBack)
                    }
                } else if (result.code == 308) {
                    result.userId
                } else {
                    ToastUtils.showLong(msg)
                    isLoading.value = false
                }
                //处理数据,刷新UI
            }

        }
    }

    /*******************************************登录回调 */
    fun authLoginCallBack() {
        loginCallBack = object : CallBack<LoginResp?>() {
            override fun callBack(result: LoginResp?) {
                if (result == null) {
//                    mLoadingView.visibility = View.GONE
                    ToastUtils.showLong("注册后登陆超时")
                    return
                }
                val msg = result.dataMap["errMsg"]
                if (result.code == 100) {
                    SPUtils.getInstance().put(AppConfig.LOGIN_TOKEN, result.token)
                    //保存当前用户
                    SPUtils.getInstance().put(AppConfig.SH_USER_ACCOUNT, phone.value)
                    SPUtils.getInstance().put(AppConfig.SH_PASSWORD, password.value)
                    bindAppCallBack()
                    if (GrpcAsyncTask.isFinish(bindTask)) {
                        bindTask =
                            UserCenterService.getInstance()
                                .bindAppUser(phone.value, phone.value, bindCallBack)
                    }
                } else {
                    ToastUtils.showLong(msg)
//                    mLoadingView.visibility = View.GONE
                }
                //处理数据,刷新UI
            }
        }
    }

    /*******************************************绑定回调 */
    fun bindAppCallBack() {
        bindCallBack = object : CallBack<AppInfoResponse?>() {
            override fun callBack(result: AppInfoResponse?) {
//                if (!ActivityUtils.isActivityAlive(activity)) {
//                    return
//                }
                if (result == null) {
                    ToastUtils.showLong("登录后绑定超时")
//                    mLoadingView.visibility = View.GONE
                    return
                }

                when {
                    result.code == 100 -> {
                        SPUtils.getInstance().put(AppConfig.USER_ID, result.appUserInfoDto.userId)
//                        mLoadingView.visibility = View.GONE
//                        gotoLoginPage()
                        //用户已经绑定
                    }
                    result.code == 7003 -> {
                        SPUtils.getInstance().put(AppConfig.USER_ID, result.appUserInfoDto.userId)
//                        mLoadingView.visibility = View.GONE
//                        gotoLoginPage()
                    }
                    else -> {
                        ToastUtils.showLong(result.msg)
//                        mLoadingView.visibility = View.GONE
                    }
                }
                //处理数据,刷新UI
            }
        }
    }

}
