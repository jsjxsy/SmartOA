package com.gx.smart.smartoa.activity.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.lib.http.api.AttendanceAppProviderService
import com.gx.smart.lib.http.base.CallBack
import com.gx.wisestone.work.grpc.ds.attendanceapp.getEmployeeDayRecordResp
import kotlinx.android.synthetic.main.attendance_record_fragment.*
import kotlinx.android.synthetic.main.layout_common_title.*
import java.util.*


class AttendanceRecordFragment : BaseFragment(), View.OnClickListener {
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

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AttendanceRecordViewModel::class.java)
        initTitle()
        initContent()
    }


    override fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title?.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.attendance_record)
        }

    }

    override fun initContent() {
        adapter = AttendanceRecordAdapter()
        recyclerView.adapter = adapter
        //添加Android自带的分割线
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        getEmployeeRecordList(calendar.timeInMillis)
        selectedTime.text = TimeUtils.date2String(calendar.time, "MM月")
        selectedTime.setOnClickListener {
            showDateSelect()
        }
    }


    private fun getEmployeeRecordList(month: Long) {
        AttendanceAppProviderService.getInstance()
            .getEmployeeRecordList(month, object : CallBack<getEmployeeDayRecordResp>() {
                override fun callBack(result: getEmployeeDayRecordResp?) {
                    if (!ActivityUtils.isActivityAlive(activity)) {
                        return
                    }

                    if (result == null) {
                        ToastUtils.showLong("外勤打卡超时!")
                        return
                    }
                    if (result.code == 100) {
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


    private fun showDateSelect() {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val startDate = Calendar.getInstance()
        startDate.set(calendar.get(Calendar.YEAR) - 1, 0, 1)
        val endDate = Calendar.getInstance()
        endDate.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1)
        //时间选择器
        TimePickerBuilder(activity,
            OnTimeSelectListener { date, v ->
                selectedTime.text = TimeUtils.date2String(date, "MM月")
                val timestamp = TimeUtils.date2Millis(date)
                getEmployeeRecordList(timestamp)
            })
            .setType(booleanArrayOf(true, true, false, false, false, false))// 默认全部显示)
            .setDate(endDate)
            .setRangDate(startDate, endDate)//起始终止年月日设定
            .build()
            .show()
    }


}
