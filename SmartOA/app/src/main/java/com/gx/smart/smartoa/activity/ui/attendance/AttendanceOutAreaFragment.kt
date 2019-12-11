package com.gx.smart.smartoa.activity.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.api.AppAttendanceService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.common.CommonResponse

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


    private fun attendance(
        latitude: String, longitude: String,
        address: String
    ) {
        AppAttendanceService.getInstance()
            .attendance(latitude, longitude, address, object : CallBack<CommonResponse>() {
                override fun callBack(result: CommonResponse?) {
                    if (result == null) {
                        ToastUtils.showLong("外勤打卡超时!")
                        return
                    }
                    if (result?.code == 100) {
                    }
                }

            })
    }

}
