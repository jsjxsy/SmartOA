package com.gx.smart.smartoa.activity.ui.meetings

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.layout_common_title.*

class MeetingScheduleFragment : Fragment(),View.OnClickListener {

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MeetingScheduleViewModel::class.java)
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
            it.visibility = View.VISIBLE
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
