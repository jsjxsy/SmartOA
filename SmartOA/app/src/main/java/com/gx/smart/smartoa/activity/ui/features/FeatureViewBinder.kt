package com.gx.smart.smartoa.activity.ui.features

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R


/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe
 */
class FeatureViewBinder : ItemViewBinder<Feature, FeatureViewBinder.ViewHolder>() {

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
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: TextView = itemView.findViewById(R.id.item)
    }
}
