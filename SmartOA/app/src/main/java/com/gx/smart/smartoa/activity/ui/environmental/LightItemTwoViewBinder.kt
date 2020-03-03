/*
 * Copyright (c) 2016-present. Drakeet Xu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package  com.gx.smart.smartoa.activity.ui.environmental

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R
import com.gx.smart.common.AppConfig
import com.gx.smart.smartoa.data.network.api.UnisiotApiService
import com.gx.smart.lib.http.base.CallBack
import com.gx.smart.lib.http.base.GrpcAsyncTask
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevDto
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.UnisiotResp

/**
 * @author Drakeet Xu
 */
class LightItemTwoViewBinder : ItemViewBinder<LightItemTwo, LightItemTwoViewBinder.TextHolder>() {

    private var devComTask: GrpcAsyncTask<String, Void, UnisiotResp>? = null
    var fragment: EnvironmentalControlFragment? = null

    class TextHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.text)
        val switchLight: SwitchCompat = itemView.findViewById(R.id.switchLight)
        val seekBar: AppCompatSeekBar = itemView.findViewById(R.id.seekBar)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): TextHolder {
        return TextHolder(
            inflater.inflate(
                R.layout.item_environmental_control_light_panel_text_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TextHolder, item: LightItemTwo) {
        holder.text.text = item.light.devName
        val statsValue = getLightStatus(item.light.`val`)
        holder.switchLight.isChecked = (statsValue == 1)
        when (item.light.linkState) {
            "0" -> holder.text.isPressed = false
            "1" -> holder.text.isPressed = true
        }

        holder.switchLight.setOnClickListener {
            when (item.light.linkState) {
                "0" -> ToastUtils.showLong("设备离线")
                "1" -> {
                    fragment?.showLoadingView()
                    switchLightAction(statsValue, item.light, holder.adapterPosition)
                }
            }
        }
        holder.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if (statsValue != 1) {
                    ToastUtils.showLong("请先开启设备")
                }
            }

            var mControlValue = 0
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress = seekBar!!.progress
                if (statsValue != 1) {
                    ToastUtils.showLong("请先开启设备")
                    return
                }
                fragment?.showLoadingView()
                if (!(progress < 0 || progress > 5)) {
                    mControlValue = 0
                } else if (!(progress <= 5 || progress > 15)) {
                    mControlValue = 10
                } else if (!(progress <= 15 || progress > 25)) {
                    mControlValue = 20
                } else if (!(progress <= 25 || progress > 35)) {
                    mControlValue = 30
                } else if (!(progress <= 35 || progress > 45)) {
                    mControlValue = 40
                } else if (!(progress <= 45 || progress > 55)) {
                    mControlValue = 50
                } else if (!(progress <= 55 || progress > 65)) {
                    mControlValue = 60
                } else if (!(progress <= 65 || progress > 75)) {
                    mControlValue = 70
                } else if (!(progress <= 75 || progress > 85)) {
                    mControlValue = 80
                } else if (!(progress <= 85 || progress > 95)) {
                    mControlValue = 90
                } else if (!(progress <= 95 || progress > 100)) {
                    mControlValue = 100
                }
                val cmd: String = mControlValue.toString()
                val smartSN = SPUtils.getInstance().getString(AppConfig.SMART_HOME_SN, "")
                devComTask = UnisiotApiService.getInstance().devCom(
                    smartSN,
                    java.lang.String.valueOf(item.light.uuid),
                    item.light.category,
                    item.light.model,
                    item.light.channel,
                    cmd,
                    object : CallBack<UnisiotResp>() {
                        override fun callBack(result: UnisiotResp?) {
                            if(!ActivityUtils.isActivityAlive(fragment?.activity)) {
                                return
                            }

                            if (result == null) {
                                ToastUtils.showLong("控制灯光超时")
                                fragment?.showLoadingFail()
                                return
                            }
                            if (result.code == 100) {
                                when (result.result) {
                                    0 -> {
                                        fragment?.showLoadingSuccess()
                                        val newLight = DevDto.newBuilder(item.light)
                                            .setVal("$statsValue,$cmd").build()
                                        adapter.items.toMutableList().apply {
                                            removeAt(holder.adapterPosition)
                                            add(holder.adapterPosition, LightItemTwo(newLight))
                                            adapter.items = this
                                        }
                                        adapter.notifyItemChanged(holder.adapterPosition)
                                    }
                                    100 -> {
                                        fragment?.showLoading()
                                    }
                                    else -> {
                                        fragment?.showLoadingFail()
                                    }
                                }
                            } else {
                                val msg = result.msg
                                ToastUtils.showLong(msg)
                                fragment?.showLoadingFail()
                            }
                        }
                    })
            }

        })
        holder.seekBar.progress = getLightValue(item.light.`val`)

    }


    private fun getLightValue(value: String): Int {
        var lightValue = 0
        if (value.contains(",")) {
            val devVal: List<String> = value.split(",")
            if (devVal.size >= 2) {
                if (devVal[1].isNotEmpty()) {
                    lightValue = devVal[1].toInt()
                }
            }
        }
        return lightValue
    }


    private fun getLightStatus(value: String): Int {
        var turnStatus = 0
        if (value.contains(",")) {
            val devVal: List<String> = value.split(",")
            if (devVal.size == 1) {
                if (devVal[0].isNotEmpty()) {
                    turnStatus = devVal[0].toInt()
                }
            } else if (devVal.size >= 2) {
                if (devVal[0].isNotEmpty()) {
                    turnStatus = devVal[0].toInt()
                }
            }
        } else if (!TextUtils.isEmpty(value)) {
            if (value.isNotEmpty()) {
                val valueNew = value.replace("0x", "")
                turnStatus = if (valueNew.contains("x")) {
                    valueNew.toInt(16)
                } else {
                    valueNew.toInt()
                }
            }
        }
        return turnStatus
    }

    private fun switchLightAction(
        finalTurnStatus: Int,
        light: DevDto,
        position: Int
    ) {
        val cmd = if (finalTurnStatus == 1) "-1" else "1"
        val smartSN = SPUtils.getInstance().getString(AppConfig.SMART_HOME_SN, "")
        devComTask = UnisiotApiService.getInstance().devCom(
            smartSN,
            java.lang.String.valueOf(light.uuid),
            light.category,
            light.model,
            light.channel,
            cmd,
            object : CallBack<UnisiotResp>() {
                override fun callBack(result: UnisiotResp?) {
                    if(!ActivityUtils.isActivityAlive(fragment?.activity)) {
                        return
                    }

                    if (result == null) {
                        ToastUtils.showLong("控制灯光超时")
                        return
                    }
                    if (result.code == 100) {
                        when (result.result) {
                            0 -> {
                                val newLight = DevDto.newBuilder(light).setVal(cmd).build()
                                adapter.items.toMutableList().apply {
                                    removeAt(position)
                                    add(position, LightItemTwo(newLight))
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
                        val msg = result.msg
                        ToastUtils.showLong(msg)
                    }
                }
            })
    }

}
