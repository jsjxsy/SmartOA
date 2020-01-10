package com.gx.smart.smartoa.activity.ui.action

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gx.smart.smartoa.R
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
        Glide.with(holder.itemView)
            .load(item.imageUrl)
            .error(R.mipmap.default_image_small)
            .placeholder(R.mipmap.default_image_small)
            //.transform(CenterCrop(), RoundedCorners(10))
            .into(holder.mImage)

        holder.redFlag.let {
            if (item.hasRead) {
                it.visibility = View.GONE
            } else {
                it.visibility = View.VISIBLE
            }
        }
        val startTime = TimeUtils.millis2String(item.startTime, "yyyy.MM.dd")
        val endTime = TimeUtils.millis2String(item.endTime, "yyyy.MM.dd")
        holder.actionTime.text = "$startTime - $endTime"
        holder.actionNumber.text = "${item.currentNum}人参加"

        holder.itemView.setOnClickListener {
            onItemClick?.onItemClick(it, position)
        }
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var onItemClick: OnItemClickListener? = null
        var actionTitle: TextView
        var actionTime: TextView
        var actionNumber: TextView
        var mImage: ImageView
        var redFlag: View

        constructor(itemView: View) : super(itemView) {
            actionTitle = itemView.findViewById(R.id.actionTitle)
            mImage = itemView.findViewById(R.id.actionImageView)
            actionTime = itemView.findViewById(R.id.actionTime)
            actionNumber = itemView.findViewById(R.id.actionNumber)
            redFlag = itemView.findViewById(R.id.redFlag)
        }

    }
}
