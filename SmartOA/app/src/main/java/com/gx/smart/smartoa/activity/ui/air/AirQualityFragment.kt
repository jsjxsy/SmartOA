package com.gx.smart.smartoa.activity.ui.air

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.gx.smart.smartoa.R
import com.gx.smart.common.AppConfig
import com.gx.smart.lib.http.api.GeneralInformationService
import com.gx.smart.lib.http.api.UnisiotApiService
import com.gx.smart.lib.http.base.CallBack
import com.gx.smart.lib.http.base.GrpcAsyncTask
import com.gx.wisestone.core.grpc.lib.generalinformation.WeatherInformationResponse
import com.gx.wisestone.core.grpc.lib.generalinformation.WeatherResponse
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.AirBoxDataGetResp
import kotlinx.android.synthetic.main.air_quality_fragment.*
import kotlinx.android.synthetic.main.layout_common_title.*

class AirQualityFragment : BaseFragment(), View.OnClickListener {

    companion object {
        fun newInstance() = AirQualityFragment()
    }

    val items = arrayListOf<AirQuality>()
    lateinit var adapter: AirQualityAdapter
    private var airBoxDataGetTask: GrpcAsyncTask<String, Void, AirBoxDataGetResp>? = null
    private var generalInfoTask: GrpcAsyncTask<String, Void, WeatherInformationResponse>? = null
    private lateinit var viewModel: AirQualityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.air_quality_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[AirQualityViewModel::class.java]
        initTitle()
        initContent()
    }

    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.air_quality)
        }
    }

    private fun initContent() {
        adapter = AirQualityAdapter()
        recyclerView.adapter = adapter
        getAirBoxData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }


    /**
     * 获取环境数据
     */
    private fun getAirBoxData() {
        val homeSN = SPUtils.getInstance().getString(AppConfig.SMART_HOME_SN, "")
        airBoxDataGetTask = UnisiotApiService.getInstance()
            .airBoxDataGet(homeSN, object : CallBack<AirBoxDataGetResp>() {
                override fun callBack(result: AirBoxDataGetResp?) {
                    if (!ActivityUtils.isActivityAlive(activity)) {
                        return
                    }
                    items.clear()
                    if (result == null) { //MyApplication.showToast("获取环境数据超时");
                        return
                    }
                    if (result.code == 100) {
                        if (result.result == 0) {
                            val array = result.contentList

                            if (array.size > 0) {
                                for (map in array) {
                                    if (map.envDataCount > 0) {
                                        val arr = map.getEnvData(0)
                                        if (!TextUtils.isEmpty(arr.value)) {
                                            val value =
                                                arr.value.split(",").toTypedArray()
                                            val temperature = "温度: " + value[0] + "℃"
                                            val humidity = "湿度: " + value[1] + "%"
                                            val pm = "PM2.5:" + value[3]
                                            val co2 = "CO2浓度:" + value[4]
                                            val item = AirQuality(temperature, humidity, pm, co2)
                                            items.add(item)
                                        } else {
                                            getHomeCityTem()
                                        }
                                    } else {
                                        getHomeCityTem()
                                    }
                                }
                                adapter.mList = items
                            } else {
                                getHomeCityTem()
                            }
                        }
                    }
                }
            })
    }


    private fun getHomeCityTem() {
        val province: String = ""
        val city: String = ""
        val area: String = ""

        generalInfoTask = GeneralInformationService.getInstance()
            .getWeatherInfo(province, city, area, object : CallBack<WeatherInformationResponse>() {
                override fun callBack(result: WeatherInformationResponse?) {
                    if (!ActivityUtils.isActivityAlive(activity)) {
                        return
                    }
                    if (result == null) {
                        return
                    }
                    if (result.code == 100) {
//                        val data: WeatherResponse = result.data
                        //mTvHumidityAndPm.setText("温度:" + data.getTem().toString() + "℃  天气:" + data.getWeather())
                    }
                }
            })
    }
}
