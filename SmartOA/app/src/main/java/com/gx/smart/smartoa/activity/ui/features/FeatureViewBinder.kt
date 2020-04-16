package com.gx.smart.smartoa.activity.ui.features

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.air.AirQualityActivity
import com.gx.smart.smartoa.activity.ui.home.head.HomeHeadViewBinder
import com.gx.smart.smartoa.activity.ui.meetings.MeetingScheduleActivity
import com.gx.smart.smartoa.activity.ui.visitor.activity.VisitorActivity
import com.gx.smart.smartoa.activity.ui.work.SharedWorkActivity


/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe
 */
class FeatureViewBinder : ItemViewBinder<Feature, FeatureViewBinder.ViewHolder>() {

    lateinit var fragmentManager: FragmentManager

    @NonNull
    override fun onCreateViewHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup): ViewHolder {
        val root = inflater.inflate(R.layout.item_feature_all_feature, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, @NonNull item: Feature) {
        val context = ActivityUtils.getActivityByView(holder.item)
        val drawableTop = ContextCompat.getDrawable(context, item.resId)
        holder.item.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null)
        holder.item.text = item.name
        holder.item.setOnClickListener {
            when (holder.item.text) {
                holder.itemView.resources.getString(R.string.environmental_control) -> {
                    HomeHeadViewBinder.joinCompanyContinue(1)
                }
                holder.itemView.resources.getString(R.string.meeting_schedule) -> {
                    ActivityUtils.startActivity(
                        Intent(
                            ActivityUtils.getTopActivity(),
                            MeetingScheduleActivity::class.java
                        )
                    )
                }
                holder.itemView.resources.getString(R.string.attendance) -> {
                    HomeHeadViewBinder.joinCompanyContinue(2)
                }
                holder.itemView.resources.getString(R.string.repair) -> {
                    HomeHeadViewBinder.joinCompanyContinue(3)
                }
                holder.itemView.resources.getString(R.string.air_quality) -> {
                    ActivityUtils.startActivity(
                        Intent(
                            ActivityUtils.getTopActivity(),
                            AirQualityActivity::class.java
                        )
                    )
                }
                holder.itemView.resources.getString(R.string.share_work) -> {
                    ActivityUtils.startActivity(
                        Intent(
                            ActivityUtils.getTopActivity(),
                            SharedWorkActivity::class.java
                        )
                    )
                }
                holder.itemView.resources.getString(R.string.visitor) -> {
                    ActivityUtils.startActivity(
                        Intent(
                            ActivityUtils.getTopActivity(),
                            VisitorActivity::class.java
                        )
                    )
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: TextView = itemView.findViewById(R.id.item)
    }
}
