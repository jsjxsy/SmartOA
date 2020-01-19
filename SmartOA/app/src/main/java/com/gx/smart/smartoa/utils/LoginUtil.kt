package com.gx.smart.smartoa.utils

import com.gx.smart.smartoa.data.LoginModelFactory
import com.gx.smart.smartoa.data.LoginRepository
import com.gx.smart.smartoa.data.db.LoginDatabase
import com.gx.smart.smartoa.data.network.LoginNetwork

/**
 *@author xiaosy
 *@create 2020-01-17
 *@Describe
 **/
object LoginUtil {
    private fun getLoginRepository() =
        LoginRepository.getInstance(LoginDatabase.getLoginDao(), LoginNetwork.getInstance())

    fun getLoginFactory() = LoginModelFactory(getLoginRepository())
}