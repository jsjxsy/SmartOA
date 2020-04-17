package com.gx.smart.smartoa.activity.ui.home.company

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.lib.http.ApiConfig
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.WebViewActivity
import com.gx.smart.smartoa.activity.ui.home.adapter.CompanyAdviseAdapter
import com.zhpan.bannerview.BannerViewPager


/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe 企业资讯
 */
class HomeCompanyAdviseViewBinder :
    ItemViewBinder<HomeCompanyAdvise, HomeCompanyAdviseViewBinder.ViewHolder>(),
    View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }


    @NonNull
    override fun onCreateViewHolder(
        @NonNull inflater: LayoutInflater,
        @NonNull parent: ViewGroup
    ): ViewHolder {
        val root = inflater.inflate(R.layout.item_home_company_advise_item, parent, false)
        return ViewHolder(
            root
        )
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, @NonNull item: HomeCompanyAdvise) {
        initCompanyAdvise(holder.mBannerViewPager, item.companyAdviseList)
    }

    private fun goWebView(url: String) {
        val intent = Intent(ActivityUtils.getTopActivity(), WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.URL, url)
        ActivityUtils.startActivity(intent)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBannerViewPager =
            itemView.findViewById<BannerViewPager<CompanyAdvise, CompanyAdviseViewHolder>>(R.id.banner)!!
    }


    private fun initCompanyAdvise(
        mBannerViewPager: BannerViewPager<CompanyAdvise, CompanyAdviseViewHolder>,
        data: List<CompanyAdvise>
    ) {
        mBannerViewPager
            .setAdapter(CompanyAdviseAdapter())
            .setOnPageClickListener {
                goWebView(ApiConfig.COMPANY_ACTION_URL)
            }
            .create(data.toList())
    }

}
