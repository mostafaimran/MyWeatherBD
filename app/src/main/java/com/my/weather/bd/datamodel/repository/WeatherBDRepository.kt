package com.my.weather.bd.datamodel.repository

import com.my.weather.bd.datamodel.models.WeatherResponse
import com.my.weather.bd.datamodel.models.Zila

interface WeatherBDRepository {
    suspend fun getWeatherData(lat: Double, long: Double): WeatherResponse
    suspend fun getZillaList(): ArrayList<Zila>?
}