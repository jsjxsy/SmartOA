package com.gx.smart.smartoa.activity.ui.environmental

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
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
class FreshAirViewBinder : ItemViewBinder<FreshAir, FreshAirViewBinder.ViewHolder>() {

    private var devComTask: GrpcAsyncTask<String, Void, UnisiotResp>? = null
    var fragment: EnvironmentalControlFragment? = null


    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        val root = inflater.inflate(R.layout.item_environmental_control_fresh_air, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: FreshAir) {
        holder.text.text = item.light.devName
        var openSwitch = 0
        var mode = 0
        val value = item.light.`val`
        try {
            if (value.contains(",")) { //val:1,4,9,23,45,42,44,,,,,,
                val devVal: List<String> = value.split(",")
                if (devVal.size == 1) {
                    openSwitch = devVal[0].toInt()
                } else if (devVal.size >= 3) {
                    openSwitch = devVal[0].toInt()
                    mode = devVal[2].toInt()
                }
            } else if (!TextUtils.isEmpty(value)) {
                openSwitch = value.toInt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (item.light.linkState != "1") {
            holder.switchLight.isChecked = false
            holder.text.isPressed = false
            holder.wind.clearCheck()
        } else {
            holder.text.isPressed = true
            holder.switchLight.isChecked = (openSwitch == 1)
            holder.lowWind.isChecked = (mode == 7)
            holder.highWind.isChecked = (mode == 9)
        }
        holder.switchLight.setOnClickListener {
            openSwitch(openSwitch, mode, item, holder.adapterPosition)
        }
        holder.lowWind.setOnClickListener {
            lowWind(openSwitch, mode, item, holder.adapterPosition)
        }

        holder.highWind.setOnClickListener {
            highWind(openSwitch, mode, item, holder.adapterPosition)
        }
    }


    private fun openSwitch(turnStatus: Int, mode: Int, item: FreshAir, position: Int) {
        if (item.light.linkState != "1") {
            ToastUtils.showLong("设备离线")
            return
        }

        val cmd: String = if (turnStatus == 1) {
            "-1"
        } else {
            "1"
        }
        setStateAction(turnStatus, mode, cmd, TURN_STATUE, item.light, position)
    }


    private fun lowWind(turnStatus: Int, mode: Int, item: FreshAir, position: Int) {
        if (turnStatus != 1) {
            ToastUtils.showLong("请先开启设备")
            return
        }

        if (mode == 7) {
            ToastUtils.showLong("当前风速已是低风")
            return
        }
        val cmd = 7.toString()
        setStateAction(turnStatus, mode, cmd, MODE_LOW_WIND, item.light, position)
    }

    private fun highWind(turnStatus: Int, mode: Int, item: FreshAir, position: Int) {
        if (turnStatus != 1) {
            ToastUtils.showLong("请先开启设备")
            return
        }

        if (mode == 9) {
            ToastUtils.showLong("当前风速已是强风")
            return
        }
        val cmd = 9.toString()
        setStateAction(turnStatus, mode, cmd, MODE_HIGH_WIND, item.light, position)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.text)
        val switchLight: SwitchCompat = itemView.findViewById(R.id.switchLight)
        val lowWind: AppCompatRadioButton = itemView.findViewById(R.id.lowWind)
        val highWind: AppCompatRadioButton = itemView.findViewById(R.id.highWind)
        val wind: RadioGroup = itemView.findViewById(R.id.wind)
    }


    private fun setStateAction(
        openSwitch: Int,
        mode: Int, cmd: String, type: Int, freshAir: DevDto, position: Int
    ) {
        fragment?.showLoadingView()
        devComTask = UnisiotApiService.getInstance().devCom(
            AppConfig.SMART_HOME_SN,
            java.lang.String.valueOf(freshAir.uuid),
            freshAir.category,
            freshAir.model,
            freshAir.channel,
            cmd,
            object : CallBack<UnisiotResp>() {
                override fun callBack(result: UnisiotResp?) {
                    if (result == null) {
                        ToastUtils.showLong("控制新风超时")
                        return
                    }
                    if (result.result == 0) {
                        when (result.result) {
                            0 -> {
                                var mode = 0
                                if (type == MODE_LOW_WIND) {
                                    mode = 7
                                }

                                if (type == MODE_HIGH_WIND) {
                                    mode = 9
                                }

                                fragment?.showLoadingSuccess()
                                val newFreshAir =
                                    DevDto.newBuilder(freshAir).setVal(
                                        "${if (openSwitch == 1) {
                                            "-1"
                                        } else {
                                            "1"
                                        }},$mode"
                                    ).build()
                                adapter.items.toMutableList().apply {
                                    removeAt(position)
                                    add(position, FreshAir(newFreshAir))
                                    adapter.items = this
                                }
                                adapter.notifyItemChanged(position)
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

        const val MODE_LOW_WIND = 7
        const val MODE_HIGH_WIND = 9
    }
}
