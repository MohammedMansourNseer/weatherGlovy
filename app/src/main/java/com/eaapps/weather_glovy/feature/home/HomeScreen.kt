@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)

package com.eaapps.weather_glovy.feature.home

import android.content.res.Configuration
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.eaapps.domain.forcast.weather.entity.ConditionEntity
import com.eaapps.domain.forcast.weather.entity.CurrentEntity
import com.eaapps.domain.forcast.weather.entity.ForecastDayEntity
import com.eaapps.domain.forcast.weather.entity.HourDayEntity
import com.eaapps.domain.forcast.weather.entity.LocationEntity
import com.eaapps.domain.forcast.weather.entity.WeatherEntity
import com.eaapps.weather_glovy.R
import com.eaapps.weather_glovy.ui.theme.Weather_glovyTheme
import com.eaapps.weather_glovy.utils.LocationService
import com.eaapps.weather_glovy.utils.drawTextOnBitmap
import com.eaapps.weather_glovy.utils.saveBitmapToFile
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import compose.icons.FeatherIcons
import compose.icons.feathericons.Camera
import kotlin.random.Random.Default.nextDouble

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val locationState = rememberPermissionState(permission = android.Manifest.permission.ACCESS_FINE_LOCATION)
    val cameraPermission = rememberPermissionState(permission = android.Manifest.permission.CAMERA)
    val locationService = LocationService(context)
    var currentLocation by rememberSaveable {
        mutableStateOf<Location?>(null)
    }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        val weatherData = uiState.value.weatherForecast
        weatherData?.apply {
            it?.drawTextOnBitmap(
                "${current.tempC}°" + "\n${
                    context.getString(
                        R.string.wind_km_h,
                        current.windKph.toString()
                    )
                }" + "\n${context.getString(R.string.wind_direction, current.windDirection)}" + "\n${
                    context.getString(
                        R.string.humidity_title,
                        current.humidity.toString()
                    )
                }"

            )?.saveBitmapToFile(context, System.currentTimeMillis().toString())?.apply {
                viewModel.saveWeatherImageInHistory(this)
            }
        }

    }
    var weatherDay by rememberSaveable { mutableStateOf(uiState.value.weatherForecast?.forecastDay?.firstOrNull()) }


    LaunchedEffect(locationState.status) {
        Log.e("TAG", "HomeScreen: 0000")

        if (!locationState.status.isGranted) {
            Log.e("TAG", "HomeScreen: 11111")

            locationState.launchPermissionRequest()
        } else {
            Log.e("TAG", "HomeScreen: 2222")

            currentLocation = locationService.getLastLocation {}
            Log.e("TAG", "HomeScreen: $currentLocation")

            if (currentLocation != null && uiState.value.weatherForecast == null) {
                Log.e("TAG", "HomeScreen: 33333")

                viewModel.getWeatherForecast(currentLocation!!.latitude, currentLocation!!.longitude)
            }
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.weather_app), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },

                )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (cameraPermission.status.isGranted) {
                    launcher.launch()
                } else {
                    cameraPermission.launchPermissionRequest()
                }
            }) {
                Icon(FeatherIcons.Camera, contentDescription = null)
            }
        },
        floatingActionButtonPosition = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) FabPosition.Center else FabPosition.End,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            when (configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    WeatherLandscapeUI(state = uiState.value, weatherDay, onSelectDay = {
                        weatherDay = it

                    }, onTryAgain = {
                        currentLocation?.apply {
                            viewModel.getWeatherForecast(latitude, longitude)
                        }
                    })
                }

                else -> {
                    WeatherPortraitUI(state = uiState.value, weatherDay, onSelectDay = {
                        weatherDay = it
                    }, onTryAgain = {
                        currentLocation?.apply {
                            viewModel.getWeatherForecast(latitude, longitude)
                        }
                    })
                }
            }
        }
    }

}

@Composable
private fun WeatherLandscapeUI(state: HomeUiState, weatherDay: ForecastDayEntity?, onSelectDay: (ForecastDayEntity) -> Unit, onTryAgain: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            WeatherPortraitUI(state, weatherDay, onSelectDay, onTryAgain)
        }
    }
}

@Composable
private fun WeatherPortraitUI(state: HomeUiState, weatherDay: ForecastDayEntity?, onSelectDay: (ForecastDayEntity) -> Unit, onTryAgain: () -> Unit) {
    if (state.loading) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
        return
    }
    if (state.failureMessage != null) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${state.failureMessage}")
                Button(onClick = onTryAgain, modifier = Modifier.padding(top = 16.dp)) {
                    Text(stringResource(R.string.try_again_button), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }
    state.weatherForecast?.let { weatherForecast ->
        Column(modifier = Modifier.fillMaxSize()) {
            weatherForecast.WeatherHeader()

            weatherDay?.let { selectedDay ->
                LazyRow(modifier = Modifier.padding(top = 16.dp)) {
                    items(selectedDay.hour, key = { it.time }) { hourEntity ->
                        hourEntity.ForecastHourItem()
                    }
                }
            }

            LazyRow(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .wrapContentHeight()
                    .padding(8.dp)
            ) {
                items(weatherForecast.forecastDay, key = { it.date }) { dayEntity ->
                    dayEntity.ForecastDayItem(currentForecast = weatherDay, onSelectDay = onSelectDay)
                }
            }
        }
    }
}

@Composable
private fun HourDayEntity.ForecastHourItem() {
    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(time, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
        AsyncImage(
            model = condition.icon,
            modifier = Modifier
                .padding(top = 24.dp)
                .size(32.dp),
            contentDescription = null,
        )
        Text("${tempC}°", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.W500)

    }
}

@Composable
private fun ForecastDayEntity.ForecastDayItem(currentForecast: ForecastDayEntity?, onSelectDay: (ForecastDayEntity) -> Unit) {
    Box(modifier = Modifier
        .padding(horizontal = 4.dp)
        .border(1.dp, Color.LightGray, RoundedCornerShape(10))
        .clip(RoundedCornerShape(10))
        .clickable {
            onSelectDay(this@ForecastDayItem)
        }) {
        Column(
            modifier = Modifier.padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(date, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
            AsyncImage(
                model = condition.icon,
                modifier = Modifier.size(24.dp),
                contentDescription = null,
            )
            Text("${maxTempC}°/${minTempC}°", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.W500)
            if (currentForecast?.id == id) Icon(
                Icons.Rounded.Check, contentDescription = null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary
            )
            else Spacer(modifier = Modifier.height(18.dp))
        }
    }

}

@Composable
private fun WeatherEntity.WeatherHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = .15f))
            .padding(bottom = 8.dp), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Weather", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp)
                )
                Text("${location.region},${location.country}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    Text(
                        stringResource(R.string.now_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.W500,
                    )
                    Row {
                        Text(
                            "${current.tempC}°",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        AsyncImage(
                            model = current.condition.icon,
                            modifier = Modifier.size(24.dp),
                            contentDescription = null,
                        )

                    }
                }

                Column {
                    Text(
                        stringResource(R.string.humidity_title, current.humidity),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.W500,
                    )
                    Text(
                        stringResource(R.string.wind_km_h, current.windKph),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                        stringResource(R.string.wind_direction, current.windDirection),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }


        }
    }
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
private fun PreviewHomeHeader() {
    val fakeData = WeatherEntity(
        location = LocationEntity(
            name = "Fake City",
            region = "Fake Region",
            country = "Fake Country",
            lat = nextDouble(-90.0, 90.0),
            lon = nextDouble(-180.0, 180.0),
        ),
        current = CurrentEntity(
            lastUpdatedEpoch = System.currentTimeMillis() / 1000,
            lastUpdated = "2024-03-08 12:00",
            tempC = nextDouble(-20.0, 40.0),
            tempF = nextDouble(-4.0, 104.0),
            humidity = 50,
            windMph = nextDouble(0.0, 50.0),
            windKph = nextDouble(0.0, 80.0),
            windDegree = nextDouble(0.0, 360.0),
            windDirection = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")[0],
            condition = ConditionEntity(
                title = "Fake Condition",
                icon = "fake_icon.png",
                code = 200,
            ),
        ),
        forecastDay = arrayListOf(),
    )
    Weather_glovyTheme {
        fakeData.WeatherHeader()
    }

}