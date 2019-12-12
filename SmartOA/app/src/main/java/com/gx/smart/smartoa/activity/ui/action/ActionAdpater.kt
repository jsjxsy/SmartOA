package com.gx.smart.smartoa.activity.ui.action

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gx.smart.smartoa.R
import com.gx.wisestone.work.app.grpc.activity.AppActivityDto

class ActionAdapter :
    RecyclerView.Adapter<ActionAdapter.ViewHolder>() {

    var mList: List<AppActivityDto>? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    var onItemClick: OnItemClickListener? = null


    //返回item个数
    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }


    //创建ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_action_layout,
                parent,
                false
            )
        )
    }

    //填充视图
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList!![position]
        holder.actionTitle.text = item.title
        Glide.with(holder.itemView).load(item.imageUrl).into(holder.mImage)
        holder.actionTime.text = "${item.startTime} - ${item.endTime}"
        holder.actionNumber.text = "${item.currentNum}人参加"
        holder.onItemClick = onItemClick
    }

    class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        var onItemClick: OnItemClickListener? = null
        var actionTitle: TextView
        var actionTime: TextView
        var actionNumber: TextView
        var mImage: ImageView

        constructor(itemView: View) : super(itemView) {
            actionTitle = itemView.findViewById(R.id.actionTitle)
            mImage = itemView.findViewById(R.id.actionImageView)
            actionTime = itemView.findViewById(R.id.actionTime)
            actionNumber = itemView.findViewById(R.id.actionNumber)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            itemView.setOnClickListener {
                onItemClick?.onItemClick(it, layoutPosition)
            }
        }

    }
}
