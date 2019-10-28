package com.gx.smart.smartoa.activity.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.drakeet.multitype.MultiTypeAdapter
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.layout_common_title.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private val adapter = MultiTypeAdapter()
    private val items = ArrayList<Any>()

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
        initTitleView()
//        initRecyclerView()
        initBanner()
    }

    private fun initBanner() {
        val convenientBanner = view!!.findViewById<ConvenientBanner<Int>>(R.id.convenientBanner)
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


    fun initTitleView() {
        left_nav_text_view.visibility = View.VISIBLE
        left_nav_text_view.text = "悦盛国际"
        right_nav_Image_view.visibility = View.VISIBLE
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

}
