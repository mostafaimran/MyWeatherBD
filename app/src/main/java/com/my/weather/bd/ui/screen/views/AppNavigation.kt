package com.my.weather.bd.ui.screen.views

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(
    onException: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onSearchClicked = { navController.navigate("search") },
                onException = onException,
                onPermissionDenied = onPermissionDenied
            )
        }
        composable("search") { SearchScreen(onBack = { navController.popBackStack() }) }
    }
}