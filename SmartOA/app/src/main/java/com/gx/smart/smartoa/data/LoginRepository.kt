package com.gx.smart.smartoa.data

import com.gx.smart.smartoa.data.db.LoginDao
import com.gx.smart.smartoa.data.network.LoginNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *@author xiaosy
 *@create 2020-01-17
 *@Describe
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
            if (!::instance.isInitialized) {
                synchronized(LoginRepository::class.java) {
                    if (!::instance.isInitialized) {
                        instance = LoginRepository(loginDao, network)
                    }
                }
            }
            return instance
        }

    }
}