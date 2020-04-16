package com.gx.smart.smartoa.activity.ui.home.head

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.arouter.ARouterConstants
import com.gx.smart.common.ApiConfig
import com.gx.smart.common.AppConfig
import com.gx.smart.eventbus.EventBusMessageConstant
import com.gx.smart.lib.widget.DrawableIndicator
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.WebViewActivity
import com.gx.smart.smartoa.activity.ui.attendance.AttendanceActivity
import com.gx.smart.smartoa.activity.ui.environmental.EnvironmentalActivity
import com.gx.smart.smartoa.activity.ui.features.AllFeatureActivity
import com.gx.smart.smartoa.activity.ui.home.viewmodel.HomeViewModel
import com.gx.smart.smartoa.activity.ui.intelligence.activity.IntelligenceParkingActivity
import com.gx.smart.smartoa.activity.ui.visitor.activity.VisitorActivity
import com.gx.wisestone.work.app.grpc.appfigure.ImagesInfoOrBuilder
import com.jeremyliao.liveeventbus.LiveEventBus
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.indicator.base.IIndicator

/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe banner 广告栏
 */
class HomeHeadViewBinder(private val viewModel: HomeViewModel) :
    ItemViewBinder<HomeHead, HomeHeadViewBinder.ViewHolder>(),
    View.OnClickListener {
    private lateinit var convenientBanner: BannerViewPager<ImagesInfoOrBuilder, LocalImageHolderView>
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.id_environmental_control_text_view -> joinCompanyContinue(
                1
            )
            R.id.id_more_text_view ->
                ActivityUtils.startActivity(
                    Intent(
                        ActivityUtils.getTopActivity(),
                        AllFeatureActivity::class.java
                    )
                )
            R.id.id_attendance_text_view -> joinCompanyContinue(
                2
            )

            R.id.id_visitor_text_view ->
                ActivityUtils.startActivity(
                    Intent(
                        ActivityUtils.getTopActivity(),
                        VisitorActivity::class.java
                    )
                )
            R.id.id_repair_text_view -> joinCompanyContinue(
                3
            )
            R.id.id_parking_text_view -> ActivityUtils.startActivity(
                Intent(
                    ActivityUtils.getTopActivity(),
                    IntelligenceParkingActivity::class.java
                )
            )

//            R.id.id_air_quality_text_view -> ActivityUtils.startActivity(
//                Intent(
//                    ActivityUtils.getTopActivity(),
//                    AirQualityActivity::class.java
//                )
//            )
//            R.id.id_share_work_text_view -> ActivityUtils.startActivity(
//                Intent(
//                    ActivityUtils.getTopActivity(),
//                    SharedWorkActivity::class.java
//                )
//            )
//            R.id.id_meeting_schedule_text_view -> ActivityUtils.startActivity(
//                Intent(
//                    ActivityUtils.getTopActivity(),
//                    MeetingScheduleActivity::class.java
//                )
//            )

        }
    }


    companion object {
        fun joinCompanyContinue(type: Int) {
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
                    2 -> gotoDetailAction(
                        type
                    )
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

        private fun gotoDetailAction(type: Int) {
            when (type) {
                1 -> ActivityUtils.startActivity(
                    Intent(
                        ActivityUtils.getTopActivity(),
                        EnvironmentalActivity::class.java
                    )
                )

                2 -> ActivityUtils.startActivity(
                    Intent(
                        ActivityUtils.getTopActivity(),
                        AttendanceActivity::class.java
                    )
                )

                3 -> ARouter.getInstance().build(ARouterConstants.REPAIR_PAGE)
                    .navigation()
            }
        }
    }


    @NonNull
    override fun onCreateViewHolder(
        @NonNull inflater: LayoutInflater,
        @NonNull parent: ViewGroup
    ): ViewHolder {
        val root = inflater.inflate(R.layout.item_home_head_item, parent, false)
        return ViewHolder(
            root
        )
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, @NonNull item: HomeHead) {
        convenientBanner = holder.item
        initClickEvent(
            holder.idEnvironmentalControlTextView,
            holder.idMoreTextView,
            holder.idAttendanceTextView,
            holder.idRepairTextView,
            holder.idVisitorTextVisitor,
            holder.idParking
        )
        initBanner(viewModel.listNetwork)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: BannerViewPager<ImagesInfoOrBuilder, LocalImageHolderView> =
            itemView.findViewById(R.id.headBanner)
        val idEnvironmentalControlTextView: TextView =
            itemView.findViewById(R.id.id_environmental_control_text_view)
        val idMoreTextView: TextView =
            itemView.findViewById(R.id.id_more_text_view)
        val idAttendanceTextView: TextView =
            itemView.findViewById(R.id.id_attendance_text_view)
        val idVisitorTextVisitor: TextView = itemView.findViewById(R.id.id_visitor_text_view)
        val idRepairTextView: TextView = itemView.findViewById(R.id.id_repair_text_view)
        val idParking: TextView = itemView.findViewById(R.id.id_parking_text_view)

    }

    private fun initClickEvent(
        id_environmental_control_text_view: TextView,
        id_more_text_view: TextView,
        id_attendance_text_view: TextView,
        idRepairTextView: TextView,
        idVisitorTextVisitor: TextView,
        idParking: TextView
    ) {
        id_environmental_control_text_view.setOnClickListener(this)
        id_more_text_view.setOnClickListener(this)
        id_attendance_text_view.setOnClickListener(this)
        idRepairTextView.setOnClickListener(this)
        idVisitorTextVisitor.setOnClickListener(this)
        idParking.setOnClickListener(this)
    }

    private fun initBanner(
        listNetwork: List<ImagesInfoOrBuilder>
    ) {

        convenientBanner.setCanLoop(true)
            .setHolderCreator { LocalImageHolderView() }
            .setIndicatorView(getDrawableIndicator())
            .setIndicatorMargin(
                0,
                0,
                0,
                ActivityUtils.getActivityByView(convenientBanner).resources.getDimensionPixelOffset(
                    R.dimen.padding_style_one
                )
            )
            .setOnPageClickListener {
                goWebView(ApiConfig.COMPANY_ACTION_URL)
            }
            .create(listNetwork.toList())
    }


    private fun getDrawableIndicator(): IIndicator? {
        val dp10: Int =
            ActivityUtils.getActivityByView(convenientBanner).resources.getDimensionPixelOffset(
                R.dimen.padding_style_one
            )
        return DrawableIndicator(convenientBanner.context)
            .setIndicatorGap(convenientBanner.resources.getDimensionPixelOffset(R.dimen.margin_style_seven))
            .setIndicatorDrawable(
                R.drawable.shape_page_indicator,
                R.drawable.shape_page_indicator_focus
            )
            .setIndicatorSize(dp10, dp10, dp10, dp10)
    }

    inner class LocalImageHolderView : com.zhpan.bannerview.holder.ViewHolder<ImagesInfoOrBuilder> {
        private lateinit var imageView: ImageView
        override fun getLayoutId(): Int = R.layout.item_home_head_item_localimage

        override fun onBind(itemView: View?, data: ImagesInfoOrBuilder?, position: Int, size: Int) {
            imageView = itemView?.findViewById(R.id.imageViewHomeBanner)!!
            updateUI(data)
        }

        private fun updateUI(data: ImagesInfoOrBuilder?) {

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
    }

    private fun goWebView(url: String) {
        val intent = Intent(ActivityUtils.getTopActivity(), WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.URL, url)
        ActivityUtils.startActivity(intent)
    }


}


