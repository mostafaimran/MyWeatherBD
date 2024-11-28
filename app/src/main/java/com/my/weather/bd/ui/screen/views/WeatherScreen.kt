package com.my.weather.bd.ui.screen.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.google.android.gms.maps.model.LatLng
import com.my.weather.bd.R
import com.my.weather.bd.data.ServerConstants
import com.my.weather.bd.datamodel.ext.getCurrentLocation
import com.my.weather.bd.datamodel.ext.toCelsius
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

@Composable
fun WeatherScreen(
    cityName: String,
    temperature: Double,
    description: String,
    weatherIcon: String?,
    pressure: Int,
    feelsLike: Double,
    humidity: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text("Search Location") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            shape = RoundedCornerShape(24.dp),
            singleLine = true
        )

        val iconUrl =
            "${ServerConstants.ICON_URL_PREFIX}$weatherIcon${ServerConstants.ICON_URL_SUFFIX}"

        // Weather Icon
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(iconUrl)
                .crossfade(true)
                .build(),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(100.dp)
        )

        val tempCelsius = stringResource(R.string.celsius)

        // City Name and Temperature
        Text(
            text = cityName,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "${temperature.toCelsius()}$tempCelsius",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            WeatherDetailItem(label = "Feels Like", value = "${feelsLike.toCelsius()}$tempCelsius")
            WeatherDetailItem(label = "Pressure", value = pressure.toString())
            WeatherDetailItem(label = "Humidity", value = humidity.toString())
        }
    }
}

@Composable
fun WeatherDetailItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}