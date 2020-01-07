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
import com.blankj.utilcode.util.SPUtils
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.air.AirQualityActivity
import com.gx.smart.smartoa.activity.ui.attendance.AttendanceActivity
import com.gx.smart.smartoa.activity.ui.company.MineCompanyActivity
import com.gx.smart.smartoa.activity.ui.environmental.EnvironmentalActivity
import com.gx.smart.smartoa.activity.ui.meetings.MeetingScheduleActivity
import com.gx.smart.smartoa.activity.ui.repair.RepairActivity
import com.gx.smart.smartoa.activity.ui.visitor.VisitorActivity
import com.gx.smart.smartoa.activity.ui.work.SharedWorkActivity
import com.gx.smart.smartoa.data.network.AppConfig
import top.limuyang2.customldialog.IOSMsgDialog


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

    override fun onBindViewHolder(@NonNull holder: ViewHolder, @NonNull feature: Feature) {
        val context = ActivityUtils.getActivityByView(holder.itemView)
        val drawableTop = ContextCompat.getDrawable(context, feature.resId)
        holder.item.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null)
        holder.item.text = feature.name
        holder.item.setOnClickListener {
            when (holder.item.text) {
                holder.itemView.resources.getString(R.string.environmental_control) -> {
                    joinCompanyContinue(1)
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
                    joinCompanyContinue(2)
                }
                holder.itemView.resources.getString(R.string.repair) -> {
                    joinCompanyContinue(3)
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


    private fun joinCompanyContinue(type: Int) {
        val employeeId = SPUtils.getInstance().getLong(AppConfig.EMPLOYEE_ID, 0L)
        if (employeeId == 0L) {
            when (SPUtils.getInstance().getInt(AppConfig.COMPANY_APPLY_STATUS, 0)) {
                1 -> IOSMsgDialog.init(fragmentManager!!)
                    .setTitle("加入企业")
                    .setMessage("您申请的企业在审核中，请耐心等待")
                    .setPositiveButton("确定").show()

                2 -> IOSMsgDialog.init(fragmentManager!!)
                    .setTitle("加入企业")
                    .setMessage("您还未入驻任何企业，请先进行企业身份认证")
                    .setPositiveButton("马上认证", View.OnClickListener {
                        ActivityUtils.startActivity(
                            Intent(
                                ActivityUtils.getTopActivity(),
                                MineCompanyActivity::class.java
                            )
                        )
                    }).show()
            }

            return
        }

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
