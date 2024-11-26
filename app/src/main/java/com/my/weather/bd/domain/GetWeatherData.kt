package com.my.weather.bd.domain

import com.my.weather.bd.core.base.Error
import com.my.weather.bd.core.base.Results
import com.my.weather.bd.core.base.Success
import com.my.weather.bd.core.base.UseCase
import com.my.weather.bd.datamodel.models.WeatherResponse
import com.my.weather.bd.datamodel.repository.WeatherBDRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetWeatherData @Inject constructor(
    private val weatherBDRepository: WeatherBDRepository
) : UseCase<ParamGetWeatherByLocation, Results<WeatherResponse>>() {

    override suspend fun execute(parameters: ParamGetWeatherByLocation): Results<WeatherResponse> =
        withContext(Dispatchers.Default) {
            try {
                val response = weatherBDRepository.getWeatherData(
                    parameters.latitude,
                    parameters.longitude
                )
                Success(response)
            } catch (e: Exception) {
                Error(e)
            }
        }
}

data class ParamGetWeatherByLocation(
    val latitude: Double,
    val longitude: Double
)