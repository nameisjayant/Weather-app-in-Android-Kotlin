package com.codingwithjks.weatherapp.ViewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingwithjks.weatherapp.Model.City
import com.codingwithjks.weatherapp.Repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository): ViewModel() {
    val weatherResponse:MutableLiveData<City> = MutableLiveData()
    @ExperimentalCoroutinesApi
    private val searchChannel = ConflatedBroadcastChannel<String>()

    @ExperimentalCoroutinesApi
    fun setSearchQuery(search:String)
    {
        searchChannel.offer(search)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun getCityData()
    {
        viewModelScope.launch {
            searchChannel.asFlow()
                .flatMapLatest { search->
                    weatherRepository.getCityData(search)
                }.catch {e->
                    Log.d("main", "${e.message}")
                }.collect { response->
                    weatherResponse.value=response
                }
        }
    }


}