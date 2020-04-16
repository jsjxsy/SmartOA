package com.gx.smart.smartoa.activity.ui.home.company

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gx.smart.smartoa.R
import com.zhpan.bannerview.BaseViewHolder

/**
 *@author xiaosy
 *@create 2020/4/16
 *@Describe
 **/
class CompanyAdviseViewHolder : BaseViewHolder<CompanyAdvise> {
    private var imageView: ImageView
    private var title: TextView
    private var time: TextView
    constructor(itemView: View) : super(itemView) {
        imageView = itemView.findViewById(R.id.id_home_company_advise_image_view)
        title = itemView.findViewById(R.id.id_home_company_advise_title)
        time = itemView.findViewById(R.id.id_home_company_advise_time)
    }

    override fun bindData(data: CompanyAdvise?, position: Int, pageSize: Int) {
        //设置图片圆角角度
        Glide.with(imageView).load(data?.resId).transform(CenterCrop(), RoundedCorners(10))
            .into(imageView)
        title.text = data?.title
        time.text = data?.time
    }
}