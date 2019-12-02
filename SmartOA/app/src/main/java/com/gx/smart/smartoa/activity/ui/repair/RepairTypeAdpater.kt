package com.gx.smart.smartoa.activity.ui.repair

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gx.smart.smartoa.R

class RepairTypeAdapter :
    RecyclerView.Adapter<RepairTypeAdapter.ViewHolder>() {
    private var mList: List<RepairType>? = null

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
                R.layout.item_repair_type_layout,
                parent,
                false
            )
        )
    }

    //填充视图
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList!![position]
        holder.mContent.text = item.content
        holder.onItemClick = onItemClick
    }

    class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        var onItemClick: OnItemClickListener? = null
        var mContent: TextView

        constructor(itemView: View) : super(itemView) {
            mContent = itemView.findViewById(R.id.content)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            itemView.setOnClickListener {
                onItemClick?.onItemClick(it, layoutPosition)
            }
        }

    }
}
