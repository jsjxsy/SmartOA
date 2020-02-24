package com.gx.smart.smartoa.utils

import androidx.databinding.BindingAdapter
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

/**
 *@author xiaosy
 *@create 2020-02-20
 *@Describe
 **/
@BindingAdapter("refreshListener")
fun SmartRefreshLayout.refreshListener(
    listener: OnRefreshListener?
) {
    Logger.e("---> refreshListener ---->$listener")
    if (listener != null) {
        this.setOnRefreshListener(listener)
    }

}