package com.gx.smart.smartoa.activity.ui.home.adapter

import android.view.View
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.home.company.CompanyAdvise
import com.gx.smart.smartoa.activity.ui.home.company.CompanyAdviseViewHolder
import com.zhpan.bannerview.BaseBannerAdapter

/**
 *@author xiaosy
 *@create 2020/4/16
 *@Describe
 **/
class CompanyAdviseAdapter : BaseBannerAdapter<CompanyAdvise, CompanyAdviseViewHolder>() {
    override fun getLayoutId(viewType: Int): Int = R.layout.item_home_company_advise

    override fun createViewHolder(itemView: View?, viewType: Int): CompanyAdviseViewHolder =
        CompanyAdviseViewHolder(itemView!!)

    override fun onBind(
        holder: CompanyAdviseViewHolder?,
        data: CompanyAdvise?,
        position: Int,
        pageSize: Int
    ) {
        holder?.bindData(data, position, pageSize)
    }
}