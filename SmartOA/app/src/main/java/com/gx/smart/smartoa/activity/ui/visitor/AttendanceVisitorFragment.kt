package com.gx.smart.smartoa.activity.ui.visitor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.gx.smart.smartoa.R

class AttendanceVisitorFragment : Fragment() {

    companion object {
        fun newInstance() = AttendanceVisitorFragment()
    }

    private lateinit var viewModel: AttendanceVisitorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.attendance_visitor_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AttendanceVisitorViewModel::class.java)
    }

}
