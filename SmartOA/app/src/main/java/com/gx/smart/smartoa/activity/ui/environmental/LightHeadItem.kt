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

import com.google.gson.Gson
import com.gx.smart.smartoa.activity.ui.Savable
import java.nio.charset.Charset

/**
 * @author Drakeet Xu
 */
class LightHeadItem : Savable {

    lateinit var text: String
     var resId: Int = -1

    constructor(text: String, resId: Int) {

        this.text = text
        this.resId = resId
    }

    constructor(data: ByteArray) {
        init(data)
    }

    override fun init(data: ByteArray) {
        val json = String(data, UTF_8)
        this.text = Gson().fromJson(json, LightHeadItem::class.java).text
        this.resId = Gson().fromJson(json, LightHeadItem::class.java).resId
    }

    override fun toBytes(): ByteArray {
        return Gson().toJson(this).toByteArray(UTF_8)
    }

    override fun describe(): String {
        return "Text"
    }

    companion object {
        private val UTF_8 = Charset.forName("UTF-8")
    }
}
