package com.my.weather.bd.datamodel.repository

import com.my.weather.bd.datamodel.models.WeatherResponse

interface WeatherBDRepository {
    suspend fun getWeatherData(lat: Double, long: Double): WeatherResponse
}