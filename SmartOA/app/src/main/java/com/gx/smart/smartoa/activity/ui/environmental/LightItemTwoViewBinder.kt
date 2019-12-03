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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R

/**
 * @author Drakeet Xu
 */
class LightItemTwoViewBinder : ItemViewBinder<LightItemTwo, LightItemTwoViewBinder.TextHolder>() {

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
        holder.text.text = item.text
        holder.switchLight.setOnCheckedChangeListener { _, isChecked ->
            holder.text.isPressed = isChecked
        }
        holder.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

    }

}
