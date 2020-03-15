package com.gx.smart.smartoa.activity.ui.environmental

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevDto

/**
 *@author xiaosy
 *@create 2019-10-30
 *@Describe
 **/
class PageAdapter(fa: FragmentActivity) : FragmentStateAdapter(
    fa
) {

    private val mPageFragment = ArrayList<PageFragmentContent>()
    private val fragments: SparseArray<Fragment>  = SparseArray()
    init {
        fragments.put(LightFragment.LIGHT_TYPE, LightFragment(mPageFragment[LightFragment.LIGHT_TYPE].itemList))
        fragments.put(CurtainFragment.CURTAIN_TYPE, CurtainFragment(mPageFragment[CurtainFragment.CURTAIN_TYPE].itemList))
        fragments.put(AirConditionerFragment.AIR_CONDITIONER_TYPE, AirConditionerFragment(mPageFragment[AirConditionerFragment.AIR_CONDITIONER_TYPE].itemList))
        fragments.put(FreshAirFragment.FRESH_AIR_TYPE, FreshAirFragment(mPageFragment[FreshAirFragment.FRESH_AIR_TYPE].itemList))
    }

//    override fun getItem(position: Int): Fragment {
//        val pageFragmentContent = mPageFragment[position]
//        when (pageFragmentContent.type) {
//            LightFragment.LIGHT_TYPE -> {
//                val lightFragment = LightFragment(pageFragmentContent.itemList)
//                lightFragment.fragment = pageFragmentContent.fragment
//                return lightFragment
//            }
//            CurtainFragment.CURTAIN_TYPE -> {
//                val curtainFragment = CurtainFragment(pageFragmentContent.itemList)
//                curtainFragment.fragment = pageFragmentContent.fragment
//                return curtainFragment
//            }
//            AirConditionerFragment.AIR_CONDITIONER_TYPE -> {
//                val airConditionerFragment = AirConditionerFragment(pageFragmentContent.itemList)
//                airConditionerFragment.fragment = pageFragmentContent.fragment
//                return airConditionerFragment
//            }
//            FreshAirFragment.FRESH_AIR_TYPE -> {
//                val freshAirFragment = FreshAirFragment(pageFragmentContent.itemList)
//                freshAirFragment.fragment = pageFragmentContent.fragment
//                return freshAirFragment
//            }
//        }
//        return Fragment()
//    }

//    override fun getCount() = mPageFragment.size
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return mPageFragment[position].title
//    }


//    override fun getItemPosition(`object`: Any): Int {
//        return POSITION_NONE
//    }


    fun addPage(page: PageFragmentContent) {
        mPageFragment.add(page)
    }

    fun clearPage() {
        mPageFragment.clear()
    }


    class PageFragmentContent(
        var type: Int,
        var itemList: List<DevDto>,
        var fragment: EnvironmentalControlFragment
    )

    override fun getItemCount(): Int = fragments.size()

    override fun createFragment(position: Int): Fragment {
       return fragments[position]
    }
}
