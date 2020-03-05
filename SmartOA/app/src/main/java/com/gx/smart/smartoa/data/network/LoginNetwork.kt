package com.gx.smart.smartoa.data.network

import com.gx.smart.lib.http.api.AuthApiService
import com.gx.smart.lib.http.base.CallBack
import com.gx.wisestone.uaa.grpc.lib.auth.LoginResp
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 *@author xiaosy
 *@create 2020-01-17
 *@Describe
 **/
class LoginNetwork {


    suspend fun requestLogin(
        account: String,
        password: String,
        login_type: Int
    ) = suspendCoroutine<LoginResp> { continuation ->
        val loginCallBack = object : CallBack<LoginResp?>() {
            override fun callBack(result: LoginResp?) {
                    if (result == null) {
                        continuation.resumeWithException(RuntimeException("网络请求超时"))
                    } else {
                        continuation.resume(result)
                    }
            }
        }
        AuthApiService.getInstance().login(account, password, login_type, loginCallBack)
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