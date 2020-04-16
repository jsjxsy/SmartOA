package com.gx.smart.smartoa.activity.ui.intelligence.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.donkingliang.labels.LabelsView
import com.gx.smart.lib.base.BaseFragment
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.intelligence.viewmodel.CarQueryViewModel
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.car_query_fragment.*
import kotlinx.android.synthetic.main.layout_common_title.*
import wang.relish.widget.vehicleedittext.VehicleKeyboardHelper


class CarQueryFragment : BaseFragment(), View.OnClickListener {

    companion object {
        fun newInstance() =
            CarQueryFragment()
    }

    private lateinit var viewModel: CarQueryViewModel
    private lateinit var labelsView: LabelsView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_query_fragment, container, false)
    }

    override fun onBindLayout(): Int {
        return R.layout.car_query_fragment;
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CarQueryViewModel::class.java)
    }

    override fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title?.let {
            it.text = "车辆查询"
            it.visibility = View.VISIBLE
        }
    }

    override fun initContent() {
        carNo8.setOnClickListener(this)
        labelsView = labels
        val label: ArrayList<String> = ArrayList()
        label.add("Android")
        label.add("IOS")
        label.add("前端")
        labelsView.setLabels(label) //直接设置一个字符串数组就可以了。
        //标签的点击监听
        labelsView.setOnLabelClickListener { label, data, position ->
            //label是被点击的标签，data是标签所对应的数据，position是标签的位置。
        }
        VehicleKeyboardHelper.bind(carNoEditText) // 为输入框绑定车牌号输入键盘
        carNoEditText.requestFocus()
        carNoEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Logger.d("afterTextChanged start s:$s")
                if (s.isNullOrBlank()) {
                    return
                }

                when (s.length) {
                    1 -> {
                        carNo1.text = s[0].toString()
                        carNo2.text = ""
                        carNo3.text = ""
                        carNo4.text = ""
                        carNo5.text = ""
                        carNo6.text = ""
                        carNo7.text = ""
                        carNo8.text = ""
                    }
                    2 -> {
                        carNo1.text = s[0].toString()
                        carNo2.text = s[1].toString()
                        carNo3.text = ""
                        carNo4.text = ""
                        carNo5.text = ""
                        carNo6.text = ""
                        carNo7.text = ""
                        carNo8.text = ""
                    }
                    3 -> {
                        carNo1.text = s[0].toString()
                        carNo2.text = s[1].toString()
                        carNo3.text = s[2].toString()
                        carNo4.text = ""
                        carNo5.text = ""
                        carNo6.text = ""
                        carNo7.text = ""
                        carNo8.text = ""
                    }
                    4 -> {
                        carNo1.text = s[0].toString()
                        carNo2.text = s[1].toString()
                        carNo3.text = s[2].toString()
                        carNo4.text = s[3].toString()
                        carNo5.text = ""
                        carNo6.text = ""
                        carNo7.text = ""
                        carNo8.text = ""
                    }
                    5 -> {
                        carNo1.text = s[0].toString()
                        carNo2.text = s[1].toString()
                        carNo3.text = s[2].toString()
                        carNo4.text = s[3].toString()
                        carNo5.text = s[4].toString()
                        carNo6.text = ""
                        carNo7.text = ""
                        carNo8.text = ""
                    }
                    6 -> {
                        carNo1.text = s[0].toString()
                        carNo2.text = s[1].toString()
                        carNo3.text = s[2].toString()
                        carNo4.text = s[3].toString()
                        carNo5.text = s[4].toString()
                        carNo6.text = s[5].toString()
                        carNo7.text = ""
                        carNo8.text = ""
                    }
                    7 -> {
                        carNo1.text = s[0].toString()
                        carNo2.text = s[1].toString()
                        carNo3.text = s[2].toString()
                        carNo4.text = s[3].toString()
                        carNo5.text = s[4].toString()
                        carNo6.text = s[5].toString()
                        carNo7.text = s[6].toString()
                        carNo8.text = ""
                    }
                    8 -> {
                        carNo1.text = s[0].toString()
                        carNo2.text = s[1].toString()
                        carNo3.text = s[2].toString()
                        carNo4.text = s[3].toString()
                        carNo5.text = s[4].toString()
                        carNo6.text = s[5].toString()
                        carNo7.text = s[6].toString()
                        if (carNo8.text.isNullOrEmpty()) {
                            carNo8.text = s[7].toString()
                            carNo8.setTextColor(resources.getColor(R.color.font_color_style_six))
                        }
                    }
                }


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Logger.d("beforeTextChanged start s:$s after:$after count:$count")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Logger.d("onTextChanged start s:$s start:$start count:$count")
            }

        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.carNo8 -> if (carNo8.text == "新能源") {
                carNo8.text = ""
                carNo8.setTextColor(resources.getColor(R.color.font_color_style_six))
            } else {
                carNo8.text = "新能源"
                carNo8.setTextColor(resources.getColor(R.color.font_color_style_night))
            }
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }
}
