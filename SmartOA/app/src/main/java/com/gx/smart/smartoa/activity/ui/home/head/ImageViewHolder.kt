package com.gx.smart.smartoa.activity.ui.home.head

import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.util.ActivityUtils
import com.bumptech.glide.Glide
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.WebViewActivity
import com.gx.wisestone.work.app.grpc.appfigure.ImagesInfoOrBuilder
import com.zhpan.bannerview.BaseViewHolder

/**
 *@author xiaosy
 *@create 2020/4/16
 *@Describe
 **/
class ImageViewHolder : BaseViewHolder<ImagesInfoOrBuilder> {
    private var imageView: ImageView

    constructor(itemView: View) : super(itemView) {
        imageView = itemView.findViewById(R.id.imageViewHomeBanner)
    }

    override fun bindData(data: ImagesInfoOrBuilder?, position: Int, pageSize: Int) {
        if (data == null) {
            if (ActivityUtils.isActivityAlive(imageView.context)) {
                Glide.with(imageView).load(R.mipmap.home_banner_test).into(imageView)
            }
            return
        }


        if (ActivityUtils.isActivityAlive(imageView.context)) {
            val imageUrl = data.imageUrl + "?v=" + data.modifyTime
            Glide.with(imageView).load(imageUrl).into(imageView)
            imageView.setOnClickListener {
                if (data.forwardUrl.isNotEmpty()) {
                    goWebView(data.forwardUrl)
                }
            }
        }
    }

    private fun goWebView(url: String) {
        val intent = Intent(ActivityUtils.getTopActivity(), WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.URL, url)
        ActivityUtils.startActivity(intent)
    }

}