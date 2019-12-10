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
                return LightFragment(pageFragmentContent.itemList)
            }
            CurtainFragment.CURTAIN_TYPE -> {
                return CurtainFragment(pageFragmentContent.itemList)
            }
            AirConditionerFragment.AIR_CONDITIONER_TYPE -> {
                return AirConditionerFragment(pageFragmentContent.itemList)
            }
            FreshAirFragment.FRESH_AIR_TYPE -> {
                return FreshAirFragment(pageFragmentContent.itemList)
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


    class PageFragmentContent(var title: String, var type: Int, var itemList: List<DevDto>)
}
