package com.gx.smart.smartoa.activity.ui.environmental

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevDto

/**
 *@author xiaosy
 *@create 2019-10-30
 *@Describe
 **/
class PageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private val mPageFragment = ArrayList<PageFragmentContent>()

    override fun getItem(position: Int): Fragment {
        val pageFragmentContent = mPageFragment[position]
        when (pageFragmentContent.type) {
            LightFragment.LIGHT_TYPE -> {
                val lightFragment = LightFragment(pageFragmentContent.itemList)
                lightFragment.fragment = pageFragmentContent.fragment
                return lightFragment
            }
            CurtainFragment.CURTAIN_TYPE -> {
                val curtainFragment = CurtainFragment(pageFragmentContent.itemList)
                curtainFragment.fragment = pageFragmentContent.fragment
                return curtainFragment
            }
            AirConditionerFragment.AIR_CONDITIONER_TYPE -> {
                val airConditionerFragment = AirConditionerFragment(pageFragmentContent.itemList)
                airConditionerFragment.fragment = pageFragmentContent.fragment
                return airConditionerFragment
            }
            FreshAirFragment.FRESH_AIR_TYPE -> {
                val freshAirFragment = FreshAirFragment(pageFragmentContent.itemList)
                freshAirFragment.fragment = pageFragmentContent.fragment
                return freshAirFragment
            }
        }
        return Fragment()
    }

    override fun getCount() = mPageFragment.size

    override fun getPageTitle(position: Int): CharSequence? {
        return mPageFragment[position].title
    }

    fun addPage(page: PageFragmentContent) {
        mPageFragment.add(page)
    }

    fun clearPage() {
        mPageFragment.clear()
    }


    class PageFragmentContent(
        var title: String,
        var type: Int,
        var itemList: List<DevDto>,
        var fragment: EnvironmentalControlFragment
    )
}
