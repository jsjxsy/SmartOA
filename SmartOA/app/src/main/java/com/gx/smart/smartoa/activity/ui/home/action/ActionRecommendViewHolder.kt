package com.gx.smart.smartoa.activity.ui.home.action

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gx.smart.common.AppConfig
import com.gx.smart.eventbus.EventBusMessageConstant
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.action.MineActionDetailFragment
import com.gx.wisestone.work.app.grpc.activity.AppActivityDto
import com.jeremyliao.liveeventbus.LiveEventBus
import com.zhpan.bannerview.BaseViewHolder

/**
 *@author xiaosy
 *@create 2020/4/16
 *@Describe
 **/
class ActionRecommendViewHolder : BaseViewHolder<AppActivityDto> {
    private var imageView: ImageView
    private var title: TextView
    private var time: TextView
    private var number: TextView
    constructor(itemView: View) : super(itemView) {
        imageView = itemView?.findViewById(R.id.actionImageView)!!
        title = itemView.findViewById(R.id.actionTitle)
        time = itemView.findViewById(R.id.actionTime)
        number = itemView.findViewById(R.id.actionNumber)
    }

    override fun bindData(data: AppActivityDto?, position: Int, pageSize: Int) {
        if (!ActivityUtils.isActivityAlive(itemView.context)) {
            return
        }
        if (data == null) {
            return
        }
        Glide.with(itemView).load(data.imageUrl)
            .error(R.mipmap.default_image_small)
            .placeholder(R.mipmap.default_image_small)
            .transform(CenterCrop(), RoundedCorners(10))
            .into(imageView)

        title.text = data.title
        val startTime = TimeUtils.millis2String(data.startTime, "yyyy.MM.dd")
        val endTime = TimeUtils.millis2String(data.endTime, "yyyy.MM.dd")

        time.text = "$startTime - $endTime"
        number.text = data.currentNum.toString() + "人参加"
        itemView.setOnClickListener {
            joinCompanyContinue(it, data)
        }
    }


    private fun joinCompanyContinue(
        view: View,
        item: AppActivityDto
    ) {
        val buildingSysTenantNo =
            SPUtils.getInstance().getInt(AppConfig.BUILDING_SYS_TENANT_NO, 0)
        val companySysTenantNo =
            SPUtils.getInstance().getInt(AppConfig.COMPANY_SYS_TENANT_NO, 0)
        if (buildingSysTenantNo == companySysTenantNo) {
            when (SPUtils.getInstance().getInt(AppConfig.COMPANY_APPLY_STATUS, 0)) {
                1 -> LiveEventBus.get(
                    EventBusMessageConstant.COMPANY_APPLY_STATUS_KEY,
                    Int::class.java
                )
                    .post(1)
                2 -> goActionDetail(view, item)
                else -> LiveEventBus.get(
                    EventBusMessageConstant.COMPANY_APPLY_STATUS_KEY,
                    Int::class.java
                )
                    .post(3)
            }

        } else {
            LiveEventBus.get(EventBusMessageConstant.COMPANY_APPLY_STATUS_KEY, Int::class.java)
                .post(3)
        }

    }

    private fun goActionDetail(view: View, item: AppActivityDto) {
        val args = Bundle()
        args.putString(MineActionDetailFragment.ARG_TITLE, item.title)
        val startTime = TimeUtils.millis2String(item.startTime, "yyyy.MM.dd")
        val endTime = TimeUtils.millis2String(item.endTime, "yyyy.MM.dd")
        args.putString(
            MineActionDetailFragment.ARG_TIME,
            "$startTime - $endTime"
        )
        args.putString(MineActionDetailFragment.ARG_CONTENT, item.content)
        args.putLong(MineActionDetailFragment.ARG_ACTIVITY_ID, item.activityId)
        Navigation.findNavController(view)
            .navigate(R.id.action_navigation_home_to_mineActionActivity, args)
    }

}