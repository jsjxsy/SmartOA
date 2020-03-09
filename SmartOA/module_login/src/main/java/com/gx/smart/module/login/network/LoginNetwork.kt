package com.gx.smart.module.login.network

import com.gx.smart.lib.http.api.AuthApiService
import com.gx.smart.lib.http.api.UserCenterService
import com.gx.smart.lib.http.base.CallBack
import com.gx.wisestone.uaa.grpc.lib.auth.LoginResp
import com.gx.wisestone.uaa.grpc.lib.auth.VerifyCodeResp
import com.gx.wisestone.work.app.grpc.appuser.AppInfoResponse
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 *@author xiaosy
 *@create 2020-01-17
 *@Describe 网络请求模块
 **/
class LoginNetwork {


    suspend fun requestLogin(
        account: String,
        password: String,
        login_type: Int
    ) =
        suspendCoroutine<LoginResp> { continuation ->
            val loginCallBack = object : CallBack<LoginResp?>() {
                override fun callBack(result: LoginResp?) {
                    resultHandle(result, continuation as Continuation<Any>)
                }
            }
            AuthApiService.getInstance().login(account, password, login_type, loginCallBack)
        }


    suspend fun requestVerifyCode(phone: String, targetType: Int, purpose: Int) =
        suspendCoroutine<VerifyCodeResp> { continuation ->
            val verifyCallBack = object : CallBack<VerifyCodeResp?>() {
                override fun callBack(result: VerifyCodeResp?) {
                    resultHandle(result, continuation as Continuation<Any>)
                }
            }
            AuthApiService.getInstance().verifyCode(phone, targetType, purpose, verifyCallBack)
        }

    suspend fun requestBindAppUser(phone: String) =
        suspendCoroutine<AppInfoResponse> { continuation ->
            val bindCallBack = object : CallBack<VerifyCodeResp?>() {
                override fun callBack(result: VerifyCodeResp?) {
                    resultHandle(result, continuation as Continuation<Any>)
                }
            }
            UserCenterService.getInstance()
                .bindAppUser(phone, phone, bindCallBack)
        }

    private fun resultHandle(result: Any?, continuation: Continuation<Any>) {
        if (result == null) {
            continuation.resumeWithException(RuntimeException("网络请求超时"))
        } else {
            continuation.resume(result)
        }
    }

    companion object {

        private lateinit var instance: LoginNetwork

        fun getInstance(): LoginNetwork {
            if (!::instance.isInitialized) {
                synchronized(LoginNetwork::class.java) {
                    if (!::instance.isInitialized) {
                        instance = LoginNetwork()
                    }
                }
            }
            return instance
        }

    }
}