package com.gx.smart.smartoa.data.db

import com.gx.smart.module.login.db.LoginDao

/**
 *@author xiaosy
 *@create 2020-01-17
 *@Describe 数据库存储
 **/
object LoginDatabase {
    private var loginDao: LoginDao? = null

    fun getLoginDao(): LoginDao {
        if (loginDao == null) {
            loginDao = LoginDao()
        }
        return loginDao!!
    }
}