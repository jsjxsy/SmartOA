package com.gx.smart.smartoa.activity.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import androidx.lifecycle.ViewModelProvider
import com.gx.smart.smartoa.R

class AttendanceOutAreaRecordFragment : BaseFragment() {

    companion object {
        fun newInstance() = AttendanceOutAreaRecordFragment()
    }

    private lateinit var viewModel: AttendanceOutAreaRecordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.attendance_out_area_record_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AttendanceOutAreaRecordViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
