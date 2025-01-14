package com.eaapps.weather_glovy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eaapps.weather_glovy.feature.history.HistoryScreen
import com.eaapps.weather_glovy.feature.home.HomeScreen


@Composable
fun NavigationHostApp(navHostController: NavHostController = rememberNavController()) {
    NavHost(navController = navHostController, startDestination = AppRoute.HomeRoute.route) {
        composable(route = AppRoute.HomeRoute.route) {
            HomeScreen()
        }

        composable(route = AppRoute.HistoryRoute.route) {
            HistoryScreen()
        }
    }


}
