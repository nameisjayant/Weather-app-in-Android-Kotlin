package com.codingwithjks.weatherapp.Network

import com.codingwithjks.weatherapp.Model.City
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather/")
    suspend fun getCityData(
        @Query("q") q:String,
        @Query("appid") appId:String
    ):City
}