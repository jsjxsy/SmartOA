package com.gx.smart.smartoa.activity.ui.home.action

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.common.AppConfig
import com.gx.smart.eventbus.EventBusMessageConstant
import com.gx.smart.lib.widget.DrawableIndicator
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.action.MineActionActivity
import com.gx.smart.smartoa.activity.ui.action.MineActionDetailFragment
import com.gx.smart.smartoa.activity.ui.home.adapter.ActionRecommendAdapter
import com.gx.smart.smartoa.activity.ui.home.viewmodel.HomeViewModel
import com.gx.wisestone.work.app.grpc.activity.AppActivityDto
import com.jeremyliao.liveeventbus.LiveEventBus
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.indicator.base.IIndicator


/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe 推荐活动
 */
class HomeActionViewBinder(private val viewModel: HomeViewModel) :
    ItemViewBinder<HomeActionRecommend, HomeActionViewBinder.ViewHolder>() {

    lateinit var actionRecommendBanner: BannerViewPager<AppActivityDto, ActionRecommendViewHolder>

    @NonNull
    override fun onCreateViewHolder(
        @NonNull inflater: LayoutInflater,
        @NonNull parent: ViewGroup
    ): ViewHolder {
        val root = inflater.inflate(R.layout.item_home_action_recommend_item, parent, false)
        return ViewHolder(
            root
        )
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, @NonNull item: HomeActionRecommend) {
        actionRecommendBanner = holder.item
        holder.more.setOnClickListener {
            val intent = Intent(holder.itemView.context, MineActionActivity::class.java)
            intent.putExtra(MineActionActivity.FROM_MORE, MineActionActivity.FROM_MORE)
            ActivityUtils.startActivity(intent)
        }
        initActionRecommend(viewModel.listAction)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: BannerViewPager<AppActivityDto, ActionRecommendViewHolder> =
            itemView.findViewById(R.id.banner)
        val more: TextView = itemView.findViewById(R.id.id_home_action_recommend_more)
    }


    private fun initActionRecommend(
        items: List<AppActivityDto>
    ) {
        actionRecommendBanner
            .setAdapter(ActionRecommendAdapter())
            .create(items.toList())

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


