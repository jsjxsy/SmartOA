package com.gx.smart.smartoa.activity.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.api.AttendanceAppProviderService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.grpc.ds.attendanceapp.getEmployeeDayRecordResp
import kotlinx.android.synthetic.main.attendance_record_fragment.*
import kotlinx.android.synthetic.main.layout_common_title.*
import org.angmarch.views.OnSpinnerItemSelectedListener
import java.util.*


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
        val monthArray = resources.getStringArray(R.array.month_items)
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val monthIndex = calendar.get(Calendar.MONTH)
        getEmployeeRecordList(calendar.timeInMillis)
        val subMonth = monthArray.slice(monthIndex..monthArray.lastIndex)
        monthSpinner.attachDataSource(subMonth)
        monthSpinner.selectedIndex = 0
        monthSpinner.onSpinnerItemSelectedListener =
            OnSpinnerItemSelectedListener { _, _, position, _ ->
                val month = monthIndex + position
                calendar.set(calendar.get(Calendar.YEAR), month, 1)
                val timestamp = TimeUtils.date2Millis(calendar.time)
                getEmployeeRecordList(timestamp)
            }
    }


    private fun getEmployeeRecordList(month: Long) {
        AttendanceAppProviderService.getInstance()
            .getEmployeeRecordList(month, object : CallBack<getEmployeeDayRecordResp>() {
                override fun callBack(result: getEmployeeDayRecordResp?) {
                    if (result == null) {
                        ToastUtils.showLong("外勤打卡超时!")
                        return
                    }
                    if (result?.code == 100) {
                        val contentList = result.contentOrBuilderList
                        if (contentList.isEmpty()) {
                            emptyLayout.visibility = View.VISIBLE
                            val list = arrayListOf<AttendanceRecord>()
                            adapter.mList = list
                        } else {
                            emptyLayout.visibility = View.GONE
                            val list = arrayListOf<AttendanceRecord>()
                            for (employeeRecord in contentList!!) {
                                val workOnTime =
                                    TimeUtils.millis2String(employeeRecord.workTime, "HH:mm:ss")
                                val workOffTime =
                                    TimeUtils.millis2String(employeeRecord.closingTime, "HH:mm:ss")
                                val date = TimeUtils.millis2String(employeeRecord.workTime, "dd")
                                list.add(AttendanceRecord(date, workOnTime, workOffTime))
                            }
                            adapter.mList = list
                        }
                        adapter.notifyDataSetChanged()
                    } else {
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
    }

}
