package com.gx.smart.smartoa.activity.ui.home.adapter

import android.view.View
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.home.head.ImageViewHolder
import com.gx.wisestone.work.app.grpc.appfigure.ImagesInfoOrBuilder
import com.zhpan.bannerview.BaseBannerAdapter

/**
 *@author xiaosy
 *@create 2020/4/16
 *@Describe
 **/
class ImageAdapter : BaseBannerAdapter<ImagesInfoOrBuilder, ImageViewHolder>() {
    override fun getLayoutId(viewType: Int): Int = R.layout.item_home_head_item_localimage

    override fun createViewHolder(itemView: View?, viewType: Int): ImageViewHolder =
        ImageViewHolder(itemView!!)

    override fun onBind(
        holder: ImageViewHolder?,
        data: ImagesInfoOrBuilder?,
        position: Int,
        pageSize: Int
    ) {
        holder?.bindData(data, position, pageSize)
    }
}