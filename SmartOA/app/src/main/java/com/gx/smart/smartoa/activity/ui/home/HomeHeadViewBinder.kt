package com.gx.smart.smartoa.activity.ui.features

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.location.AMapLocation
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.blankj.utilcode.util.ActivityUtils
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.map.MapLocationHelper
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.WebViewActivity
import com.gx.smart.smartoa.activity.ui.air.AirQualityActivity
import com.gx.smart.smartoa.activity.ui.attendance.AttendanceActivity
import com.gx.smart.smartoa.activity.ui.environmental.EnvironmentalActivity
import com.gx.smart.smartoa.activity.ui.home.HomeHead
import com.gx.smart.smartoa.activity.ui.meetings.MeetingScheduleActivity
import com.gx.smart.smartoa.activity.ui.messages.MessageActivity
import com.gx.smart.smartoa.activity.ui.repair.RepairActivity
import com.gx.smart.smartoa.activity.ui.visitor.VisitorActivity
import com.gx.smart.smartoa.activity.ui.work.SharedWorkActivity
import com.gx.smart.smartoa.data.network.api.AppFigureService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.appfigure.ImagesInfoOrBuilder
import com.gx.wisestone.work.app.grpc.appfigure.ImagesResponse


/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe
 */
class HomeHeadViewBinder : ItemViewBinder<HomeHead, HomeHeadViewBinder.ViewHolder>(),
    View.OnClickListener, MapLocationHelper.LocationCallBack {
    private lateinit var helper: MapLocationHelper
    private lateinit var titleText: TextView
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.id_environmental_control_text_view ->
                ActivityUtils.startActivity(
                    Intent(
                        ActivityUtils.getTopActivity(),
                        EnvironmentalActivity::class.java
                    )
                )
            R.id.right_nav_Image_view ->
                ActivityUtils.startActivity(
                    Intent(
                        ActivityUtils.getTopActivity(),
                        MessageActivity::class.java
                    )
                )
            R.id.id_more_text_view ->
                ActivityUtils.startActivity(
                    Intent(
                        ActivityUtils.getTopActivity(),
                        AllFeatureActivity::class.java
                    )
                )
            R.id.id_attendance_text_view ->
                ActivityUtils.startActivity(
                    Intent(
                        ActivityUtils.getTopActivity(),
                        AttendanceActivity::class.java
                    )
                )
            R.id.id_visitor_text_view ->
                ActivityUtils.startActivity(
                    Intent(
                        ActivityUtils.getTopActivity(),
                        VisitorActivity::class.java
                    )
                )
            R.id.id_more_text_view -> ActivityUtils.startActivity(
                Intent(
                    ActivityUtils.getTopActivity(),
                    AllFeatureActivity::class.java
                )
            )

            R.id.id_repair_text_view -> ActivityUtils.startActivity(
                Intent(
                    ActivityUtils.getTopActivity(),
                    RepairActivity::class.java
                )
            )
            R.id.id_air_quality_text_view -> ActivityUtils.startActivity(
                Intent(
                    ActivityUtils.getTopActivity(),
                    AirQualityActivity::class.java
                )
            )
            R.id.id_share_work_text_view -> ActivityUtils.startActivity(
                Intent(
                    ActivityUtils.getTopActivity(),
                    SharedWorkActivity::class.java
                )
            )
            R.id.id_meeting_schedule_text_view -> ActivityUtils.startActivity(
                Intent(
                    ActivityUtils.getTopActivity(),
                    MeetingScheduleActivity::class.java
                )
            )

        }
    }


    @NonNull
    override fun onCreateViewHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup): ViewHolder {
        val root = inflater.inflate(R.layout.item_home_head_item, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, @NonNull homeHead: HomeHead) {
        helper = MapLocationHelper(holder.itemView.context, this)
        helper.startMapLocation()
        initClickEvent(
            holder.id_environmental_control_text_view,
            holder.id_more_text_view,
            holder.id_attendance_text_view,
            holder.id_visitor_text_view,
            holder.idRepairTextView,
            holder.idAirQualityTextView,
            holder.idShareWorkTextView,
            holder.idMeetingScheduleTextView
        )
        initTitleView(
            holder.title,
            holder.left_nav_text_view,
            holder.right_nav_Image_view
        )

        AppFigureService.getInstance().carouselFigure(object : CallBack<ImagesResponse?>() {
            override fun callBack(result: ImagesResponse?) {
                if (result?.code == 100) {
                    var list = result.imagesInfoOrBuilderList.toList()
                    initBanner(holder.item, list)
                }
            }

        })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: ConvenientBanner<ImagesInfoOrBuilder> = itemView.findViewById(R.id.headBanner)
        val id_environmental_control_text_view: TextView =
            itemView.findViewById(R.id.id_environmental_control_text_view)
        val id_more_text_view: TextView =
            itemView.findViewById(R.id.id_more_text_view)
        val id_attendance_text_view: TextView =
            itemView.findViewById(R.id.id_attendance_text_view)
        val id_visitor_text_view: TextView =
            itemView.findViewById(R.id.id_visitor_text_view)

        val idRepairTextView: TextView = itemView.findViewById(R.id.id_repair_text_view)
        val idAirQualityTextView: TextView = itemView.findViewById(R.id.id_air_quality_text_view)
        val idShareWorkTextView: TextView = itemView.findViewById(R.id.id_share_work_text_view)
        val idMeetingScheduleTextView: TextView =
            itemView.findViewById(R.id.id_meeting_schedule_text_view)

        val title: ViewGroup =
            itemView.findViewById(R.id.title)
        val left_nav_text_view: TextView =
            itemView.findViewById(R.id.left_nav_text_view)
        val right_nav_Image_view: ImageView =
            itemView.findViewById(R.id.right_nav_Image_view)


    }

    private fun initTitleView(
        title: ViewGroup,
        left_nav_text_view: TextView,
        right_nav_Image_view: ImageView
    ) {
        title.setBackgroundColor(Color.TRANSPARENT)
        titleText = left_nav_text_view
        right_nav_Image_view.visibility = View.VISIBLE
        right_nav_Image_view.setOnClickListener(this)
    }

    private fun initClickEvent(
        id_environmental_control_text_view: TextView,
        id_more_text_view: TextView,
        id_attendance_text_view: TextView,
        id_visitor_text_view: TextView,
        idRepairTextView: TextView,
        idAirQualityTextView: TextView,
        idShareWorkTextView: TextView,
        idMeetingScheduleTextView: TextView
    ) {
        id_environmental_control_text_view.setOnClickListener(this)
        id_more_text_view.setOnClickListener(this)
        id_attendance_text_view.setOnClickListener(this)
        id_visitor_text_view.setOnClickListener(this)
        idRepairTextView.setOnClickListener(this)
        idAirQualityTextView.setOnClickListener(this)
        idShareWorkTextView.setOnClickListener(this)
        idMeetingScheduleTextView.setOnClickListener(this)
    }

    private fun initBanner(
        convenientBanner: ConvenientBanner<ImagesInfoOrBuilder>, list: List<ImagesInfoOrBuilder>
    ) {

        convenientBanner.setPages(object : CBViewHolderCreator {
            override fun createHolder(itemView: View): Holder<*> {
                return LocalImageHolderView(itemView)
            }

            override fun getLayoutId(): Int {
                return R.layout.item_home_head_item_localimage
            }
        }, list)
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

    //B、本地图片
    inner class LocalImageHolderView(itemView: View) : Holder<ImagesInfoOrBuilder>(itemView) {
        private lateinit var imageView: ImageView
        override fun updateUI(data: ImagesInfoOrBuilder) {

            if (ActivityUtils.isActivityAlive(itemView.context)) {
                val imageUrl = data.imageUrl + "?v=" + data.modifyTime
                Glide.with(itemView).load(imageUrl).into(imageView)
                imageView.setOnClickListener {
                    goWebView(data.forwardUrl)
                }
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

    override fun onCallLocationSuc(location: AMapLocation?) {
        titleText?.let {
            it.visibility = View.VISIBLE
            it.text = location?.address
        }

    }

    fun onDestroy() {
        helper.stopMapLocation()
    }
}
