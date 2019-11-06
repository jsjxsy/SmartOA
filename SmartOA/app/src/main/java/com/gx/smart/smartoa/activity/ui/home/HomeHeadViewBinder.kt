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
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.blankj.utilcode.util.ActivityUtils
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.attendance.AttendanceActivity
import com.gx.smart.smartoa.activity.ui.environmental.EnvironmentalActivity
import com.gx.smart.smartoa.activity.ui.messages.MessageActivity
import com.gx.smart.smartoa.activity.ui.visitor.VisitorActivity


/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe
 */
class HomeHeadViewBinder : ItemViewBinder<HomeHead, HomeHeadViewBinder.ViewHolder>(),
    View.OnClickListener {

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

        }
    }


    @NonNull
    override fun onCreateViewHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup): ViewHolder {
        val root = inflater.inflate(R.layout.item_home_head_item, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, @NonNull homeHead: HomeHead) {
        initBanner(holder.item, homeHead)
        initClickEvent(
            holder.id_environmental_control_text_view,
            holder.id_more_text_view,
            holder.id_attendance_text_view,
            holder.id_visitor_text_view
        )
        initTitleView(
            holder.title,
            holder.left_nav_text_view,
            holder.right_nav_Image_view
        )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: ConvenientBanner<Int> = itemView.findViewById(R.id.headBanner)
        val id_environmental_control_text_view: TextView =
            itemView.findViewById(R.id.id_environmental_control_text_view)
        val id_more_text_view: TextView =
            itemView.findViewById(R.id.id_environmental_control_text_view)
        val id_attendance_text_view: TextView =
            itemView.findViewById(R.id.id_environmental_control_text_view)
        val id_visitor_text_view: TextView =
            itemView.findViewById(R.id.id_environmental_control_text_view)
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
        left_nav_text_view.visibility = View.VISIBLE
        left_nav_text_view.text = "悦盛国际"
        right_nav_Image_view.visibility = View.VISIBLE
        right_nav_Image_view.setOnClickListener(this)
    }

    private fun initClickEvent(
        id_environmental_control_text_view: TextView,
        id_more_text_view: TextView,
        id_attendance_text_view: TextView,
        id_visitor_text_view: TextView
    ) {
        id_environmental_control_text_view.setOnClickListener(this)
        id_more_text_view.setOnClickListener(this)
        id_attendance_text_view.setOnClickListener(this)
        id_visitor_text_view.setOnClickListener(this)
    }

    private fun initBanner(convenientBanner: ConvenientBanner<Int>, homeHead: HomeHead) {

        convenientBanner.setPages(object : CBViewHolderCreator {
            override fun createHolder(itemView: View): Holder<*> {
                return LocalImageHolderView(itemView)
            }

            override fun getLayoutId(): Int {
                return R.layout.item_home_head_item_localimage
            }
        }, homeHead.images)
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
    inner class LocalImageHolderView(itemView: View) : Holder<Int>(itemView) {
        lateinit var imageView: ImageView
        override fun updateUI(data: Int) {
            imageView.setImageResource(data)
        }

        override fun initView(itemView: View) {
            imageView = itemView.findViewById(R.id.imageViewHomeBanner)
        }

    }

}
