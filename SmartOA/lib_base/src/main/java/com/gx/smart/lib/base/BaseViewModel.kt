package com.gx.smart.lib.base

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.common.ApiConfig
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch

/**
 *@author xiaosy
 *@create 2020/3/8
 *@Describe 封装base ViewModel类
 **/
open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    //请求错误
    var error: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    init {
        error.value = false
    }


    /**
     * 统一处理服务器异常
     */
    open fun handleServiceException(msg: String) {
        if (TextUtils.isEmpty(msg)) {
            ToastUtils.showLong(ApiConfig.SERVER_ERROR_MESSAGE)
        } else {
            ToastUtils.showLong(msg)
        }
    }

    fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) =
        viewModelScope.launch {
            try {
                Logger.d("coroutine task start")
                block()
                Logger.d("coroutine task end")
            } catch (e: Throwable) {
                Logger.e(e, "network request error")
                error(e)
            }
        }
}