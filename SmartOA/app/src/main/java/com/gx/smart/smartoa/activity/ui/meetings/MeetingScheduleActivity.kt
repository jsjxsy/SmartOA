package com.gx.smart.smartoa.activity.ui.meetings

import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.base.BaseActivity

class MeetingScheduleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_schedule)
    }

    override fun onSupportNavigateUp() =
        findNavController(this, R.id.meetingScheduleFragment).navigateUp()
}
