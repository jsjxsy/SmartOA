package com.gx.smart.smartoa.activity.ui.meetings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gx.smart.smartoa.R

class MeetingScheduleAdapter(private var mList: List<MeetingSchedule>?) :
    RecyclerView.Adapter<MeetingScheduleAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    private lateinit var onItemClick: OnItemClickListener


    fun setList(mList: List<MeetingSchedule>) {
        this.mList = mList
    }

    //返回item个数
    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }


    //创建ViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MeetingScheduleAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_meeting_schedule_layout,
                parent,
                false
            )
        )
    }

    //填充视图
    override fun onBindViewHolder(holder: MeetingScheduleAdapter.ViewHolder, position: Int) {
        val item = mList!![position]
        holder.mContent.text = item.content
        holder.onItemClick = onItemClick
    }

    class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        lateinit var onItemClick: OnItemClickListener
        var mContent: TextView
        var mImage: ImageView

        constructor(itemView: View) : super(itemView) {
            mContent = itemView.findViewById(R.id.content)
            mImage = itemView.findViewById(R.id.image)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            itemView.setOnClickListener {
                onItemClick?.onItemClick(it, layoutPosition)
            }
        }

    }
}
