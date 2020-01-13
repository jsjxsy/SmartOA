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
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
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
 * @author Drakeet Xu
 */
class LightItemOneViewBinder : ItemViewBinder<LightItemOne, LightItemOneViewBinder.TextHolder>() {

    private var devComTask: GrpcAsyncTask<String, Void, UnisiotResp>? = null
    var fragment: EnvironmentalControlFragment? = null

    class TextHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.text)
        val switchLightPanel: SwitchCompat = itemView.findViewById(R.id.switchLightPanel)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): TextHolder {
        return TextHolder(
            inflater.inflate(
                R.layout.item_environmental_control_light_text_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TextHolder, item: LightItemOne) {
        holder.adapterPosition
        holder.text.text = item.light.devName
        when (item.light.linkState) {
            "0" -> holder.text.isPressed = false
            "1" -> holder.text.isPressed = true
        }
        var statsValue = getLightStatus(item.light.`val`)
        var status = (statsValue == 1)
        holder.switchLightPanel.isChecked = status
        holder.switchLightPanel.setOnClickListener {
            when (item.light.linkState) {
                "0" -> ToastUtils.showLong("设备离线")
                "1" -> {
                    fragment?.showLoadingView()
                    switchLightAction(statsValue, item, getPosition(holder))
                }
            }
        }
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
        item: LightItemOne,
        position: Int
    ) {
        val cmd = if (finalTurnStatus == 1) "-1" else "1"
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
                        return
                    }
                    if (result.code == 100) {
                        when (result.result) {
                            0 -> {
                                fragment?.showLoadingSuccess()
                                val newLight = DevDto.newBuilder(item.light).setVal(cmd).build()
                                adapter.items.toMutableList().apply {
                                    removeAt(position)
                                    add(position, LightItemOne(newLight))
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
                        val msg = result.msg
                        ToastUtils.showLong(msg)
                    }
                }
            })
    }

}
