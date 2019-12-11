package com.gx.smart.smartoa.activity.ui.environmental

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R

/**
 * @author xiaosy
 * @create 2019-10-31
 * @Describe
 */
class AirConditionerViewBinder :
    ItemViewBinder<AirConditioner, AirConditionerViewBinder.ViewHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        val root =
            inflater.inflate(R.layout.item_environmental_control_air_conditioner, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: AirConditioner) {
        holder.text.text = item.light.devName
        holder.switchLight.setOnCheckedChangeListener { _, isChecked ->
            holder.text.isPressed = isChecked
        }

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
            holder.mid_wind.visibility = View.GONE
        } else {
            holder.mid_wind.visibility = View.VISIBLE
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
            holder.auto_wind.visibility = View.GONE
        } else {
            holder.auto_wind.visibility = View.VISIBLE
        }

        var turnStatus = 0
        var mode = 0
        var windMode = 0
        var temperature = 0
        val value: String = item.light.`val` //紫光平台返回数据不确定原因，导致编码暂时只能这么处理

//        "val":1,6,7,25,12
//        说明:val是,分割的多状态设备在线代表当前状态
//        第一位代表开关机,第二位代表模式,第三位风速,第四位温度,第五位风向
//        如上面所示:1代表开关机,6代表模式,7代表风速,25代表温度,12代表风向
        //        "val":1,6,7,25,12
//        说明:val是,分割的多状态设备在线代表当前状态
//        第一位代表开关机,第二位代表模式,第三位风速,第四位温度,第五位风向
//        如上面所示:1代表开关机,6代表模式,7代表风速,25代表温度,12代表风向
        try {
            Log.i("leer", "ZGAirConPanelAdapter convert  val:$value")
            if (value.contains(",")) {
                val devVal: List<String> = value.split(",")
                Log.i("leer", "AirConditioner devVal:" + devVal.size)
                if (devVal.size == 1) {
                    turnStatus = devVal[0].toInt()
                } else if (devVal.size == 2) {
                    turnStatus = devVal[0].toInt()
                    mode = devVal[1].toInt()
                } else if (devVal.size == 3) {
                    turnStatus = devVal[0].toInt()
                    mode = devVal[1].toInt()
                    windMode = devVal[2].toInt()
                } else if (devVal.size == 4) {
                    turnStatus = devVal[0].toInt()
                    mode = devVal[1].toInt()
                    windMode = devVal[2].toInt()
                    temperature = devVal[3].toInt()
                }
            } else if (!TextUtils.isEmpty(value)) {
                turnStatus = value.toInt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (item.light.linkState != "1") {
            holder.switchLight.isChecked = false
            holder.switchLight.isEnabled = false
            holder.auto.isEnabled = false
            holder.refrigeration.isEnabled = false
            holder.hot.isEnabled = false
            holder.water.isEnabled = false
            holder.wind.isEnabled = false
            holder.low_wind.isEnabled = false
            holder.mid_wind.isEnabled = false
            holder.high_wind.isEnabled = false
            holder.auto_wind.isEnabled = false
        } else {
            holder.switchLight.isChecked = true
            holder.switchLight.isEnabled = true
            holder.auto.isEnabled = true
            holder.refrigeration.isEnabled = true
            holder.hot.isEnabled = true
            holder.water.isEnabled = true
            holder.wind.isEnabled = true
            holder.low_wind.isEnabled = true
            holder.mid_wind.isEnabled = true
            holder.high_wind.isEnabled = true
            holder.auto_wind.isEnabled = true
            holder.switchLight.isChecked = turnStatus == 1
            holder.temperature.text = "$temperature"
            if (item.light.model == ("ir_air_conditioner")) { //红外空调特殊，只能限定几个温度值
//                4=22℃-制热-高风，5=24℃-制热-高风， 6=26℃-制热-低风，7=28℃-制热-高风，
//                8=22℃-制冷-高风，9=24℃-制冷-高风， 10=26℃-制冷-低风，13=28℃-制冷-高风
//制冷
                holder.refrigeration.isPressed = mode == 8 || mode == 9 || mode == 10 || mode == 13
                holder.hot.isPressed = mode == 3 || mode == 4 || mode == 5 || mode == 6 || mode == 7
                holder.low_wind.isPressed = windMode == 6 || windMode == 10
                holder.high_wind.isPressed =
                    windMode == 4 || windMode == 5 || windMode == 7 || windMode == 8 || windMode == 9 || windMode == 13

            } else { //红外码库空调、中央空调
                holder.refrigeration.isPressed = mode == 2
                holder.hot.isPressed = mode == 3
                holder.water.isPressed = mode == 4
                holder.auto.isPressed = mode == 5
                holder.auto.isPressed = mode == 5
                holder.wind.isPressed = mode == 6
                //低风
                holder.low_wind.isPressed = windMode == 7
                //中风

                holder.mid_wind.isPressed = windMode == 9
                //强风
                holder.high_wind.isPressed = windMode == 9
                //自动风
                holder.auto_wind.isPressed = windMode == 10
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.text)
        val switchLight: SwitchCompat = itemView.findViewById(R.id.switchLight)
        val auto: AppCompatTextView = itemView.findViewById(R.id.auto)
        val refrigeration: AppCompatTextView = itemView.findViewById(R.id.refrigeration)
        val hot: AppCompatTextView = itemView.findViewById(R.id.hot)
        val water: AppCompatTextView = itemView.findViewById(R.id.water)
        val wind: AppCompatTextView = itemView.findViewById(R.id.wind)
        val low_wind: AppCompatTextView = itemView.findViewById(R.id.low_wind)
        val mid_wind: AppCompatTextView = itemView.findViewById(R.id.mid_wind)
        val high_wind: AppCompatTextView = itemView.findViewById(R.id.high_wind)
        val auto_wind: AppCompatTextView = itemView.findViewById(R.id.auto_wind)
        val temperature: AppCompatTextView = itemView.findViewById(R.id.temperature)
    }
}
