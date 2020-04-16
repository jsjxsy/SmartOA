package com.gx.smart.smartoa.activity.ui.home.adapter

import android.view.View
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.home.action.ActionRecommendViewHolder
import com.gx.wisestone.work.app.grpc.activity.AppActivityDto
import com.zhpan.bannerview.BaseBannerAdapter

/**
 *@author xiaosy
 *@create 2020/4/16
 *@Describe
 **/
class ActionRecommendAdapter : BaseBannerAdapter<AppActivityDto, ActionRecommendViewHolder>() {
    override fun getLayoutId(viewType: Int): Int = R.layout.item_home_action_recommend

    override fun createViewHolder(itemView: View?, viewType: Int): ActionRecommendViewHolder =
        ActionRecommendViewHolder(itemView!!)

    override fun onBind(
        holder: ActionRecommendViewHolder?,
        data: AppActivityDto?,
        position: Int,
        pageSize: Int
    ) {
        holder?.bindData(data, position, pageSize)
    }
}