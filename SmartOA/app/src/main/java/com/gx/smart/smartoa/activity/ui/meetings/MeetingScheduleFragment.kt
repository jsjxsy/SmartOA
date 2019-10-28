package com.gx.smart.smartoa.activity.ui.meetings

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gx.smart.smartoa.R

class MeetingScheduleFragment : Fragment() {

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
        // TODO: Use the ViewModel
    }

}
