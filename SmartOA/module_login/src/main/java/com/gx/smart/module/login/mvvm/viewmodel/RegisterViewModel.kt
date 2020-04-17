package com.gx.smart.module.login.mvvm.viewmodel

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.lib.http.ApiConfig
import com.gx.smart.common.DataCheckUtil
import com.gx.smart.module.login.mvvm.repository.LoginRepository

open class RegisterViewModel(
    application: Application,
    private val loginRepository: LoginRepository
) :
    LoginViewModel(application, loginRepository) {
    open var confirmPassword = MutableLiveData<String>("")
    open var identityCode = MutableLiveData<String>("")

    init {
        targetType = 1
        purpose = 2
    }

    fun register() {
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
        isLoading.value = true
        launch({
            val result = loginRepository.regist(
                phone,
                password,
                phone,
                identityCode
            )
            val msg = result.dataMap["errMsg"]
            when (result.code) {
                100 -> {
                    ToastUtils.showLong("注册成功")
                    login(2)
                }
                308 -> {
                    result.userId
                }
                else -> {
                    ToastUtils.showLong(msg)
                    isLoading.value = false
                }
            }

        }, {
            ToastUtils.showLong("注册请求超时")
            isLoading.value = false
        })
    }

    override fun login(loginType: Int) {
        super.login(loginType)
    }

    fun gotoAgreementPage() {
        goWebView(ApiConfig.WEB_AGREEMENT_URL)
    }

    private fun goWebView(url: String) {
        ARouter.getInstance().build("/app/webview")
            .withString("URL", url)
            .navigation()
    }

}
