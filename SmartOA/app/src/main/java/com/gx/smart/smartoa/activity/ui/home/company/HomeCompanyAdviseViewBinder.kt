package com.gx.smart.smartoa.activity.ui.home.company

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.WebViewActivity
import com.gx.smart.smartoa.data.network.ApiConfig
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.adapter.OnPageChangeListenerAdapter
import com.zhpan.bannerview.constants.IndicatorGravity
import com.zhpan.indicator.enums.IndicatorSlideMode


/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe 企业资讯
 */
class HomeCompanyAdviseViewBinder :
    ItemViewBinder<HomeCompanyAdvise, HomeCompanyAdviseViewBinder.ViewHolder>(),
    View.OnClickListener {
    //


    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }


    @NonNull
    override fun onCreateViewHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup): ViewHolder {
        val root = inflater.inflate(R.layout.item_home_company_advise_item, parent, false)
        return ViewHolder(
            root
        )
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, @NonNull homeAdvise: HomeCompanyAdvise) {
        initCompanyAdvise(holder.mBannerViewPager, homeAdvise.companyAdviseList)
//        holder.itemView.setOnClickListener{
//            goWebView(ApiConfig.COMPANY_ACTION_URL)
//        }

    }

    private fun goWebView(url: String) {
        val intent = Intent(ActivityUtils.getTopActivity(), WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.URL, url)
        ActivityUtils.startActivity(intent)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //private lateinit var mBannerViewPager: BannerViewPager<CompanyAdvise, CompanyAdviseHolderView>
        val mBannerViewPager =
            itemView.findViewById<BannerViewPager<CompanyAdvise, CompanyAdviseHolderView>>(R.id.banner)!!
        //mBannerViewPager = findViewById(R.id.banner_view)
        //initViewPager()
    }

//    private fun initCompanyAdvise(
//        companyAdvise: ConvenientBanner<CompanyAdvise>,
//        data: List<CompanyAdvise>
//    ) {
//        companyAdvise.setPages(object : CBViewHolderCreator {
//            override fun createHolder(itemView: View): Holder<*> {
//                return CompanyAdviseHolderView(
//                    itemView
//                )
//            }
//
//            override fun getLayoutId(): Int {
//                return R.layout.item_home_company_advise
//            }
//        }, data).setPageIndicator(
//            intArrayOf(
//                R.drawable.shape_action_page_indicator,
//                R.drawable.shape_action_page_indicator_focus
//            )
//        )
//            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
//            .setPointViewVisible(true)
//    }


    //    class CompanyAdviseHolderView(itemView: View) : Holder<CompanyAdvise>(itemView) {
//        private lateinit var imageView: ImageView
//        private lateinit var title: TextView
//        private lateinit var time: TextView
//        override fun updateUI(data: CompanyAdvise) {
//            //设置图片圆角角度
//            Glide.with(itemView).load(data.resId).transform(CenterCrop(), RoundedCorners(10))
//                .into(imageView)
//            title.text = data.title
//            time.text = data.time
//        }
//
//        override fun initView(itemView: View) {
//            imageView = itemView.findViewById(R.id.id_home_company_advise_image_view)
//            title = itemView.findViewById(R.id.id_home_company_advise_title)
//            time = itemView.findViewById(R.id.id_home_company_advise_time)
//
//        }
//
//    }


    private fun initCompanyAdvise(
        mBannerViewPager: BannerViewPager<CompanyAdvise, CompanyAdviseHolderView>,
        data: List<CompanyAdvise>
    ) {
        mBannerViewPager.setCanLoop(true)
            .setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
            .setIndicatorMargin(0, 0, 0, ActivityUtils.getActivityByView(mBannerViewPager).getResources().getDimensionPixelOffset(R.dimen.padding_style_one))
            .setIndicatorGravity(IndicatorGravity.CENTER)
            .setHolderCreator { CompanyAdviseHolderView() }
            .setOnPageClickListener {
                goWebView(ApiConfig.COMPANY_ACTION_URL)
            }
            .create(data.toList())
    }

    private fun initViewPager() {


    }


    class CompanyAdviseHolderView : com.zhpan.bannerview.holder.ViewHolder<CompanyAdvise> {
        private lateinit var imageView: ImageView
        private lateinit var title: TextView
        private lateinit var time: TextView
        override fun getLayoutId(): Int {
            return R.layout.item_home_company_advise
        }

        override fun onBind(itemView: View?, data: CompanyAdvise?, position: Int, size: Int) {
            imageView = itemView?.findViewById(R.id.id_home_company_advise_image_view)!!
            title = itemView?.findViewById(R.id.id_home_company_advise_title)
            time = itemView?.findViewById(R.id.id_home_company_advise_time)
            updateUI(data)
        }

        private fun updateUI(data: CompanyAdvise?) {
            //设置图片圆角角度
            Glide.with(imageView).load(data?.resId).transform(CenterCrop(), RoundedCorners(10))
                .into(imageView)
            title.text = data?.title
            time.text = data?.time
        }


    }

}
