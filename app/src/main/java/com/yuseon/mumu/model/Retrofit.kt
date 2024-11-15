package com.yuseon.mumu.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class RetrofitObject {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://meta.musinsa.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)
}

interface ApiService {
    @GET("interview/list.json")
    suspend fun getUser(): MainDataModel // 콜 객체
}