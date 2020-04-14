package com.gx.smart.smartoa.activity.ui.visitor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import androidx.lifecycle.ViewModelProvider
import com.gx.smart.smartoa.R

class AttendanceVisitorFragment : BaseFragment() {

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

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AttendanceVisitorViewModel::class.java)
    }

}
