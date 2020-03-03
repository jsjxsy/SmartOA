package com.gx.smart.smartoa.activity.ui.meetings

import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.gx.smart.smartoa.R
import com.gx.smart.lib.base.BaseActivity
import kotlinx.android.synthetic.main.activity_meeting_schedule.*

class MeetingScheduleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_meeting_schedule)
        val navHostFragment = meetingScheduleFragmentEnter as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_meeting_schedule)
        if (intent.hasExtra("toMeetingMyReservationFragment")) {
            graph.startDestination = R.id.meetingMyReservationFragment
        }else{
            graph.startDestination = R.id.meetingScheduleFragment
        }
        navHostFragment.navController.graph = graph
    }

    override fun onSupportNavigateUp() =
        findNavController(this, R.id.meetingScheduleFragmentEnter).navigateUp()
}
