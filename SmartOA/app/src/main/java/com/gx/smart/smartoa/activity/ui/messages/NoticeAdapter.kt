package com.gx.smart.smartoa.activity.ui.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gx.smart.smartoa.R
import com.gx.wisestone.work.app.grpc.information.AppAnnouncementDto

/**
 * @author xiaosy
 * @create 2019-11-20
 * @Describe
 */
class NoticeAdapter : RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {
    private var onItemClick: OnItemClickListener? =
        null
    private var mList: List<AppAnnouncementDto>? = null
    fun setOnItemClick(onItemClick: OnItemClickListener?) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notice_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        if (onItemClick != null) {
            holder.itemView.setOnClickListener { v ->
                if (onItemClick != null) {
                    onItemClick!!.onItemClick(v, position)
                }
            }
        }
        val item = mList!![position]
        holder.title.text = item.title
        holder.subTitle.text = item.content
        if (item.hasRead) {
            holder.redFlag.visibility = View.GONE
        } else {
            holder.redFlag.visibility = View.VISIBLE
        }
        holder.time.text = item.createTime.toString()
    }

    fun setList(mList: List<AppAnnouncementDto>?) {
        this.mList = mList
    }

    fun getList(): List<AppAnnouncementDto>? {
        return this.mList
    }

    override fun getItemCount(): Int {
        return if (mList != null) mList!!.size else 0
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val time: TextView = itemView.findViewById(R.id.time)
        val redFlag: View = itemView.findViewById(R.id.redFlag)
        val subTitle: TextView = itemView.findViewById(R.id.subTitle)

    }
}