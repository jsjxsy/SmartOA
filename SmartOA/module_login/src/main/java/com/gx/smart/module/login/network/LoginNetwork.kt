package com.gx.smart.module.login.network

import com.gx.smart.lib.http.api.AppEmployeeService
import com.gx.smart.lib.http.api.AppMessagePushService
import com.gx.smart.lib.http.api.AuthApiService
import com.gx.smart.lib.http.api.UserCenterService
import com.gx.smart.lib.http.base.CallBack
import com.gx.wisestone.uaa.grpc.lib.auth.LoginResp
import com.gx.wisestone.uaa.grpc.lib.auth.RegistResp
import com.gx.wisestone.uaa.grpc.lib.auth.UserModifyResp
import com.gx.wisestone.uaa.grpc.lib.auth.VerifyCodeResp
import com.gx.wisestone.work.app.grpc.appuser.AppInfoResponse
import com.gx.wisestone.work.app.grpc.employee.AppMyCompanyResponse
import com.gx.wisestone.work.app.grpc.push.UpdateMessagePushResponse
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

    @Suppress("UNCHECKED_CAST")
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


    @Suppress("UNCHECKED_CAST")
    suspend fun requestVerifyCode(phone: String, targetType: Int, purpose: Int) =
        suspendCoroutine<VerifyCodeResp> { continuation ->
            val verifyCallBack = object : CallBack<VerifyCodeResp?>() {
                override fun callBack(result: VerifyCodeResp?) {
                    resultHandle(result, continuation as Continuation<Any>)
                }
            }
            AuthApiService.getInstance().verifyCode(phone, targetType, purpose, verifyCallBack)
        }
    @Suppress("UNCHECKED_CAST")
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
    @Suppress("UNCHECKED_CAST")
    suspend fun regist(
        account: String,
        password: String,
        mobile: String,
        mobile_verify_code: String
    ) = suspendCoroutine<RegistResp> { continuation ->
        val callBack = object : CallBack<RegistResp?>() {
            override fun callBack(result: RegistResp?) {
                resultHandle(result, continuation as Continuation<Any>)
            }
        }
        AuthApiService.getInstance()
            .regist(account, password, mobile, mobile_verify_code, callBack)
    }
    @Suppress("UNCHECKED_CAST")
    suspend fun updateMessagePush(JGToken: String) =
        suspendCoroutine<UpdateMessagePushResponse> { continuation ->
            val callBack = object : CallBack<UpdateMessagePushResponse?>() {
                override fun callBack(result: UpdateMessagePushResponse?) {
                    resultHandle(result, continuation as Continuation<Any>)
                }
            }

            AppMessagePushService.getInstance().updateMessagePush(JGToken, callBack);
        }
    @Suppress("UNCHECKED_CAST")
    suspend fun myCompany() = suspendCoroutine<AppMyCompanyResponse> { continuation ->
        val callBack = object : CallBack<AppMyCompanyResponse?>() {
            override fun callBack(result: AppMyCompanyResponse?) {
                resultHandle(result, continuation as Continuation<Any>)
            }
        }
        AppEmployeeService.getInstance().myCompany(callBack)
    }
    @Suppress("UNCHECKED_CAST")
    suspend fun userModifyPassWord(
        password: String,
        userId: String,
        token: String
    ) = suspendCoroutine<UserModifyResp> { continuation ->
        val callBack = object : CallBack<UserModifyResp?>() {
            override fun callBack(result: UserModifyResp?) {
                resultHandle(result, continuation as Continuation<Any>)
            }
        }
        AuthApiService.getInstance()
            .userModifyPassWord(password, userId, token, callBack)
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