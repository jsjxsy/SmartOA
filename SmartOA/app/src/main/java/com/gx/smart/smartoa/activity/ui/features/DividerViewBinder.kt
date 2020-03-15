package com.gx.smart.smartoa.activity.ui.features

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R


/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe
 */
class DividerViewBinder : ItemViewBinder<Divider, DividerViewBinder.ViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup): ViewHolder {
        val root = inflater.inflate(R.layout.item_line_recycler_view, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, @NonNull category: Divider) {
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
