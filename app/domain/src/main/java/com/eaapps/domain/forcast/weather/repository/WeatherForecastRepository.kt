package com.eaapps.domain.forcast.weather.repository

import com.eaapps.domain.forcast.weather.entity.WeatherEntity

interface WeatherForecastRepository {

    suspend fun getForecast(latitude: Double, longitude: Double): WeatherEntity

}