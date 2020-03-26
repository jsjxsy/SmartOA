package com.gx.smart.smartoa.activity.ui.visitor


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.evnironmental_control_fragment.*
import kotlinx.android.synthetic.main.fragment_mine_visitor.viewPager
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class MineVisitorRecordFragment : BaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine_visitor, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTitle()
        initContent()
    }


    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.mine_visitor)
        }
    }

    private fun initContent() {
        viewPager.adapter = object : FragmentStateAdapter(
            activity!!
        ) {
            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                return arrayListOf(
                    AttendanceVisitorFragment.newInstance(),
                    NotAttendanceFragment.newInstance()
                )[position]
            }
        }
        TabLayoutMediator(id_environmental_control_tab, viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = arrayListOf("已到访客", "未到访客")[position]
            }).attach()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }

}
