package com.gx.smart.smartoa.activity.ui.meetings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.layout_common_title.*

class MeetingScheduleFragment : BaseFragment(),View.OnClickListener {

    companion object {
        fun newInstance() = MeetingScheduleFragment()
    }

    private lateinit var viewModel: MeetingScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.meeting_schedule_fragment, container, false)
    }

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MeetingScheduleViewModel::class.java)
        initTitle()
    }


    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title?.let {
            it.text = getString(R.string.meeting_schedule)
            it.visibility = View.VISIBLE
        }
        right_nav_text_view?.let {
//            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
            it.text = getString(R.string.mine_schedule)
        }

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.right_nav_text_view -> findNavController().navigate(R.id.action_meetingScheduleFragment_to_meetingMyReservationFragment)
        }
    }
}
