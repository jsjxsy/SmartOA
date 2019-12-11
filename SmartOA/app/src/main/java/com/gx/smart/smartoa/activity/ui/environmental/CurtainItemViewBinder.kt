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
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
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
 * @author Drakeet Xu
 */
class CurtainItemViewBinder : ItemViewBinder<CurtainItem, CurtainItemViewBinder.TextHolder>() {

    private var devComTask: GrpcAsyncTask<String, Void, UnisiotResp>? = null
    var fragment: EnvironmentalControlFragment? = null

    class TextHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.text)
        val curtainGroup: RadioGroup = itemView.findViewById(R.id.curtainGroup)
        val open: AppCompatRadioButton = itemView.findViewById(R.id.open)
        val pause: AppCompatRadioButton = itemView.findViewById(R.id.pause)
        val close: AppCompatRadioButton = itemView.findViewById(R.id.close)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): TextHolder {
        return TextHolder(
            inflater.inflate(
                R.layout.item_environmental_control_curtain,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TextHolder, item: CurtainItem) {
        holder.text.text = item.light.devName

        holder.curtainGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.open -> setStateAction(1, item.light)
                R.id.close -> setStateAction(-1, item.light)
                R.id.pause -> setStateAction(2, item.light)
            }

        }

        holder.curtainGroup.setOnClickListener {
            when (item.light.linkState) {
                "0" -> {
                    ToastUtils.showLong("设备离线")
                    holder.curtainGroup.clearCheck()
                }

            }
        }

        when (item.light.linkState) {
            "1" -> {
                when (getCurtainState(item.light.`val`)) {
                    1 -> holder.open.isChecked = true
                    -1 -> holder.close.isChecked = true
                    2 -> holder.pause.isChecked = true
                }
                holder.text.isPressed = true
            }
            else -> {
                holder.text.isPressed = false
            }
        }

    }

    private fun getCurtainState(value: String): Int {
        var openSwitch = 0
        try {
            if (value.contains(",")) {
                val devVal: List<String> = value.split(",")
                if (devVal.isNotEmpty()) {
                    openSwitch = devVal[0].toInt()
                }
            } else if (!TextUtils.isEmpty(value)) {
                openSwitch = value.toInt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return openSwitch
    }


    private fun setStateAction(state: Int, curtain: DevDto) {
        val cmd = state.toString()
        devComTask = UnisiotApiService.getInstance().devCom(
            AppConfig.SMART_HOME_SN,
            java.lang.String.valueOf(curtain.uuid),
            curtain.category,
            curtain.model,
            curtain.channel,
            cmd,
            object : CallBack<UnisiotResp>() {
                override fun callBack(result: UnisiotResp?) {
                    if (result == null) {
                        ToastUtils.showLong("控制窗帘超时")
                        return
                    }
                    if (result.result == 0) {
                        when (result.result) {
                            0 -> {
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

}
