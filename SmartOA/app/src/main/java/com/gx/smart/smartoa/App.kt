package com.gx.smart.smartoa

import android.app.Application
import com.blankj.utilcode.util.Utils

/**
 *@author xiaosy
 *@create 2019-10-30
 *@Describe
 **/
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }
}