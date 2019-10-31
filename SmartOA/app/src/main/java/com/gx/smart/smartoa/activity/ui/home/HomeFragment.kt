package com.gx.smart.smartoa.activity.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.drakeet.multitype.MultiTypeAdapter
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.environmental.EnvironmentalActivity
import com.gx.smart.smartoa.activity.ui.messages.MessageActivity
import com.gx.smart.smartoa.data.model.HomeActionRecommend
import com.gx.smart.smartoa.data.model.HomeCompanyAdvise
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_common_title.*


class HomeFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.id_environmental_control_text_view ->
                startActivity(Intent(activity, EnvironmentalActivity::class.java))
            R.id.right_nav_Image_view ->
                startActivity(Intent(activity, MessageActivity::class.java))
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private val adapter = MultiTypeAdapter()
    private val items = ArrayList<Any>()
    private lateinit var context: FragmentActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
        context = requireActivity()
        initClickEvent()

        initTitleView()
//        initRecyclerView()
        initBanner()
        initActionRecommend()
        initCompanyAdvise()
    }

    private fun initClickEvent() {
        id_environmental_control_text_view.setOnClickListener(this)
    }

    private fun initBanner() {
        val convenientBanner = view!!.findViewById<ConvenientBanner<Int>>(R.id.headBanner)
        val localImages = arrayListOf<Int>(
            R.mipmap.home_banner_test,
            R.mipmap.home_banner_test,
            R.mipmap.home_banner_test
        )
        convenientBanner.setPages(object : CBViewHolderCreator {
            override fun createHolder(itemView: View): Holder<*> {
                return LocalImageHolderView(itemView)
            }

            override fun getLayoutId(): Int {
                return R.layout.item_head_item_localimage
            }
        }, localImages)
            .setPageIndicator(
                intArrayOf(
                    R.drawable.shape_page_indicator,
                    R.drawable.shape_page_indicator_focus
                )
            )
            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
            .setPointViewVisible(true)
            .startTurning(2000)
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


    private fun initTitleView() {
        title.setBackgroundColor(Color.TRANSPARENT)
        left_nav_text_view.visibility = View.VISIBLE
        left_nav_text_view.text = "悦盛国际"
        right_nav_Image_view.visibility = View.VISIBLE
        right_nav_Image_view.setOnClickListener(this)
    }

//    fun initRecyclerView() {
//        val recyclerView = view!!.findViewById<RecyclerView>(R.id.homeRecyclerView)
//        adapter.register(HomeBannerAdapter())
//        recyclerView.adapter = adapter
//        val localImages = arrayListOf<Int>(R.mipmap.home_banner_test, R.mipmap.home_banner_test)
//        val imageItem = HomeBanner(localImages)
//
//        items.add(imageItem)
//        adapter.items = items
//        adapter.notifyDataSetChanged()
//    }

    private fun initActionRecommend() {
        val data = arrayListOf<HomeActionRecommend>(
            HomeActionRecommend(R.mipmap.home_banner_test, "广信篮球队报名开始啦！", "2019-10-10 14:39", 10),
            HomeActionRecommend(R.mipmap.home_banner_test, "广信篮球队报名开始啦！", "2019-10-10 14:39", 5),
            HomeActionRecommend(R.mipmap.home_banner_test, "广信篮球队报名开始啦！", "2019-10-10 14:39", 1)
        )
        val actionRecommendBanner =
            view!!.findViewById<ConvenientBanner<HomeActionRecommend>>(R.id.action_recommend_banner)
        actionRecommendBanner.setPages(object : CBViewHolderCreator {
            override fun createHolder(itemView: View): Holder<*> {
                return ActionRecommendHolderView(itemView)
            }

            override fun getLayoutId(): Int {
                return R.layout.item_home_action_recommend
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

    class ActionRecommendHolderView(itemView: View) : Holder<HomeActionRecommend>(itemView) {
        private lateinit var imageView: ImageView
        private lateinit var title: TextView
        private lateinit var time: TextView
        private lateinit var number: TextView
        override fun updateUI(data: HomeActionRecommend) {
            Glide.with(itemView).load(data.imageResId).transform(CenterCrop(), RoundedCorners(10))
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


    private fun initCompanyAdvise() {
        val data = arrayListOf<HomeCompanyAdvise>(
            HomeCompanyAdvise(R.mipmap.home_banner_test, "1 广信篮球队报名开始啦！", "2019-10-10 14:39"),
            HomeCompanyAdvise(R.mipmap.home_banner_test, "2 广信篮球队报名开始啦！", "2019-10-10 14:39"),
            HomeCompanyAdvise(R.mipmap.home_banner_test, "3 广信篮球队报名开始啦！", "2019-10-10 14:39")
        )
        val companyAdvise =
            view!!.findViewById<ConvenientBanner<HomeCompanyAdvise>>(R.id.company_advice_banner)
        companyAdvise.setPages(object : CBViewHolderCreator {
            override fun createHolder(itemView: View): Holder<*> {
                return CompanyAdviseHolderView(itemView)
            }

            override fun getLayoutId(): Int {
                return R.layout.item_home_company_advise
            }
        }, data).setPageIndicator(
            intArrayOf(
                com.gx.smart.smartoa.R.drawable.shape_action_page_indicator,
                com.gx.smart.smartoa.R.drawable.shape_action_page_indicator_focus
            )
        )
            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
            .setPointViewVisible(true)
    }


    class CompanyAdviseHolderView(itemView: View) : Holder<HomeCompanyAdvise>(itemView) {
        private lateinit var imageView: ImageView
        private lateinit var title: TextView
        private lateinit var time: TextView
        override fun updateUI(data: HomeCompanyAdvise) {
            //设置图片圆角角度
            Glide.with(itemView).load(data.imageResId).transform(CenterCrop(), RoundedCorners(10))
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
