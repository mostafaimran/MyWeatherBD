package com.my.weather.bd.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.my.weather.bd.core.base.Error
import com.my.weather.bd.core.base.Results
import com.my.weather.bd.core.base.Success
import com.my.weather.bd.coreandroid.BaseViewModel
import com.my.weather.bd.coreandroid.util.ControlledRunner
import com.my.weather.bd.coreandroid.util.SingleEvent
import com.my.weather.bd.datamodel.models.Location
import com.my.weather.bd.datamodel.models.WeatherResponse
import com.my.weather.bd.domain.GetWeatherData
import com.my.weather.bd.domain.ParamGetWeatherByLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt

@HiltViewModel
class WeatherBDViewModel @Inject constructor(
    private val getWeatherData: GetWeatherData
) : BaseViewModel() {

    var uiState by mutableStateOf(HomeWeatherScreenState())
        private set

    val lvApiIssue = MutableLiveData<SingleEvent<Exception>>()

    val logDataList = ArrayList<LogData>()

    private var controlledRunnerFetchRecords = ControlledRunner<Results<WeatherResponse>>()

    fun requestLocation() {
        uiState = uiState.copy(locationRequired = true)
    }

    fun updateLocation(location: Location) {
        uiState = uiState.copy(
            currentLocation = LatLng(location.lat, location.lng),
            locationRequired = false
        )
    }

    fun getVisibleRadius(cameraPositionState: CameraPositionState): Double? {
        cameraPositionState.projection?.visibleRegion?.let { visibleRegion ->
            val distanceWidth = FloatArray(1)
            val distanceHeight = FloatArray(1)
            val farRight = visibleRegion.farRight
            val farLeft = visibleRegion.farLeft
            val nearRight = visibleRegion.nearRight
            val nearLeft = visibleRegion.nearLeft

            android.location.Location.distanceBetween(
                (farLeft.latitude + nearLeft.latitude) / 2,
                farLeft.longitude,
                (farRight.latitude + nearRight.latitude) / 2,
                farRight.longitude,
                distanceWidth
            )
            android.location.Location.distanceBetween(
                farRight.latitude,
                (farRight.longitude + farLeft.longitude) / 2,
                nearRight.latitude,
                (nearRight.longitude + nearLeft.longitude) / 2,
                distanceHeight
            )
            return sqrt(
                distanceWidth[0].toDouble().pow(2.0) + distanceHeight[0].toDouble().pow(2.0)
            ) / 2.0
        }
        return null
    }

    private fun addLog(log: String) {
        logDataList.add(LogData(log = log))
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
    val locationRequired: Boolean = false
)

data class LogData(val date: Date = Date(), val log: String)