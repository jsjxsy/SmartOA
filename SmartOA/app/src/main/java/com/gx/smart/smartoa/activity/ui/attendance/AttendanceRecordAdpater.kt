package com.gx.smart.smartoa.activity.ui.attendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gx.smart.smartoa.R

class AttendanceRecordAdapter :
    RecyclerView.Adapter<AttendanceRecordAdapter.ViewHolder>() {

    var mList: List<AttendanceRecord>? = null

    //返回item个数
    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }


    //创建ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_attendance_record_layout,
                parent,
                false
            )
        )
    }

    //填充视图
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList!![position]
        holder.timeWorkOn.text = item.content
        holder.timeWorkOff.text = item.content
        holder.date.text = item.content
        holder.timeWorkOn.text = item.content
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var timeWorkOn: TextView
        var timeWorkOff: TextView
        var date: TextView

        constructor(itemView: View) : super(itemView) {
            timeWorkOn = itemView.findViewById(R.id.timeWorkOn)
            timeWorkOff = itemView.findViewById(R.id.timeWorkOff)
            date = itemView.findViewById(R.id.date)
        }

    }
}
