package com.eaapps.weather_glovy.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.eaapps.weather_glovy.R
import com.eaapps.weather_glovy.navigation.AppRoute
import com.eaapps.weather_glovy.navigation.NavigationHostApp
import com.eaapps.weather_glovy.ui.theme.Weather_glovyTheme


@Composable
fun MainScreen(navController: NavHostController) {
    val configuration = LocalConfiguration.current
    val list = arrayListOf(
        BottomMenuContentModel(
            stringResource(R.string.homeLabel), Icons.Rounded.Home, AppRoute.HomeRoute.route
        ),
        BottomMenuContentModel(
            stringResource(R.string.historyLabel), Icons.AutoMirrored.Rounded.List, AppRoute.HistoryRoute.route
        ),
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val primaryColor = MaterialTheme.colorScheme.primary

    NavigationSuiteScaffold(
        modifier = Modifier.fillMaxSize(),
        layoutType = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) NavigationSuiteType.NavigationRail else NavigationSuiteType.NavigationBar,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = Color.White,
        ),
        navigationSuiteItems = {
            list.forEach { item ->
                item(
                    icon = { Icon(item.icon, contentDescription = null) },
                    label = { Text(item.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold) },
                    selected = currentRoute == item.route,
                    colors = NavigationSuiteItemColors(
                        navigationBarItemColors = NavigationBarItemColors(
                            selectedIconColor = primaryColor,
                            selectedTextColor = primaryColor,
                            selectedIndicatorColor = Color.Transparent,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            disabledIconColor = Color.Gray,
                            disabledTextColor = Color.Gray
                        ),
                        navigationRailItemColors = NavigationRailItemColors(
                            selectedIconColor = primaryColor,
                            selectedTextColor = primaryColor,
                            selectedIndicatorColor = Color.Transparent,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            disabledIconColor = Color.Gray,
                            disabledTextColor = Color.Gray
                        ),
                        navigationDrawerItemColors = object : NavigationDrawerItemColors {
                            @Composable
                            override fun badgeColor(selected: Boolean): State<Color> {
                                return remember(selected) { mutableStateOf(Color.Transparent) }

                            }

                            @Composable
                            override fun containerColor(selected: Boolean): State<Color> {
                                return remember(selected) { mutableStateOf(Color.Transparent) }
                            }

                            @Composable
                            override fun iconColor(selected: Boolean): State<Color> {
                                return remember(selected) { mutableStateOf(Color.Transparent) }
                            }

                            @Composable
                            override fun textColor(selected: Boolean): State<Color> {
                                return remember(selected) { mutableStateOf(Color.Transparent) }
                            }
                        }
                    ),
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }

                )
            }

        },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavigationHostApp(navController)
        }
    }
}

@Composable
@Preview
private fun PreviewMainScreen() {
    Weather_glovyTheme {
        MainScreen(navController = rememberNavController())
    }
}