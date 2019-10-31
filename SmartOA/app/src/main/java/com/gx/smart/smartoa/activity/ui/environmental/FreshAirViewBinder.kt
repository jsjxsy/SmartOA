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
class FreshAirViewBinder : ItemViewBinder<FreshAir, FreshAirViewBinder.ViewHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        val root = inflater.inflate(R.layout.item_environmental_control_fresh_air, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, freshAir: FreshAir) {
        holder.text.text = freshAir.text
    }

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val text: TextView = itemView.findViewById(R.id.text)
     }
}
