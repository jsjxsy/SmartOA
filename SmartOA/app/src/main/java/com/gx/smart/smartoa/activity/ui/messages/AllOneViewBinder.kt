package com.gx.smart.smartoa.activity.ui.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R


/**
 * @author xiaosy
 * @create 2019-10-31
 * @Describe
 */
class AllOneViewBinder : ItemViewBinder<AllOne, AllOneViewBinder.ViewHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        val root = inflater.inflate(R.layout.item_message_all_item_one, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, allOne: AllOne) {

    }

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
