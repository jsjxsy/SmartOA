package com.gx.smart.smartoa.activity.ui.messages

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gx.smart.smartoa.activity.ui.action.MineActionFragment

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
    private val newsFragment: Fragment = NewsFragment.newInstance()
    private val mineActionFragment: Fragment = NewsFragment.newInstance()
    private val noticeFragment: Fragment = NoticeFragment.newInstance()
    override fun getItem(position: Int): Fragment {
        val pageFragmentContent = mPageFragment[position]

        when (pageFragmentContent.type) {
            NewsFragment.NOTICE_NEWS -> {
                return newsFragment
            }
            MineActionFragment.ACTION_TYPE -> {
                return mineActionFragment
            }
            NoticeFragment.NOTICE_TYPE -> {
                return noticeFragment
            }

        }
        return AllFragment.newInstance()
    }

    override fun getCount() = mPageFragment.size

    override fun getPageTitle(position: Int): CharSequence? {
        return mPageFragment[position].title
    }

    fun addPage(page: PageFragmentContent) {
        mPageFragment.add(page)
    }


    class PageFragmentContent(var title: String, var type: Int)
}
