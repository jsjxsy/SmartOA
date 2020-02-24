package com.gx.smart.smartoa.activity.ui.home

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
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.eventbus.EventBusMessageConstant
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.action.MineActionActivity
import com.gx.smart.smartoa.activity.ui.action.MineActionDetailFragment
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AppActivityService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.core.grpc.lib.common.QueryDto
import com.gx.wisestone.work.app.grpc.activity.ActivityCommonResponse
import com.gx.wisestone.work.app.grpc.activity.ActivityRequest
import com.gx.wisestone.work.app.grpc.activity.AppActivityDto
import com.jeremyliao.liveeventbus.LiveEventBus


/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe
 */
class HomeActionViewBinder(private val viewModel: HomeViewModel) :
    ItemViewBinder<HomeActionRecommend, HomeActionViewBinder.ViewHolder>() {

    lateinit var actionRecommendBanner: ConvenientBanner<AppActivityDto>
    @NonNull
    override fun onCreateViewHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup): ViewHolder {
        val root = inflater.inflate(R.layout.item_home_action_recommend_item, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, @NonNull item: HomeActionRecommend) {
        actionRecommendBanner = holder.item
        holder.more.setOnClickListener {
            val intent = Intent(holder.itemView.context, MineActionActivity::class.java)
            intent.putExtra(MineActionActivity.FROM_MORE, MineActionActivity.FROM_MORE)
            ActivityUtils.startActivity(intent)
        }
        initActionRecommend(actionRecommendBanner, viewModel.listAction)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: ConvenientBanner<AppActivityDto> = itemView.findViewById(R.id.banner)
        val more: TextView = itemView.findViewById(R.id.id_home_action_recommend_more)
    }


    private fun initActionRecommend(
        actionRecommendBanner: ConvenientBanner<AppActivityDto>,
        items: List<AppActivityDto>
    ) {
        actionRecommendBanner.setPages(object : CBViewHolderCreator {
            override fun createHolder(itemView: View): Holder<*> {
                return ActionRecommendHolderView(itemView)
            }

            override fun getLayoutId(): Int {
                return R.layout.item_home_action_recommend
            }
        }, items).setPageIndicator(
            intArrayOf(
                R.drawable.shape_action_page_indicator,
                R.drawable.shape_action_page_indicator_focus
            )
        )
            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
            .setPointViewVisible(true)
            .startTurning(3000)

    }

    inner class ActionRecommendHolderView(itemView: View) :
        Holder<AppActivityDto>(itemView) {
        private lateinit var imageView: ImageView
        private lateinit var title: TextView
        private lateinit var time: TextView
        private lateinit var number: TextView
        override fun updateUI(data: AppActivityDto) {
            if (!ActivityUtils.isActivityAlive(itemView.context)) {
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

        override fun initView(itemView: View) {
            imageView = itemView.findViewById(R.id.actionImageView)
            title = itemView.findViewById(R.id.actionTitle)
            time = itemView.findViewById(R.id.actionTime)
            number = itemView.findViewById(R.id.actionNumber)

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


}
