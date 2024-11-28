package com.my.weather.bd.ui.screen

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
import com.my.weather.bd.domain.GetWeatherData
import com.my.weather.bd.domain.ParamGetWeatherByLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class WeatherBDViewModel @Inject constructor(
    private val getWeatherData: GetWeatherData
) : BaseViewModel() {

    var uiState by mutableStateOf(HomeWeatherScreenState())
        private set

    private var controlledRunnerFetchRecords = ControlledRunner<Results<WeatherResponse>>()

    fun updateLocationPermissionGranted() {
        uiState = uiState.copy(locationPermissionRequired = false)
    }

    fun updateLocation(latLng: LatLng) {
        uiState = uiState.copy(currentLocation = latLng)
    }

    fun getWeatherByLocation(latitude: Double, longitude: Double) {
        uiScope.launch {
            uiState = uiState.copy(isLoading = true)

            when (val results = controlledRunnerFetchRecords.cancelPreviousThenRun {
                getWeatherData(
                    ParamGetWeatherByLocation(latitude, longitude)
                )
            }) {
                is Success -> {
                    uiState = uiState.copy(
                        weatherResponse = results.data,
                        isLoading = false
                    )
                }

                is Error -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        exception = results.exception
                    )
                }
            }
        }
    }

    fun exceptionHandled() {
        uiState = uiState.copy(exception = null)
    }
}

data class HomeWeatherScreenState(
    val weatherResponse: WeatherResponse? = null,
    val isLoading: Boolean = false,
    val exception: Exception? = null,
    val currentLocation: LatLng? = null,
    val locationPermissionRequired: Boolean = true
)

data class LogData(val date: Date = Date(), val log: String)