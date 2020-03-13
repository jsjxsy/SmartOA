package com.gx.smart.smartoa.activity.ui.messages

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gx.smart.smartoa.activity.ui.action.MineActionFragment

/**
 *@author xiaosy
 *@create 2019-10-30
 *@Describe
 **/
class PageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(
    fragmentActivity
) {

    val fragments: SparseArray<Fragment> = SparseArray()

    init {
        fragments.put(NewsFragment.NOTICE_NEWS, NewsFragment.newInstance())
        fragments.put(MineActionFragment.ACTION_TYPE, MineActionFragment.newInstance())
        fragments.put(NoticeFragment.NOTICE_TYPE, NoticeFragment.newInstance())
    }

    override fun getItemCount() = fragments.size()

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
