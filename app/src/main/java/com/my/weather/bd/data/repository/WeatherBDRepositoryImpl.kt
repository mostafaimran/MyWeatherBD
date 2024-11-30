package com.my.weather.bd.data.repository

import android.content.Context
import android.util.Log
import com.my.weather.bd.core.JsonConverter
import com.my.weather.bd.data.Constants
import com.my.weather.bd.data.network.OpenWeatherAPI
import com.my.weather.bd.datamodel.exceptions.ServerException
import com.my.weather.bd.datamodel.ext.convertNetworkSpecificException
import com.my.weather.bd.datamodel.models.WeatherResponse
import com.my.weather.bd.datamodel.models.Zila
import com.my.weather.bd.datamodel.repository.WeatherBDRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject


class WeatherBDRepositoryImpl @Inject constructor(
    private val context: Context,
    private val jsonConverter: JsonConverter,
    private val openWeatherAPI: OpenWeatherAPI
) : WeatherBDRepository {

    override suspend fun getWeatherData(lat: Double, long: Double): WeatherResponse {
        return try {
            val result = openWeatherAPI.getWeather(lat, long)
            if (result.cod == 200)
                result
            else
                throw Exception(ServerException(result.message ?: "something is wrong"))
        } catch (e: Exception) {
            Log.e("WeatherBDRepositoryImpl", "getWeatherData: ", e)
            throw e.convertNetworkSpecificException()
        }
    }

    override suspend fun getZillaList(): ArrayList<Zila>? = withContext(Dispatchers.IO) {
        try {
            val inputStream: InputStream = context.assets.open(Constants.ZILA_LIST_ASSET_FILE)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, charset("UTF-8"))
            if (json.isNotEmpty()) {
                val zilaList = jsonConverter.fromJsontoArrayList(json, Zila::class.java)
                return@withContext zilaList
            } else
                return@withContext null
        } catch (ex: IOException) {
            ex.printStackTrace()
            return@withContext null
        }
    }
}