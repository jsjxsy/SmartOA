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
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.action.MineActionDetailFragment
import com.gx.smart.smartoa.activity.ui.messages.MessageActivity
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AppActivityService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.core.grpc.lib.common.QueryDto
import com.gx.wisestone.work.app.grpc.activity.ActivityCommonResponse
import com.gx.wisestone.work.app.grpc.activity.AppActivityDto
import com.scwang.smartrefresh.layout.SmartRefreshLayout


/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe
 */
class HomeActionViewBinder :
    ItemViewBinder<HomeActionRecommend, HomeActionViewBinder.ViewHolder>() {

    lateinit var mRefreshLayout: SmartRefreshLayout
    lateinit var actionRecommendBanner: ConvenientBanner<AppActivityDto>
    @NonNull
    override fun onCreateViewHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup): ViewHolder {
        val root = inflater.inflate(R.layout.item_home_action_recommend_item, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, @NonNull actionRecommendList: HomeActionRecommend) {
        actionRecommendBanner = holder.item
        holder.more.setOnClickListener {
            val intent = Intent(holder.itemView.context, MessageActivity::class.java)
            intent.putExtra(MessageActivity.INTENT_KEY, MessageActivity.INTENT_MESSAGE)
            ActivityUtils.startActivity(intent)
        }
        findAllApplyInfos()
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

    }

    class ActionRecommendHolderView(itemView: View) : Holder<AppActivityDto>(itemView) {
        private lateinit var imageView: ImageView
        private lateinit var title: TextView
        private lateinit var time: TextView
        private lateinit var number: TextView
        override fun updateUI(data: AppActivityDto) {
            Glide.with(itemView).load(data.imageUrl)
                .error(R.mipmap.default_image_small)
                .placeholder(R.mipmap.default_image_small)
                .transform(CenterCrop(), RoundedCorners(10))
                .into(imageView)

            title.text = data.title
            time.text = "${data.startTime} - ${data.endTime}"
            number.text = data.currentNum.toString() + "人参加"
            itemView.setOnClickListener {
                goActionDetail(it, data)
            }
        }

        override fun initView(itemView: View) {
            imageView = itemView.findViewById(R.id.actionImageView)
            title = itemView.findViewById(R.id.actionTitle)
            time = itemView.findViewById(R.id.actionTime)
            number = itemView.findViewById(R.id.actionNumber)

        }

        private fun goActionDetail(view: View, item: AppActivityDto) {
            val args = Bundle()
            args.putString(MineActionDetailFragment.ARG_TITLE, item.title)
            args.putString(
                MineActionDetailFragment.ARG_TIME,
                "${item.startTime - item.endTime}"
            )
            args.putString(MineActionDetailFragment.ARG_CONTENT, item.content)
            args.putLong(MineActionDetailFragment.ARG_ACTIVITY_ID, item.activityId)
            Navigation.findNavController(view)
                .navigate(R.id.action_navigation_home_to_mineActionActivity, args)
        }

    }


    fun findAllApplyInfos() {
        var employeeId = 0L
        val buildingSysTenantNo =
            SPUtils.getInstance().getInt(AppConfig.BUILDING_SYS_TENANT_NO, 0)
        val companySysTenantNo =
            SPUtils.getInstance().getInt(AppConfig.COMPANY_SYS_TENANT_NO, 0)
        if (buildingSysTenantNo == companySysTenantNo) {
            employeeId = SPUtils.getInstance().getLong(AppConfig.EMPLOYEE_ID, 0L)
        }
        if (employeeId == 0L) {
            mRefreshLayout?.finishRefresh()
            return
        }
        val query = QueryDto.newBuilder()
            .setPage(0)
            .setPageSize(3)
            .build()
        AppActivityService.getInstance()
            .findAllActivityInfos(query, object : CallBack<ActivityCommonResponse>() {
                override fun callBack(result: ActivityCommonResponse?) {
                    mRefreshLayout?.finishRefresh()
                    if (result == null) {
                        ToastUtils.showLong("查询活动超时!")
                        return
                    }
                    if (result?.code == 100) {
                        var list = result.contentList.toList()
                        initActionRecommend(actionRecommendBanner, list)
                    } else {
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
    }


}
