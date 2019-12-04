package com.gx.smart.smartoa.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.protobuf.ProtoConverterFactory

object ServiceCreator {

    private const val BASE_URL = "http://guolin.tech/"

    private val httpClient = OkHttpClient.Builder()

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient.build())
        .addConverterFactory(ProtoConverterFactory.create())


    private val retrofit = builder.build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

}