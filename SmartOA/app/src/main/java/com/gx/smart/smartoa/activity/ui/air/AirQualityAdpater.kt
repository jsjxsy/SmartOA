package com.gx.smart.smartoa.activity.ui.air

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gx.smart.smartoa.R

class AirQualityAdapter :
    RecyclerView.Adapter<AirQualityAdapter.ViewHolder>() {

    var mList: List<AirQuality>? = null

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
                R.layout.item_air_quality_layout,
                parent,
                false
            )
        )
    }

    //填充视图
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList!![position]
        holder.place.text = item.content
        holder.temperature.text = item.content
        holder.humidity.text = item.content
        holder.pm.text = item.content
        holder.co2.text = item.content
        
        holder.onItemClick = onItemClick
    }

    class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        var onItemClick: OnItemClickListener? = null
        var place: TextView
        var temperature: TextView
        var humidity: TextView
        var pm: TextView
        var co2: TextView

        constructor(itemView: View) : super(itemView) {
            place = itemView.findViewById(R.id.place)
            temperature = itemView.findViewById(R.id.temperature)
            humidity = itemView.findViewById(R.id.humidity)
            pm = itemView.findViewById(R.id.pm)
            co2 = itemView.findViewById(R.id.co2)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            itemView.setOnClickListener {
                onItemClick?.onItemClick(it, layoutPosition)
            }
        }

    }
}
