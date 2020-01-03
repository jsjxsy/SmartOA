package com.gx.smart.smartoa.activity.ui.meetings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gx.smart.smartoa.R

/**
 * A simple [Fragment] subclass.
 */
class MeetingAttendeesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_metting_attendees, container, false)
    }


}
