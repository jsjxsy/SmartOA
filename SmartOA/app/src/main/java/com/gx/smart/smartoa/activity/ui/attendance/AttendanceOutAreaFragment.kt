package com.gx.smart.smartoa.activity.ui.attendance

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.amap.api.location.AMapLocation
import com.amap.api.maps2d.MapView
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.map.MapLocationHelper
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.api.AppAttendanceService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import kotlinx.android.synthetic.main.attendance_out_area_fragment.*
import kotlinx.android.synthetic.main.layout_common_title.*
import java.util.*


class AttendanceOutAreaFragment : Fragment(), MapLocationHelper.LocationCallBack {

    companion object {
        fun newInstance() = AttendanceOutAreaFragment()
        private class TimeHandler(private val timeTextView: TextView) :
            Handler(Looper.getMainLooper()) {

            private var mStopped = false

            private fun post() {
                //每隔1秒发送一次消息
                sendMessageDelayed(obtainMessage(0), 1000 * 60)
            }

            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                val calendar = Calendar.getInstance(TimeZone.getDefault())
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                if (!mStopped) {
                    timeTextView.text = "${hour}:${minute}"
                    //实现实时更新
                    post()
                }
            }

            fun startScheduleUpdate() {
                mStopped = false
                post()
            }

        }
    }

    private lateinit var viewModel: AttendanceOutAreaViewModel
    private lateinit var mapView: MapView
    private lateinit var helper: MapLocationHelper
    private lateinit var latitude: String
    private lateinit var longitude: String
    private var address: String? = null
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
        mapView = map as MapView
        mapView.onCreate(savedInstanceState) // 此方法必须重写
        helper = MapLocationHelper(context, this)
        helper.startMapLocation()
        initTitle()
        initContent()

    }

    private fun initContent() {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        dateText.text = "${year}年${month}月${day}日"

        status.text = "${hour}:${minute}"
        TimeHandler(status).startScheduleUpdate()

        apply.setOnClickListener {
            if (address == null) {
                ToastUtils.showLong("没有获取到位置信息")
            } else {
                attendance(address!!, latitude, longitude)
            }
        }
    }

    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener {
                activity?.onBackPressed()
            }
        }
        center_title?.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.attendance)
        }

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
                        status.visibility = View.VISIBLE
                        status.text = "成功"
                    } else {
                        ToastUtils.showLong(result.msg)
                    }
                }

            })
    }

    override fun onCallLocationSuc(location: AMapLocation?) {
        addressText.text = location?.address
        address = addressText.text.toString()
        latitude = location?.latitude.toString()
        longitude = location?.longitude.toString()

    }

    override fun onDestroy() {
        super.onDestroy()
        helper.stopMapLocation()
    }

}
