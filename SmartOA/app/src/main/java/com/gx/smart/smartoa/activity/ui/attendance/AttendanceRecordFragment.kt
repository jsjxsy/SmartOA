package com.gx.smart.smartoa.activity.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.attendance_record_fragment.*
import kotlinx.android.synthetic.main.layout_common_title.*

class AttendanceRecordFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }

    companion object {
        fun newInstance() = AttendanceRecordFragment()
    }

    private lateinit var adapter: AttendanceRecordAdapter
    private lateinit var viewModel: AttendanceRecordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.attendance_record_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AttendanceRecordViewModel::class.java)
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
            it.text = getString(R.string.attendance_record)
        }

    }

    private fun initContent() {
        adapter = AttendanceRecordAdapter()
        recyclerView.adapter = adapter
        adapter.mList = arrayListOf(
            AttendanceRecord("",""),
            AttendanceRecord("",""),
            AttendanceRecord("",""),
            AttendanceRecord("","")
        )
    }

}
