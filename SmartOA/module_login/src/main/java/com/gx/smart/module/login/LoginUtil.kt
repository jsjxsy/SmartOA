package com.gx.smart.module.login

import com.gx.smart.module.login.mvvm.factory.LoginModelFactory
import com.gx.smart.module.login.mvvm.repository.LoginRepository
import com.gx.smart.module.login.network.LoginNetwork
import com.gx.smart.smartoa.data.db.LoginDatabase

/**
 *@author xiaosy
 *@create 2020-01-17
 *@Describe 管理多个viewModel
 **/
object LoginUtil {
    private fun getLoginRepository() =
        LoginRepository.getInstance(LoginDatabase.getLoginDao(), LoginNetwork.getInstance())

    fun getLoginFactory() = LoginModelFactory(getLoginRepository())
}