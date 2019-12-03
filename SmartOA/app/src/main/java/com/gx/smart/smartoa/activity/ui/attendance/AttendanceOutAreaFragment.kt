package com.gx.smart.smartoa.activity.ui.attendance

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gx.smart.smartoa.R

class AttendanceOutAreaFragment : Fragment() {

    companion object {
        fun newInstance() = AttendanceOutAreaFragment()
    }

    private lateinit var viewModel: AttendanceOutAreaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.attendance_out_area_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AttendanceOutAreaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
