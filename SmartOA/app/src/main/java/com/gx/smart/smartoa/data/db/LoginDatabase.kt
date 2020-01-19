package com.gx.smart.smartoa.data.db

/**
 *@author xiaosy
 *@create 2020-01-17
 *@Describe
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