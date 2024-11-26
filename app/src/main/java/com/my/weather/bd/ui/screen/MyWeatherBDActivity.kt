package com.my.weather.bd.ui.screen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.my.weather.bd.coreandroid.util.SingleEventObserver
import com.my.weather.bd.datamodel.ext.getErrorMessage
import com.my.weather.bd.ui.theme.TravelaPropertyListingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyWeatherBDActivity : ComponentActivity() {
    private val viewModel: WeatherBDViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelaPropertyListingTheme {
                /*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PropertyMapScreen(
                        viewModel,
                        innerPadding,
                        Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                        onNavigationBack = {
                            onBackPressedDispatcher.onBackPressed()
                        },
                        onPermissionDenied = {
                            Toast.makeText(
                                this,
                                getString(R.string.location_permission_issue),
                                Toast.LENGTH_LONG
                            ).show()

                            finish()
                        }
                    )
                }*/
            }
        }

        viewModel.lvApiIssue.observe(this, SingleEventObserver {
            Toast.makeText(
                this,
                it.getErrorMessage(this),
                Toast.LENGTH_LONG
            ).show()

        })

        viewModel.requestLocation()
    }
}