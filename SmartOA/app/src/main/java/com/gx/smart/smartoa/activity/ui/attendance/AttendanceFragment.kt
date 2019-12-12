package com.gx.smart.smartoa.activity.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.attendance_fragment.*
import kotlinx.android.synthetic.main.layout_common_title.*

class AttendanceFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.right_nav_text_view -> findNavController().navigate(R.id.action_attendanceFragment_to_attendanceRecordFragment)
            R.id.attendance_out_area -> findNavController().navigate(R.id.action_attendanceFragment_to_attendanceOutAreaFragment)
        }
    }

    companion object {
        fun newInstance() = AttendanceFragment()
    }

    private lateinit var viewModel: AttendanceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.attendance_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AttendanceViewModel::class.java)
        initTitle()
        initContent()
    }

    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title?.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.attendance)
        }

        right_nav_text_view?.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.attendance_record)
            it.setOnClickListener(this)
        }
    }

    private fun initContent() {
        attendance_out_area.setOnClickListener(this)
    }

}
