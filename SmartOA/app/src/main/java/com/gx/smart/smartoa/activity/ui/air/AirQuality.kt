package com.gx.smart.smartoa.activity.ui.air

/**
 * @author xiaosy
 * @create 2019-11-08
 * @Describe
 * //温度，湿度，光照，pm2.5,co2,甲醛
 */

data class AirQuality(
    var temperature: String?,
    var humidity: String?,
    var pm: String?,
    var co2: String?
)
