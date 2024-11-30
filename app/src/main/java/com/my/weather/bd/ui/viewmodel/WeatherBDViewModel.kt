package com.my.weather.bd.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.maps.model.LatLng
import com.my.weather.bd.core.base.Error
import com.my.weather.bd.core.base.Results
import com.my.weather.bd.core.base.Success
import com.my.weather.bd.coreandroid.BaseViewModel
import com.my.weather.bd.coreandroid.util.ControlledRunner
import com.my.weather.bd.datamodel.models.WeatherResponse
import com.my.weather.bd.datamodel.models.Zila
import com.my.weather.bd.domain.GetWeatherData
import com.my.weather.bd.domain.LoadZilaList
import com.my.weather.bd.domain.ParamGetWeatherByLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class WeatherBDViewModel @Inject constructor(
    private val getWeatherData: GetWeatherData,
    private val loadZilaList: LoadZilaList
) : BaseViewModel() {

    var weatherScreenState by mutableStateOf(HomeWeatherScreenState())
        private set

    var searchScreenState by mutableStateOf(SearchScreenState())
        private set

    private var controlledRunnerFetchRecords = ControlledRunner<Results<WeatherResponse>>()

    fun updateLocationPermissionGranted() {
        weatherScreenState = weatherScreenState.copy(locationPermissionRequired = false)
    }

    fun updateLocation(latLng: LatLng) {
        weatherScreenState = weatherScreenState.copy(currentLocation = latLng)
    }

    fun getZilaList() {
        uiScope.launch {
            searchScreenState = searchScreenState.copy(isLoading = true)
            val zilaList = loadZilaList(Any())
            searchScreenState = searchScreenState.copy(
                zilaList = zilaList,
                isLoading = false,
                loadZillaRequired = false
            )
        }
    }

    fun getWeatherByLocation(latitude: Double, longitude: Double) {
        uiScope.launch {
            weatherScreenState = weatherScreenState.copy(isLoading = true)

            when (val results = controlledRunnerFetchRecords.cancelPreviousThenRun {
                getWeatherData(
                    ParamGetWeatherByLocation(latitude, longitude)
                )
            }) {
                is Success -> {
                    weatherScreenState = weatherScreenState.copy(
                        weatherResponse = results.data,
                        isLoading = false
                    )
                }

                is Error -> {
                    weatherScreenState = weatherScreenState.copy(
                        isLoading = false,
                        exception = results.exception
                    )
                }
            }
        }
    }
}

data class HomeWeatherScreenState(
    val weatherResponse: WeatherResponse? = null,
    val isLoading: Boolean = false,
    val exception: Exception? = null,
    val currentLocation: LatLng? = null,
    val locationPermissionRequired: Boolean = true
)

data class SearchScreenState(
    val zilaList: ArrayList<Zila>? = null,
    val isLoading: Boolean = false,
    val loadZillaRequired: Boolean = true
)

data class LogData(val date: Date = Date(), val log: String)