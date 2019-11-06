package com.gx.smart.smartoa.activity.ui.features

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.home.ActionRecommend
import com.gx.smart.smartoa.activity.ui.home.HomeActionRecommend


/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe
 */
class HomeActionViewBinder : ItemViewBinder<HomeActionRecommend, HomeActionViewBinder.ViewHolder>(),
    View.OnClickListener {

    override fun onClick(v: View?) {
        when (v!!.id) {

        }
    }


    @NonNull
    override fun onCreateViewHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup): ViewHolder {
        val root = inflater.inflate(R.layout.item_home_action_recommend_item, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, @NonNull actionRecommendList: HomeActionRecommend) {
        initActionRecommend(holder.item, actionRecommendList)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: ConvenientBanner<ActionRecommend> = itemView.findViewById(R.id.banner)

    }


    private fun initActionRecommend(actionRecommendBanner: ConvenientBanner<ActionRecommend>,actionRecommend: HomeActionRecommend ) {
        actionRecommendBanner.setPages(object : CBViewHolderCreator {
            override fun createHolder(itemView: View): Holder<*> {
                return ActionRecommendHolderView(itemView)
            }

            override fun getLayoutId(): Int {
                return R.layout.item_home_action_recommend
            }
        }, actionRecommend.actionRecommendList).setPageIndicator(
            intArrayOf(
                R.drawable.shape_action_page_indicator,
                R.drawable.shape_action_page_indicator_focus
            )
        )
            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
            .setPointViewVisible(true)
    }

    class ActionRecommendHolderView(itemView: View) : Holder<ActionRecommend>(itemView) {
        private lateinit var imageView: ImageView
        private lateinit var title: TextView
        private lateinit var time: TextView
        private lateinit var number: TextView
        override fun updateUI(data: ActionRecommend) {
            Glide.with(itemView).load(data.resId).transform(CenterCrop(), RoundedCorners(10))
                .into(imageView)
            title.text = data.title
            time.text = data.time
            number.text = data.number.toString() + "人参加"
        }

        override fun initView(itemView: View) {
            imageView = itemView.findViewById(R.id.id_home_action_recommend_image_view)
            title = itemView.findViewById(R.id.id_home_action_recommend_title)
            time = itemView.findViewById(R.id.id_time_action_recommend)
            number = itemView.findViewById(R.id.id_number_action_recommend)

        }

    }

}
