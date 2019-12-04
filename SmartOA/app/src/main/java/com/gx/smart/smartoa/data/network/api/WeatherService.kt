package com.gx.smart.smartoa.data.network.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("api/weather")
    suspend fun getWeather(@Query("cityid") weatherId: String): Call<String>

    @GET("api/bing_pic")
    suspend fun getBingPic(): Call<String>

}