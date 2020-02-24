package com.gx.smart.smartoa.activity.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.eventbus.EventBusMessageConstant
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.WebViewActivity
import com.gx.smart.smartoa.activity.ui.air.AirQualityActivity
import com.gx.smart.smartoa.activity.ui.attendance.AttendanceActivity
import com.gx.smart.smartoa.activity.ui.company.MineCompanyActivity
import com.gx.smart.smartoa.activity.ui.environmental.EnvironmentalActivity
import com.gx.smart.smartoa.activity.ui.features.AllFeatureActivity
import com.gx.smart.smartoa.activity.ui.meetings.MeetingScheduleActivity
import com.gx.smart.smartoa.activity.ui.repair.RepairActivity
import com.gx.smart.smartoa.activity.ui.visitor.VisitorActivity
import com.gx.smart.smartoa.activity.ui.work.SharedWorkActivity
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AppFigureService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.appfigure.ImagesInfoOrBuilder
import com.gx.wisestone.work.app.grpc.appfigure.ImagesResponse
import com.jeremyliao.liveeventbus.LiveEventBus
import top.limuyang2.customldialog.IOSMsgDialog


/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe banner 广告栏
 */
class HomeHeadViewBinder(private val viewModel: HomeViewModel) : ItemViewBinder<HomeHead, HomeHeadViewBinder.ViewHolder>(),
    View.OnClickListener {
    private lateinit var convenientBanner: ConvenientBanner<Any>
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.id_environmental_control_text_view -> joinCompanyContinue(1)
            R.id.id_more_text_view ->
                ActivityUtils.startActivity(
                    Intent(
                        ActivityUtils.getTopActivity(),
                        AllFeatureActivity::class.java
                    )
                )
            R.id.id_attendance_text_view -> joinCompanyContinue(2)

//            R.id.id_visitor_text_view ->
//                ActivityUtils.startActivity(
//                    Intent(
//                        ActivityUtils.getTopActivity(),
//                        VisitorActivity::class.java
//                    )
//                )
            R.id.id_repair_text_view -> joinCompanyContinue(3)
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
                    1 -> LiveEventBus.get(EventBusMessageConstant.COMPANY_APPLY_STATUS_KEY,Int::class.java)
                        .post(1)
                    2 -> gotoDetailAction(type)
                    else -> LiveEventBus.get(EventBusMessageConstant.COMPANY_APPLY_STATUS_KEY,Int::class.java)
                        .post(3)
                }

            } else {
                LiveEventBus.get(EventBusMessageConstant.COMPANY_APPLY_STATUS_KEY,Int::class.java)
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

                3 -> ActivityUtils.startActivity(
                    Intent(
                        ActivityUtils.getTopActivity(),
                        RepairActivity::class.java
                    )
                )

            }
        }
    }



    @NonNull
    override fun onCreateViewHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup): ViewHolder {
        val root = inflater.inflate(R.layout.item_home_head_item, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, @NonNull item: HomeHead) {
        convenientBanner = holder.item
        initClickEvent(
            holder.idEnvironmentalControlTextView,
            holder.idMoreTextView,
            holder.idAttendanceTextView,
            holder.idRepairTextView
        )
        initBanner(convenientBanner, viewModel.listNetwork, viewModel.listLocal)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: ConvenientBanner<Any> = itemView.findViewById(R.id.headBanner)
        val idEnvironmentalControlTextView: TextView =
            itemView.findViewById(R.id.id_environmental_control_text_view)
        val idMoreTextView: TextView =
            itemView.findViewById(R.id.id_more_text_view)
        val idAttendanceTextView: TextView =
            itemView.findViewById(R.id.id_attendance_text_view)

        val idRepairTextView: TextView = itemView.findViewById(R.id.id_repair_text_view)

    }

    private fun initClickEvent(
        id_environmental_control_text_view: TextView,
        id_more_text_view: TextView,
        id_attendance_text_view: TextView,
        idRepairTextView: TextView
    ) {
        id_environmental_control_text_view.setOnClickListener(this)
        id_more_text_view.setOnClickListener(this)
        id_attendance_text_view.setOnClickListener(this)
        idRepairTextView.setOnClickListener(this)
    }

    private fun initBanner(
        convenientBanner: ConvenientBanner<Any>,
        listNetwork: List<ImagesInfoOrBuilder>,
        listLocal: List<Int>
    ) {

        if (listNetwork.isNotEmpty()) {
            convenientBanner.setPages(object : CBViewHolderCreator {
                override fun createHolder(itemView: View): NetWorkImageHolderView {
                    return NetWorkImageHolderView(itemView)
                }

                override fun getLayoutId(): Int {
                    return R.layout.item_home_head_item_localimage
                }
            }, listNetwork)
                .setPageIndicator(
                    intArrayOf(
                        R.drawable.shape_page_indicator,
                        R.drawable.shape_page_indicator_focus
                    )
                )
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPointViewVisible(true)
                .startTurning(2000)
        } else {
            convenientBanner.setPages(object : CBViewHolderCreator {
                override fun createHolder(itemView: View): LocalImageHolderView {
                    return LocalImageHolderView(itemView)
                }

                override fun getLayoutId(): Int {
                    return R.layout.item_home_head_item_localimage
                }
            }, listLocal)
                .setPageIndicator(
                    intArrayOf(
                        R.drawable.shape_page_indicator,
                        R.drawable.shape_page_indicator_focus
                    )
                )
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPointViewVisible(true)
                .startTurning(2000)
        }
    }

    //A、网络图片
    inner class NetWorkImageHolderView(itemView: View) : Holder<ImagesInfoOrBuilder>(itemView) {
        private lateinit var imageView: ImageView
        override fun updateUI(data: ImagesInfoOrBuilder) {

            if (ActivityUtils.isActivityAlive(itemView.context)) {
                val imageUrl = data.imageUrl + "?v=" + data.modifyTime
                Glide.with(itemView).load(imageUrl).into(imageView)
                imageView.setOnClickListener {
                    if (data.forwardUrl.isNotEmpty()) {
                        goWebView(data.forwardUrl)
                    }
                }
            }
        }

        override fun initView(itemView: View) {
            imageView = itemView.findViewById(R.id.imageViewHomeBanner)
        }

    }

    //B、本地图片
    inner class LocalImageHolderView(itemView: View) : Holder<Int>(itemView) {
        private lateinit var imageView: ImageView
        override fun updateUI(data: Int) {
            if (ActivityUtils.isActivityAlive(itemView.context)) {
                Glide.with(itemView).load(data).into(imageView)
            }
        }

        override fun initView(itemView: View) {
            imageView = itemView.findViewById(R.id.imageViewHomeBanner)
        }

    }

    private fun goWebView(url: String) {
        val intent = Intent(ActivityUtils.getTopActivity(), WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.URL, url)
        ActivityUtils.startActivity(intent)
    }



}


