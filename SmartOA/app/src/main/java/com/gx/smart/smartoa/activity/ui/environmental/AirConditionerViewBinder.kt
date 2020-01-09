package com.gx.smart.smartoa.activity.ui.environmental

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.UnisiotApiService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevDto
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.UnisiotResp

/**
 * @author xiaosy
 * @create 2019-10-31
 * @Describe
 */
class AirConditionerViewBinder :
    ItemViewBinder<AirConditioner, AirConditionerViewBinder.ViewHolder>() {

    private var devComTask: GrpcAsyncTask<String, Void, UnisiotResp>? = null
    var fragment: EnvironmentalControlFragment? = null

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        val root =
            inflater.inflate(R.layout.item_environmental_control_air_conditioner, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: AirConditioner) {
        holder.text.text = item.light.devName
        //这种空调没有自动模式
        if (item.light.model == "air_midea_ac" //变频多联机中央空调温控器（III）
            || item.light.model == ("air_hitachi_ac") //变频多联机中央空调温控器（IV）
            || item.light.model == ("air_iracc_central_ac") //变频多联机中央空调温控器（VI）
            || item.light.model == ("air_toshiba_ac") // 变频多联机中央空调温控器（VII）
            || item.light.model == ("ir_air_conditioner") //红外空调
        ) {
            holder.auto.visibility = View.GONE
        } else {
            holder.auto.visibility = View.VISIBLE
        }
        //这种空调没有除湿、送风模式
        if (item.light.model == ("ir_air_conditioner")) { //红外空调
            holder.wind.visibility = View.GONE
            holder.water.visibility = View.GONE
        } else {
            holder.wind.visibility = View.VISIBLE
            holder.water.visibility = View.VISIBLE
        }
        //这种空调风速没有中风
        if (item.light.model == ("ir_air_conditioner")) { //红外空调
            holder.midWind.visibility = View.GONE
        } else {
            holder.midWind.visibility = View.VISIBLE
        }
        //这种空调风速没有自动风
        if (item.light.model == ("air_mcquay_central_ac") //变频多联机中央空调温控器（I）
            || item.light.model == ("air_daikin_central_ac") //变频多联机中央空调温控器（II）
            || item.light.model == ("air_midea_ac") //变频多联机中央空调温控器（III）
            || item.light.model == ("air_hitachi_ac") //变频多联机中央空调温控器（IV）
            || item.light.model == ("air_iracc_central_ac") //变频多联机中央空调温控器（VI）
            || item.light.model == ("air_toshiba_ac") //变频多联机中央空调温控器（VII）
            || item.light.model == ("ir_air_conditioner") //红外空调
        ) {
            holder.autoWind.visibility = View.GONE
        } else {
            holder.autoWind.visibility = View.VISIBLE
        }

        var turnStatus = 0
        var mode = 0
        var windMode = 0
        var temperature = 0
        val value: String = item.light.`val`
        //紫光平台返回数据不确定原因，导致编码暂时只能这么处理
        //"val":1,6,7,25,12
        // 说明:val是,分割的多状态设备在线代表当前状态
        //第一位代表开关机,第二位代表模式,第三位风速,第四位温度,第五位风向
        //如上面所示:1代表开关机,6代表模式,7代表风速,25代表温度,12代表风向
        //        "val":1,6,7,25,12
        //说明:val是,分割的多状态设备在线代表当前状态
        //第一位代表开关机,第二位代表模式,第三位风速,第四位温度,第五位风向
        //如上面所示:1代表开关机,6代表模式,7代表风速,25代表温度,12代表风向
        if (value.contains(",")) {
            val devVal: List<String> = value.split(",")
            Log.i("leer", "AirConditioner devVal:" + devVal.size)
            when (devVal.size) {
                1 -> {
                    turnStatus = devVal[0].toInt()
                }
                2 -> {
                    turnStatus = devVal[0].toInt()
                    mode = devVal[1].toInt()
                }
                3 -> {
                    turnStatus = devVal[0].toInt()
                    mode = devVal[1].toInt()
                    windMode = devVal[2].toInt()
                }
                4 -> {
                    turnStatus = devVal[0].toInt()
                    mode = devVal[1].toInt()
                    windMode = devVal[2].toInt()
                    temperature = devVal[3].toInt()
                }
            }
        } else if (!TextUtils.isEmpty(value)) {
            turnStatus = value.toInt()
        }
        holder.switchLight.setOnClickListener {
            openSwitch(turnStatus, mode, windMode, temperature, item, holder.adapterPosition)
        }
        if (item.light.linkState != "1") {
            holder.text.isPressed = false
            holder.auto.isEnabled = false
            holder.auto.isPressed = false
            holder.refrigeration.isEnabled = false
            holder.refrigeration.isPressed = false
            holder.hot.isEnabled = false
            holder.hot.isPressed = false
            holder.water.isEnabled = false
            holder.water.isPressed = false
            holder.wind.isEnabled = false
            holder.wind.isPressed = false
            holder.lowWind.isEnabled = false
            holder.lowWind.setTextColor(
                getColor(
                    holder.itemView.context,
                    R.color.font_color_style_six
                )
            )

            holder.midWind.isEnabled = false
            holder.midWind.isPressed = false
            holder.midWind.setTextColor(
                getColor(
                    holder.itemView.context,
                    R.color.font_color_style_six
                )
            )

            holder.highWind.isEnabled = false
            holder.highWind.isPressed = false
            holder.highWind.setTextColor(
                getColor(
                    holder.itemView.context,
                    R.color.font_color_style_six
                )
            )

            holder.autoWind.isEnabled = false
            holder.autoWind.setTextColor(
                getColor(
                    holder.itemView.context,
                    R.color.font_color_style_six
                )
            )
            holder.dev.isEnabled = false
            holder.add.isEnabled = false
            holder.temperature.text = "0"
        } else {
            holder.text.isPressed = true
            holder.switchLight.isEnabled = true
            holder.auto.isEnabled = true
            holder.auto.setOnClickListener {
                auto(turnStatus, mode, windMode, temperature, item, holder.adapterPosition)
            }
            holder.refrigeration.isEnabled = true
            holder.refrigeration.setOnClickListener {
                refrigeration(turnStatus, mode, windMode, temperature, item, holder.adapterPosition)
            }
            holder.hot.isEnabled = true
            holder.hot.setOnClickListener {
                hot(turnStatus, mode, windMode, temperature, item, holder.adapterPosition)
            }
            holder.water.isEnabled = true
            holder.water.setOnClickListener {
                water(turnStatus, mode, windMode, temperature, item, holder.adapterPosition)
            }
            holder.wind.isEnabled = true
            holder.wind.setOnClickListener {
                wind(turnStatus, mode, windMode, temperature, item, holder.adapterPosition)
            }
            holder.lowWind.isEnabled = true
            holder.lowWind.setOnClickListener {
                lowWind(turnStatus, mode, windMode, temperature, item, holder.adapterPosition)
            }
            holder.midWind.isEnabled = true
            holder.midWind.setOnClickListener {
                midWind(turnStatus, mode, windMode, temperature, item, holder.adapterPosition)
            }
            holder.highWind.isEnabled = true
            holder.highWind.setOnClickListener {
                highWind(turnStatus, mode, windMode, temperature, item, holder.adapterPosition)
            }
            holder.autoWind.isEnabled = true
            holder.autoWind.setOnClickListener {
                autoWind(turnStatus, mode, windMode, temperature, item, holder.adapterPosition)
            }
            holder.switchLight.isChecked = turnStatus == 1
            holder.temperature.text = "$temperature"
            holder.add.isEnabled = true
            holder.add.setOnClickListener {
                addTemperature(
                    turnStatus,
                    mode,
                    windMode,
                    temperature,
                    item,
                    holder.adapterPosition
                )
            }

            holder.dev.isEnabled = true
            holder.dev.setOnClickListener {
                reduceTemperature(
                    turnStatus,
                    mode,
                    windMode,
                    temperature,
                    item,
                    holder.adapterPosition
                )
            }

            //红外空调特殊，只能限定几个温度值
            if (item.light.model == ("ir_air_conditioner")) {
                //4=22℃-制热-高风，5=24℃-制热-高风， 6=26℃-制热-低风，7=28℃-制热-高风，
                //8=22℃-制冷-高风，9=24℃-制冷-高风， 10=26℃-制冷-低风，13=28℃-制冷-高风
                //制冷
                holder.refrigeration.isPressed = mode == 8 || mode == 9 || mode == 10 || mode == 13
                holder.hot.isPressed = mode == 3 || mode == 4 || mode == 5 || mode == 6 || mode == 7
                if (windMode == 6 || windMode == 10) {
                    holder.lowWind.setTextColor(
                        getColor(
                            holder.itemView.context,
                            R.color.font_color_style_two
                        )
                    )
                } else {
                    holder.lowWind.setTextColor(
                        getColor(
                            holder.itemView.context,
                            R.color.font_color_style_six
                        )
                    )
                }


                if (windMode == 4 || windMode == 5 || windMode == 7 || windMode == 8 || windMode == 9 || windMode == 13) {
                    holder.highWind.setTextColor(
                        getColor(
                            holder.itemView.context,
                            R.color.font_color_style_two
                        )
                    )
                } else {
                    holder.highWind.setTextColor(
                        getColor(
                            holder.itemView.context,
                            R.color.font_color_style_six
                        )
                    )
                }
            } else {
                //红外码库空调、中央空调
                holder.refrigeration.isPressed = mode == 2
                holder.hot.isPressed = mode == 3
                holder.water.isPressed = mode == 4
                holder.auto.isPressed = mode == 5
                holder.auto.isPressed = mode == 5
                holder.wind.isPressed = mode == 6
                //低风
                if (windMode == 7) {
                    holder.lowWind.setTextColor(
                        getColor(
                            holder.itemView.context,
                            R.color.font_color_style_two
                        )
                    )
                } else {
                    holder.lowWind.setTextColor(
                        getColor(
                            holder.itemView.context,
                            R.color.font_color_style_six
                        )
                    )
                }
                //中风
                if (windMode == 8) {
                    holder.midWind.setTextColor(
                        getColor(
                            holder.itemView.context,
                            R.color.font_color_style_two
                        )
                    )
                } else {
                    holder.midWind.setTextColor(
                        getColor(
                            holder.itemView.context,
                            R.color.font_color_style_six
                        )
                    )
                }
                //强风
                if (windMode == 9) {
                    holder.highWind.setTextColor(
                        getColor(
                            holder.itemView.context,
                            R.color.font_color_style_two
                        )
                    )
                } else {
                    holder.highWind.setTextColor(
                        getColor(
                            holder.itemView.context,
                            R.color.font_color_style_six
                        )
                    )
                }
                //自动风
                if (windMode == 10) {
                    holder.autoWind.setTextColor(
                        getColor(
                            holder.itemView.context,
                            R.color.font_color_style_two
                        )
                    )
                } else {
                    holder.autoWind.setTextColor(
                        getColor(
                            holder.itemView.context,
                            R.color.font_color_style_six
                        )
                    )
                }
            }
        }
    }

    private fun autoWind(
        turnStatus: Int, mode: Int,
        windMode: Int,
        temperature: Int, item: AirConditioner, position: Int
    ) {
        if (turnStatus != 1) {
            ToastUtils.showLong("请先开启设备")
            return
        }

        if (mode == 10) {
            ToastUtils.showLong("当前风速已是自动风")
            return
        }
        val cmd = 10.toString()
        setStateAction(turnStatus, mode, windMode, temperature, cmd,
            MODE_AUTO_WIND, item.light, position)
    }

    private fun highWind(
        turnStatus: Int, mode: Int,
        windMode: Int,
        temperature: Int, item: AirConditioner, position: Int
    ) {
        if (turnStatus != 1) {
            ToastUtils.showLong("请先开启设备")
            return
        }

        if (mode == 9) {
            ToastUtils.showLong("当前风速已是强风")
            return
        }
        val cmd = 9.toString()
        setStateAction(turnStatus, mode, windMode, temperature, cmd,
            MODE_HIGH_WIND, item.light, position)
    }

    private fun midWind(
        turnStatus: Int, mode: Int,
        windMode: Int,
        temperature: Int, item: AirConditioner, position: Int
    ) {
        if (turnStatus != 1) {
            ToastUtils.showLong("请先开启设备")
            return
        }

        if (mode == 8) {
            ToastUtils.showLong("当前风速已是中风")
            return
        }
        val cmd = 8.toString()
        setStateAction(turnStatus, mode, windMode, temperature, cmd, MODE_MID_WIND, item.light, position)
    }

    private fun lowWind(
        turnStatus: Int, mode: Int,
        windMode: Int,
        temperature: Int, item: AirConditioner, position: Int
    ) {
        if (turnStatus != 1) {
            ToastUtils.showLong("请先开启设备")
            return
        }

        if (mode == 7) {
            ToastUtils.showLong("当前风速已是低风")
            return
        }
        val cmd = 7.toString()
        setStateAction(turnStatus, mode, windMode, temperature, cmd,
            MODE_LOW_WIND, item.light, position)
    }

    private fun wind(
        turnStatus: Int, mode: Int,
        windMode: Int,
        temperature: Int, item: AirConditioner, position: Int
    ) {
        if (turnStatus != 1) {
            ToastUtils.showLong("请先开启设备")
            return
        }

        if (mode == 6) {
            ToastUtils.showLong("当前已是送风模式")
            return
        }
        val cmd = 6.toString()
        setStateAction(turnStatus, mode, windMode, temperature, cmd, MODE_WIND, item.light, position)
    }

    private fun water(
        turnStatus: Int, mode: Int,
        windMode: Int,
        temperature: Int, item: AirConditioner, position: Int
    ) {
        if (turnStatus != 1) {
            ToastUtils.showLong("请先开启设备")
            return
        }

        if (mode == 4) {
            ToastUtils.showLong("当前已是除湿模式")
            return
        }
        val cmd = 4.toString()
        setStateAction(turnStatus, mode, windMode, temperature, cmd, MODE_WATER, item.light, position)
    }

    private fun hot(
        turnStatus: Int, mode: Int,
        windMode: Int,
        temperature: Int, item: AirConditioner, position: Int
    ) {
        if (turnStatus != 1) {
            ToastUtils.showLong("请先开启设备")
            return
        }

        if (mode == 3) {
            ToastUtils.showLong("当前已是制热模式")
            return
        }
        val cmd = 3.toString()
        setStateAction(turnStatus, mode, windMode, temperature, cmd, MODE_HOT, item.light, position)
    }

    private fun refrigeration(
        turnStatus: Int, mode: Int,
        windMode: Int,
        temperature: Int, item: AirConditioner, position: Int
    ) {
        if (turnStatus != 1) {
            ToastUtils.showLong("请先开启设备")
            return
        }

        if (mode == 2) {
            ToastUtils.showLong("当前已是制冷模式")
            return
        }
        val cmd = 2.toString()
        setStateAction(turnStatus, mode, windMode, temperature, cmd,
            MODE_REFRIGERATION, item.light, position)
    }

    private fun auto(
        turnStatus: Int, mode: Int,
        windMode: Int,
        temperature: Int, item: AirConditioner, position: Int
    ) {
        if (turnStatus != 1) {
            ToastUtils.showLong("请先开启设备")
            return
        }

        if (mode == 5) {
            ToastUtils.showLong("当前已是自动模式")
            return
        }
        val cmd = 5.toString()
        setStateAction(turnStatus, mode, windMode, temperature, cmd,MODE_AUTO_WIND, item.light, position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.text)
        val switchLight: SwitchCompat = itemView.findViewById(R.id.switchLight)
        val auto: AppCompatTextView = itemView.findViewById(R.id.auto)
        val refrigeration: AppCompatTextView = itemView.findViewById(R.id.refrigeration)
        val hot: AppCompatTextView = itemView.findViewById(R.id.hot)
        val water: AppCompatTextView = itemView.findViewById(R.id.water)
        val wind: AppCompatTextView = itemView.findViewById(R.id.wind)
        val lowWind: AppCompatTextView = itemView.findViewById(R.id.low_wind)
        val midWind: AppCompatTextView = itemView.findViewById(R.id.mid_wind)
        val highWind: AppCompatTextView = itemView.findViewById(R.id.high_wind)
        val autoWind: AppCompatTextView = itemView.findViewById(R.id.auto_wind)
        val temperature: AppCompatTextView = itemView.findViewById(R.id.temperature)
        val dev: AppCompatImageView = itemView.findViewById(R.id.dev)
        val add: AppCompatImageView = itemView.findViewById(R.id.add)
    }

    private fun openSwitch(
        turnStatus: Int, mode: Int,
        windMode: Int,
        temperature: Int, item: AirConditioner, position: Int
    ) {
        if (item.light.linkState != "1") {
            ToastUtils.showLong("设备离线")
            return
        }

        val cmd: String = if (turnStatus == 1) {
            "-1"
        } else {
            "1"
        }
        setStateAction(turnStatus, mode, windMode, temperature, cmd,
            TURN_STATUE, item.light, position)
    }


    private fun addTemperature(
        turnStatus: Int, mode: Int,
        windMode: Int,
        temperature: Int, item: AirConditioner, position: Int
    ) {
        if (turnStatus != 1) {
            ToastUtils.showLong("请先开启设备")
            return
        }

        if (temperature >= 30) {
            ToastUtils.showLong("已是最高温度")
            return
        }
        val cmd: String =
            if (temperature == 0) {
                "25"
            } else {
                (temperature + 1).toString()
            }
        setStateAction(turnStatus, mode, windMode, temperature, cmd,TEMPERATURE_ADD, item.light, position)
    }

    private fun reduceTemperature(
        turnStatus: Int, mode: Int,
        windMode: Int,
        temperature: Int, item: AirConditioner, position: Int
    ) {
        if (turnStatus != 1) {
            ToastUtils.showLong("请先开启设备")
            return
        }

        if (temperature <= 17) {
            ToastUtils.showLong("已是最低温度")
            return
        }

        val cmd: String =
            if (temperature == 0) {
                "25"
            } else {
                (temperature - 1).toString()
            }
        setStateAction(turnStatus, mode, windMode, temperature, cmd,TEMPERATURE_REDUCE, item.light, position)
    }


    private fun setStateAction(
        turnStatus: Int,
        mode: Int,
        windMode: Int,
        temperature: Int,
        cmd: String,
        type: Int,
        airConditioner: DevDto,
        position: Int
    ) {
        fragment?.showLoadingView()
        devComTask = UnisiotApiService.getInstance().devCom(
            AppConfig.SMART_HOME_SN,
            java.lang.String.valueOf(airConditioner.uuid),
            airConditioner.category,
            airConditioner.model,
            airConditioner.channel,
            cmd,
            object : CallBack<UnisiotResp>() {
                override fun callBack(result: UnisiotResp?) {
                    if(!ActivityUtils.isActivityAlive(activity)) {
                        return
                    }

                    if (result == null) {
                        ToastUtils.showLong("控制空调超时")
                        return
                    }
                    if (result.result == 0) {
                        when (result.result) {
                            0 -> {
                                var mode = 0
                                if (type == MODE_AUTO) {
                                    mode = 5
                                }

                                if (type == MODE_REFRIGERATION) {
                                    mode = 2
                                }

                                if (type == MODE_HOT) {
                                    mode = 3
                                }


                                if (type == MODE_WATER) {
                                    mode = 4
                                }
                                if (type == MODE_WIND) {
                                    mode = 6
                                }
                                if (type == MODE_LOW_WIND) {
                                    mode = 7
                                }
                                if (type == MODE_MID_WIND) {
                                    mode = 8
                                }

                                if (type == MODE_HIGH_WIND) {
                                    mode = 9
                                }
                                if (type == MODE_AUTO_WIND) {
                                    mode = 10
                                }
                                val newAirConditioner =
                                    DevDto.newBuilder(airConditioner)
                                        .setVal(
                                            "${if (turnStatus == 1) {
                                                "-1"
                                            } else {
                                                "1"
                                            }
                                            },${mode},${windMode},${if (temperature == 0) {
                                                "25"
                                            } else {
                                                (temperature).toString()
                                            }}"
                                        )
                                        .build()
                                adapter.items.toMutableList().apply {
                                    removeAt(position)
                                    add(position, AirConditioner(newAirConditioner))
                                    adapter.items = this
                                }
                                adapter.notifyItemChanged(position)

                                fragment?.showLoadingSuccess()
                            }
                            100 -> {
                                fragment?.showLoading()
                            }
                            else -> {
                                fragment?.showLoadingFail()
                            }
                        }
                    } else {
                        ToastUtils.showLong(result.msg)
                    }
                }
            })
    }

    companion object {
        const val TURN_STATUE = 1

        const val MODE_REFRIGERATION = 2
        const val MODE_AUTO = 3
        const val MODE_HOT = 4
        const val MODE_WATER = 5
        const val MODE_WIND = 6
        const val MODE_LOW_WIND = 7
        const val MODE_MID_WIND = 8
        const val MODE_HIGH_WIND = 9
        const val MODE_AUTO_WIND = 10

        const val TEMPERATURE_ADD = 4
        const val TEMPERATURE_REDUCE = 5
    }
}
