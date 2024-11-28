package com.my.weather.bd.ui.screen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.my.weather.bd.R
import com.my.weather.bd.ui.screen.views.HomeScreen
import com.my.weather.bd.ui.theme.WeatherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyWeatherBDActivity : ComponentActivity() {
    private val viewModel: WeatherBDViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherTheme {
                HomeScreen(
                    onSearchClicked = {

                    },
                    onException = {
                        Toast.makeText(
                            this,
                            R.string.server_error_message,
                            Toast.LENGTH_LONG
                        ).show()

                        finish()
                    },
                    onPermissionDenied = {
                        Toast.makeText(
                            this,
                            R.string.location_permission_issue,
                            Toast.LENGTH_LONG
                        ).show()

                        finish()
                    }
                )
            }
        }
    }
}