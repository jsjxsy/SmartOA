package com.gx.smart.smartoa.activity.ui.repair

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gx.smart.smartoa.R
import com.gx.wisestone.work.app.grpc.repair.RepairInfoOrBuilder

class RepairRecordAdapter :
    RecyclerView.Adapter<RepairRecordAdapter.ViewHolder>() {

    var mList: List<RepairInfoOrBuilder>? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    var onItemClick: OnItemClickListener? = null


    //返回item个数
    override fun getItemCount(): Int {
        return mList?.size ?: 0
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
        val item = mList!![position]
        holder.mContent.text = item.content
        holder.state.text = when (item.status) {
            //1.未处理2.处理中3.已完成
            1 -> "未处理"
            2 -> "处理中"
            3 -> "已完成"
            else -> "未知"

        }
        holder.employeeName.text = item.employeeName
        holder.time.text = "${item.handleTime}"
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
