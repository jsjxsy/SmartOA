package com.gx.smart.smartoa.activity.ui.mine

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.gx.smart.smartoa.R

/**
 *@author xiaosy
 *@create 2019-10-25
 *@Describe
 **/
class MineAdapter(private val ctx: Context) : BaseAdapter() {
    var lists = ArrayList<MineFragment.Item>()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rootView =
            LayoutInflater.from(ctx).inflate(R.layout.item_mine_list_view_layout, parent, false)
        if((lists[position].itemIcon != -1) and
                lists[position].itemName.isNotBlank()){
            val item = rootView.findViewById<TextView>(R.id.mine_item_text_view)
            item.text = lists[position].itemName
            val drawableLeft = ctx.resources.getDrawable(lists[position].itemIcon)
            item.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
            //item.setCompoundDrawablePadding(4)
        }
        return rootView

    }


    override fun getItem(position: Int): Any {
        if (lists.isNotEmpty()) {
            return lists[position]
        }
        return MineFragment.Item(-1,"")
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return lists.size
    }
}