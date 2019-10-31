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

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R

/**
 * @author Drakeet Xu
 */
class LightItemTwoViewBinder : ItemViewBinder<LightItemTwo, LightItemTwoViewBinder.TextHolder>() {

  class TextHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val text: TextView = itemView.findViewById(R.id.text)
  }

  override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): TextHolder {
    return TextHolder(inflater.inflate(R.layout.item_environmental_control_light_panel_text_view, parent, false))
  }

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: TextHolder, item: LightItemTwo) {
    holder.text.text = "hello: " + item.text
  }

}
