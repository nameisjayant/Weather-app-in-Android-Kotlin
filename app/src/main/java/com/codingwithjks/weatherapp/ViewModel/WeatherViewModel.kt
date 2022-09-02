package com.codingwithjks.weatherapp.ViewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingwithjks.weatherapp.Model.City
import com.codingwithjks.weatherapp.Model.Main
import com.codingwithjks.weatherapp.Model.Weather
import com.codingwithjks.weatherapp.Model.Wind
import com.codingwithjks.weatherapp.Repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalCoroutinesApi
@FlowPreview
@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {
    private val _weatherResponse: MutableStateFlow<City> = MutableStateFlow(
        City(
            listOf(Weather()),
            Main(),
            Wind()
        )
    )
    val weatherResponse: StateFlow<City> = _weatherResponse
    private val searchChannel = MutableSharedFlow<String>(1)


    fun setSearchQuery(search: String) {
        searchChannel.tryEmit(search)
    }

    init {
        getCityData()
    }


    private fun getCityData() {
        viewModelScope.launch {
            searchChannel
                .flatMapLatest { search ->
                    weatherRepository.getCityData(search)
                }.catch { e ->
                    Log.d("main", "${e.message}")
                }.collect { response ->
                    _weatherResponse.value = response
                }
        }
    }


}