package com.codingwithjks.weatherapp.Repository

import com.codingwithjks.weatherapp.Model.City
import com.codingwithjks.weatherapp.Network.ApiServiceImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apiServiceImp: ApiServiceImp) {

    fun getCityData(city:String):Flow<City> = flow {
        val response= apiServiceImp.getCity(city,"2477aaa507b993535ef2dcaa8b1f1c02")
        emit(response)
    }.flowOn(Dispatchers.IO)
        .conflate()
}