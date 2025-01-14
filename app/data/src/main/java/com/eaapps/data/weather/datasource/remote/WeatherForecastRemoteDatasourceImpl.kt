package com.eaapps.data.weather.datasource.remote

import com.eaapps.data.BuildConfig
import com.eaapps.data.weather.datasource.remote.model.WeatherForecastResponseDto
import com.eaapps.data.weather.datasource.remote.service.WeatherApiService
import javax.inject.Inject

class WeatherForecastRemoteDatasourceImpl @Inject constructor(private val apiService: WeatherApiService) : WeatherForecastRemoteDatasource {
    override suspend fun getWeatherForecast(lat: Double, long: Double): WeatherForecastResponseDto =
        apiService.getWeatherForecast(BuildConfig.API_KEY, "$lat,$long", 7)


}

