package com.gx.smart.smartoa.activity.ui.visitor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import androidx.lifecycle.ViewModelProvider
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.visitor.viewmodel.NotAttendanceViewModel

class NotAttendanceFragment : BaseFragment() {

    companion object {
        fun newInstance() =
            NotAttendanceFragment()
    }

    private lateinit var viewModel: NotAttendanceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.not_attendance_fragment, container, false)
    }

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotAttendanceViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
