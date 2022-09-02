package com.codingwithjks.weatherapp.Model

data class City(
    val weather:List<Weather>,
    val main:Main,
    val wind:Wind,
    val name:String = ""
) {
}

