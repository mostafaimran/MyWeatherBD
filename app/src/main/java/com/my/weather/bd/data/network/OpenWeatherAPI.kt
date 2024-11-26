package com.my.weather.bd.data.network

import com.my.weather.bd.BuildConfig
import com.my.weather.bd.datamodel.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherAPI {
    @GET(ApiEndPoints.API_GET_WEATHER_DATA)
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ): WeatherResponse

}