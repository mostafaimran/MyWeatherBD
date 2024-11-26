package com.my.weather.bd.data.repository

import android.util.Log
import com.my.weather.bd.data.network.OpenWeatherAPI
import com.my.weather.bd.datamodel.exceptions.LocalException
import com.my.weather.bd.datamodel.ext.convertNetworkSpecificException
import com.my.weather.bd.datamodel.models.WeatherResponse
import com.my.weather.bd.datamodel.repository.WeatherBDRepository
import javax.inject.Inject

class WeatherBDRepositoryImpl @Inject constructor(private val openWeatherAPI: OpenWeatherAPI) :
    WeatherBDRepository {

    override suspend fun getWeatherData(lat: Double, long: Double): WeatherResponse {
        return try {
            val result = openWeatherAPI.getWeather(lat, long)
            if (result.cod == 200)
                result
            else
                throw Exception(LocalException("somethins is wrong"))
        } catch (e: Exception) {
            Log.e("WeatherBDRepositoryImpl", "getWeatherData: ", e)
            throw e.convertNetworkSpecificException()
        }
    }
}