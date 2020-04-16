package com.gx.smart.module.login.mvvm.viewmodel

import android.app.Application
import android.text.TextUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.common.AppConfig
import com.gx.smart.common.DataCheckUtil
import com.gx.smart.lib.http.lib.utils.AuthUtils
import com.gx.smart.module.login.mvvm.repository.LoginRepository

open class ForgetPasswordViewModel(application: Application, private val loginRepository: LoginRepository) :
    RegisterViewModel(application, loginRepository) {
    init {
        var targetType = 1
        var purpose = 1
    }

    /**
     * 点击修改密码
     */
    fun modify() {
        val identityCode: String? = identityCode.value.toString().trim()
        val password = password.value.toString().trim()
        val confirmPassword: String = confirmPassword.value.toString().trim()
        val phone = phone.value.toString().trim()
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

        if (password.length < 8) {
            ToastUtils.showLong("密码长度不得小于8位")
            return
        }
        if (password.length > 16) {
            ToastUtils.showLong("密码长度不得大于16位")
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

        //手机号验证码登录
        val loginType = 3
        login(loginType)
    }

    /**
     * 登陆 网络请求
     * @param loginType 2:手机号密码登录 3: 手机号验证码登录
     */
    override fun login(loginType: Int) {
        launch({
            val result = loginRepository.login(phone.value!!, password.value!!, loginType)
            val msg = result.dataMap["errMsg"]
            if (result.code == 100) {
                SPUtils.getInstance().put(AppConfig.LOGIN_TOKEN, result.token)
                //保存当前用户
                val isPassWord = loginType == 2
                if (isPassWord) {
                    SPUtils.getInstance().put(AppConfig.SH_USER_ACCOUNT, phone.value)
                    SPUtils.getInstance().put(AppConfig.SH_PASSWORD, password.value)
                } else {
                    SPUtils.getInstance().put(AppConfig.SH_USER_ACCOUNT, phone.value)
                }
                modifyPassword()
            } else {
                isLoading.value = false
                ToastUtils.showLong(msg)
            }
        }
            , {
                ToastUtils.showLong("登录超时")
            })

    }


    private fun modifyPassword() {
        val token = SPUtils.getInstance().getString(AppConfig.LOGIN_TOKEN)
        val holder = AuthUtils.parseJwtHolder(token)
        val userId = holder.subject
        launch({
            val result = loginRepository.userModifyPassWord(password.value!!, userId, token)
            val msg = result.dataMap["errMsg"]
            if (result.code == 100) {
                ToastUtils.showLong("修改密码成功")
//                activity?.finish()
//                val intent = Intent(activity, LoginActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                ActivityUtils.startActivity(intent)
            } else {
                ToastUtils.showLong(msg)
            }
        },{
            ToastUtils.showLong("修改密码超时")
        })

    }

}
