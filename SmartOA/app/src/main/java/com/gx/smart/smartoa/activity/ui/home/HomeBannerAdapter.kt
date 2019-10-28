package com.gx.smart.smartoa.activity.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.model.HomeBanner


/**
 *@author xiaosy
 *@create 2019-10-24
 *@Describe
 **/
class HomeBannerAdapter : ItemViewBinder<HomeBanner, HomeBannerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(
            inflater.inflate(
                R.layout.item_home_head_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, item: HomeBanner) {
        holder.banner.setPages(object : CBViewHolderCreator {
            override fun createHolder(itemView: View): Holder<*> {
                return LocalImageHolderView(itemView)
            }

            override fun getLayoutId(): Int {
                return R.layout.item_head_item_localimage
            }
        }, item.imageResId)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val banner = itemView.findViewById<ConvenientBanner<Int>>(R.id.headBanner)!!
    }

    //B、本地图片
    inner class LocalImageHolderView(itemView: View) : Holder<Int>(itemView) {
        lateinit var imageView: ImageView
        override fun updateUI(data: Int) {
            imageView.setImageResource(data)
        }

        override fun initView(itemView: View) {
            imageView = itemView.findViewById(R.id.imageViewHomeBanner)
        }

    }

}