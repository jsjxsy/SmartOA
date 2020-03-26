package com.gx.smart.lib.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch

/**
 *@author xiaosy
 *@create 2020/3/8
 *@Describe 封装base ViewModel类
 **/
open class BaseViewModel : ViewModel() {
     fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) =
        viewModelScope.launch {
            try {
                Logger.d( "coroutine task start")
                block()
                Logger.d( "coroutine task end")
            } catch (e: Throwable) {
                Logger.e(e,"network request error")
                error(e)
            }
        }
}