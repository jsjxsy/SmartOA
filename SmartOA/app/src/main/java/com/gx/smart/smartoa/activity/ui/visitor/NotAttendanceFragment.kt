package com.gx.smart.smartoa.activity.ui.visitor

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gx.smart.smartoa.R

class NotAttendanceFragment : Fragment() {

    companion object {
        fun newInstance() = NotAttendanceFragment()
    }

    private lateinit var viewModel: NotAttendanceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.not_attendance_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NotAttendanceViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
