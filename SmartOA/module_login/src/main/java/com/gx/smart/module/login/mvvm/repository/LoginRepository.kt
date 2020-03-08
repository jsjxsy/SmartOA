package com.gx.smart.module.login.mvvm.repository

import com.gx.smart.module.login.db.LoginDao
import com.gx.smart.module.login.network.LoginNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *@author xiaosy
 *@create 2020-01-17
 *@Describe 登陆模块数据仓储（包含：database和sharePreference和network）
 **/
class LoginRepository private constructor(private val loginDao: LoginDao, private val network: LoginNetwork) {

    suspend fun login(account: String,
                      password: String,
                      login_type: Int) = withContext(Dispatchers.IO) {
        network.requestLogin(account, password,login_type)
    }

    companion object {

        private lateinit var instance: LoginRepository

        fun getInstance(loginDao: LoginDao, network: LoginNetwork): LoginRepository {
            if (!Companion::instance.isInitialized) {
                synchronized(LoginRepository::class.java) {
                    if (!Companion::instance.isInitialized) {
                        instance =
                            LoginRepository(
                                loginDao,
                                network
                            )
                    }
                }
            }
            return instance
        }

    }
}