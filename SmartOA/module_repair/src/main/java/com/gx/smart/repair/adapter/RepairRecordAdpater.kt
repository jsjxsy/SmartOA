package com.gx.smart.repair.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.TimeUtils
import com.gx.smart.repair.R
import com.gx.wisestone.work.app.grpc.repair.RepairInfoOrBuilder

class RepairRecordAdapter :
    RecyclerView.Adapter<RepairRecordAdapter.ViewHolder>() {

    var mList: List<RepairInfoOrBuilder> = arrayListOf()

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    var onItemClick: OnItemClickListener? = null


    //返回item个数
    override fun getItemCount(): Int {
        return mList.size
    }

    fun addList(list: List<RepairInfoOrBuilder>) {
        mList.toMutableList().apply {
            addAll(list)
            mList = this
        }
    }

    fun clear() {
        mList.toMutableList().apply {
            clear()
            mList = this
        }
    }


    //创建ViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_repair_record_layout,
                parent,
                false
            )
        )
    }

    //填充视图
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]
        holder.mContent.text = when (item.type) {
            1 -> "设备损坏"
            2 -> "办公绿化"
            3 -> "公共卫生"
            else -> "未知"
        }
        holder.state.text = when (item.status) {
            //1.未处理2.处理中3.已完成
            1 -> {
                holder.state.setTextColor(holder.itemView.resources.getColor(R.color.font_color_style_ten))
                "未处理"
            }
            2 -> {
                holder.state.setTextColor(holder.itemView.resources.getColor(R.color.font_color_style_eleven))
                "处理中"
            }
            3 -> {
                holder.state.setTextColor(holder.itemView.resources.getColor(R.color.font_color_style_night))
                "已完成"
            }
            else -> {
                holder.state.setTextColor(holder.itemView.resources.getColor(R.color.font_color_style_five))
                "未知"
            }

        }

        holder.employeeName.text = item.handleName ?: "--"

        holder.time.text = if (item.handleTime == 0L) {
            "-"
        } else {
            TimeUtils.millis2String(item.handleTime)
        }
        holder.onItemClick = onItemClick
    }

    class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        var onItemClick: OnItemClickListener? = null
        var mContent: TextView
        var state: TextView
        var employeeName: TextView
        var time: TextView

        constructor(itemView: View) : super(itemView) {
            mContent = itemView.findViewById(R.id.content)
            state = itemView.findViewById(R.id.state)
            employeeName = itemView.findViewById(R.id.employeeName)
            time = itemView.findViewById(R.id.time)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            itemView.setOnClickListener {
                onItemClick?.onItemClick(it, layoutPosition)
            }
        }

    }
}
