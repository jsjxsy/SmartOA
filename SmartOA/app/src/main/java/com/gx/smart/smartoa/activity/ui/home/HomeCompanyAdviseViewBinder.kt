package com.gx.smart.smartoa.activity.ui.home

import android.content.Intent
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
import com.blankj.utilcode.util.ActivityUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.WebViewActivity
import com.gx.smart.smartoa.activity.ui.home.CompanyAdvise
import com.gx.smart.smartoa.activity.ui.home.HomeCompanyAdvise
import com.gx.smart.smartoa.data.network.ApiConfig


/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe 企业资讯
 */
class HomeCompanyAdviseViewBinder :
    ItemViewBinder<HomeCompanyAdvise, HomeCompanyAdviseViewBinder.ViewHolder>(),
    View.OnClickListener {

    override fun onClick(v: View?) {
        when (v!!.id) {

        }
    }


    @NonNull
    override fun onCreateViewHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup): ViewHolder {
        val root = inflater.inflate(R.layout.item_home_company_advise_item, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, @NonNull homeAdvise: HomeCompanyAdvise) {
        initCompanyAdvise(holder.item, homeAdvise.companyAdviseList)
        holder.item.setOnItemClickListener {
            goWebView(ApiConfig.COMPANY_ACTION_URL)
        }
    }

    private fun goWebView(url: String) {
        val intent = Intent(ActivityUtils.getTopActivity(), WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.URL, url)
        ActivityUtils.startActivity(intent)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: ConvenientBanner<CompanyAdvise> = itemView.findViewById(R.id.banner)

    }

    private fun initCompanyAdvise(
        companyAdvise: ConvenientBanner<CompanyAdvise>,
        data: List<CompanyAdvise>
    ) {
        companyAdvise.setPages(object : CBViewHolderCreator {
            override fun createHolder(itemView: View): Holder<*> {
                return CompanyAdviseHolderView(itemView)
            }

            override fun getLayoutId(): Int {
                return R.layout.item_home_company_advise
            }
        }, data).setPageIndicator(
            intArrayOf(
                R.drawable.shape_action_page_indicator,
                R.drawable.shape_action_page_indicator_focus
            )
        )
            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
            .setPointViewVisible(true)
    }


    class CompanyAdviseHolderView(itemView: View) : Holder<CompanyAdvise>(itemView) {
        private lateinit var imageView: ImageView
        private lateinit var title: TextView
        private lateinit var time: TextView
        override fun updateUI(data: CompanyAdvise) {
            //设置图片圆角角度
            Glide.with(itemView).load(data.resId).transform(CenterCrop(), RoundedCorners(10))
                .into(imageView)
            title.text = data.title
            time.text = data.time
        }

        override fun initView(itemView: View) {
            imageView = itemView.findViewById(R.id.id_home_company_advise_image_view)
            title = itemView.findViewById(R.id.id_home_company_advise_title)
            time = itemView.findViewById(R.id.id_home_company_advise_time)

        }

    }

}
