package com.my.weather.bd.domain

import com.my.weather.bd.core.base.UseCase
import com.my.weather.bd.datamodel.models.Zila
import com.my.weather.bd.datamodel.repository.WeatherBDRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadZilaList @Inject constructor(
    private val weatherBDRepository: WeatherBDRepository
) : UseCase<Any, ArrayList<Zila>?>() {

    override suspend fun execute(parameters: Any): ArrayList<Zila>? =
        withContext(Dispatchers.Default) {
            try {
                weatherBDRepository.getZillaList()
            } catch (e: Exception) {
                null
            }
        }
}
