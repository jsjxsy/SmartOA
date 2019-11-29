package com.gx.smart.smartoa.activity.ui.meetings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gx.smart.smartoa.R

class ReservationAdapter : RecyclerView.Adapter<ReservationAdapter.ViewHolder>() {

    private lateinit var mList: List<Reservation>

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int){
            findNavController(view).navigate(
                R.id.action_meetingMyReservationFragment_to_meetingMyReservationDetailFragment
            )
        }
    }

    var onItemClick: OnItemClickListener? = null


    fun setList(mList: List<Reservation>) {
        this.mList = mList
    }

    //返回item个数
    override fun getItemCount(): Int {
        return mList?.size
    }


    //创建ViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_reservation_layout,
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
