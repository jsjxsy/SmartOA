package com.gx.smart.smartoa.activity.ui.action

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.databinding.ItemActionLayoutBinding
import com.gx.wisestone.work.app.grpc.activity.AppActivityDto

class ActionAdapter :
    RecyclerView.Adapter<ActionAdapter.ViewHolder>() {

    var mList: List<AppActivityDto> = arrayListOf()

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    var onItemClick: OnItemClickListener? = null


    //返回item个数
    override fun getItemCount(): Int {
        return mList.size
    }


    //创建ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val dataBindingUtil = DataBindingUtil.inflate<ItemActionLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_action_layout,
            parent,
            false
        )
        return ViewHolder(dataBindingUtil)
    }

    //填充视图
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]
        holder.itemBinding.data = item
        holder.itemView.setOnClickListener {
            onItemClick?.onItemClick(it, position)
        }
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var itemBinding: ItemActionLayoutBinding

        constructor(itemBinding: ItemActionLayoutBinding) : super(itemBinding.root) {
            this.itemBinding = itemBinding
        }

    }
}
