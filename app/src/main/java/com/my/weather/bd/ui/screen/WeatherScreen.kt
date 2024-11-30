package com.my.weather.bd.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.my.weather.bd.R
import com.my.weather.bd.data.ServerConstants
import com.my.weather.bd.datamodel.ext.toCelsius


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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
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
            modifier = Modifier.size(140.dp)
        )

        val tempCelsius = stringResource(R.string.celsius)

        // City Name and Temperature
        Text(
            text = cityName,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "${temperature.toCelsius()}$tempCelsius",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            WeatherDetailItem(
                label = stringResource(R.string.feels_like),
                value = "${feelsLike.toCelsius()}$tempCelsius"
            )
            WeatherDetailItem(
                label = stringResource(R.string.pressure),
                value = pressure.toString()
            )
            WeatherDetailItem(
                label = stringResource(R.string.humidity),
                value = humidity.toString()
            )
        }
    }
}

@Composable
fun WeatherDetailItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.padding(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}