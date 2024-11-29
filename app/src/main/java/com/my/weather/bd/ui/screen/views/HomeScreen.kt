package com.my.weather.bd.ui.screen.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.LatLng
import com.my.weather.bd.R
import com.my.weather.bd.datamodel.ext.getCurrentLocation
import com.my.weather.bd.ui.screen.WeatherBDViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: WeatherBDViewModel = hiltViewModel(),
    onSearchClicked: () -> Unit,
    onException: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val uiState = viewModel.uiState
            val requestLocationPermission = uiState.locationPermissionRequired
            val weatherResponse = uiState.weatherResponse
            val currentLocation = uiState.currentLocation
            val exception = uiState.exception

            // Check and request location permission
            if (requestLocationPermission) {
                LocationPermissionScreen(
                    onLocationFound = { location ->
                        viewModel.updateLocationPermissionGranted()
                        viewModel.updateLocation(location)
                    }, permissionDenied = {
                        onPermissionDenied()
                    }
                )
            } else {
                if (currentLocation != null) {
                    if (exception != null) {
                        onException()
                    } else {
                        weatherResponse?.let { response ->
                            WeatherScreen(
                                cityName = response.name ?: "",
                                temperature = response.main.temp,
                                description = response.weather.firstOrNull()?.description ?: "",
                                weatherIcon = response.weather.firstOrNull()?.icon ?: "",
                                pressure = response.main.pressure,
                                feelsLike = response.main.feelsLike,
                                humidity = response.main.humidity
                            )
                        } ?: run {
                            viewModel.getWeatherByLocation(
                                currentLocation.latitude,
                                currentLocation.longitude
                            )

                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                } else {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                    LocalContext.current.getCurrentLocation { lat, long ->
                        viewModel.updateLocation(LatLng(lat, long))
                    }
                }
            }
        }
    }
}
