package com.yuseon.mumu.model

import com.google.gson.Gson

class Repository {
    val api : ApiService = RetrofitObject().apiService

    fun getMockData() : MainDataModel{
        val gson = Gson()
        return gson.fromJson(mockDataString, MainDataModel::class.java)
    }

    suspend fun getData() : MainDataModel{
        return api.getUser();
    }
}