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
import androidx.navigation.fragment.findNavController
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.attendance_fragment.*
import kotlinx.android.synthetic.main.layout_common_title.*
import java.util.*

class AttendanceFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.right_nav_text_view -> findNavController().navigate(R.id.action_attendanceFragment_to_attendanceRecordFragment)
            R.id.attendance_out_area -> findNavController().navigate(R.id.action_attendanceFragment_to_attendanceOutAreaFragment)
        }
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

        TimeHandler(time).startScheduleUpdate()
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        dateText.text = "${year}年${month}月${day}日 星期" + getWeak(calendar)
        time.text = "${hour}:${minute}:${second}"
    }

    companion object {
        fun newInstance() = AttendanceFragment()
        private class TimeHandler(private val timeTextView: TextView) :
            Handler(Looper.getMainLooper()) {

            private var mStopped = false

            fun post() {
                //每隔1秒发送一次消息
                sendMessageDelayed(obtainMessage(0), 1000)
            }

            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                val calendar = Calendar.getInstance(TimeZone.getDefault())
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                val second = calendar.get(Calendar.SECOND)

                if (!mStopped) {
                    timeTextView.text = "${hour}:${minute}:${second}"
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

    /**
     * 获取当前星期
     * @param calendar Calendar
     * @return String
     */
    private fun getWeak(calendar: Calendar): String {
        return when (calendar.get(Calendar.WEEK_OF_MONTH)) {
            1 -> "日"
            2 -> "一"
            3 -> "二"
            4 -> "三"
            5 -> "四"
            6 -> "五"
            else -> "六"
        }
    }


}
