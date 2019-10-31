package com.gx.smart.smartoa.activity.ui.environmental

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R

/**
 * @author xiaosy
 * @create 2019-10-31
 * @Describe
 */
class AirConditionerViewBinder : ItemViewBinder<AirConditioner, AirConditionerViewBinder.ViewHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        val root =
            inflater.inflate(R.layout.item_environmental_control_air_conditioner, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, airConditioner: AirConditioner) {
        holder.text.text = airConditioner.text
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.text)
    }
}
