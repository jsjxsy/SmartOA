package com.gx.smart.smartoa.utils

import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.Glide
import com.gx.smart.smartoa.R
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

/**
 *@author xiaosy
 *@create 2020-02-20
 *@Describe 自定义view DataBinding
 **/
@BindingAdapter("refreshListener")
fun SmartRefreshLayout.refreshListener(
    listener: OnRefreshListener?
) {
    if (listener != null) {
        this.setOnRefreshListener(listener)
    }

}


@BindingAdapter("loadMoreListener")
fun SmartRefreshLayout.loadMoreListener(
    listener: OnLoadmoreListener?
) {
    if (listener != null) {
        this.setOnLoadmoreListener(listener)
    }

}

@BindingAdapter("url")
fun AppCompatImageView.url(url: String?) {
    Glide.with(this)
        .load(url)
        .error(R.mipmap.default_image_small)
        .placeholder(R.mipmap.default_image_small)
        .into(this)
}

@BindingAdapter("startTime", "endTime")
fun TextView.time(startTime: Long, endTime: Long) {
    val startTime = TimeUtils.millis2String(startTime, "yyyy.MM.dd")
    val endTime = TimeUtils.millis2String(endTime, "yyyy.MM.dd")
    this.text = "$startTime - $endTime"
}

@BindingAdapter("number")
fun TextView.number(number: Int) {
    this.text = "${number}人参加"
}