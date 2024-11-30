package com.my.weather.bd.ui.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.my.weather.bd.ui.viewmodel.WeatherBDViewModel

@Composable
fun AppNavigation(
    viewModel: WeatherBDViewModel = hiltViewModel(),
    onException: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onSearchClicked = { navController.navigate("search") },
                onException = onException,
                onPermissionDenied = onPermissionDenied
            )
        }
        composable("search") {
            SearchScreen(viewModel = viewModel, onBack = {
                navController.popBackStack()
            })
        }
    }
}