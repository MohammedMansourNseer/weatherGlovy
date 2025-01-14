package com.eaapps.data.weather.datasource.remote.service

import com.eaapps.data.weather.datasource.remote.model.WeatherForecastResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("/v1/forecast.json")
    suspend fun getWeatherForecast(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int,
    ): WeatherForecastResponseDto

}