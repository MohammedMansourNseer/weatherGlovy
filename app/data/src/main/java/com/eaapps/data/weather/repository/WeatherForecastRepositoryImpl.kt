package com.eaapps.data.weather.repository

import com.eaapps.data.weather.datasource.remote.WeatherForecastRemoteDatasource
import com.eaapps.data.weather.datasource.remote.model.toEntity
import com.eaapps.domain.forcast.weather.entity.WeatherEntity
import com.eaapps.domain.forcast.weather.repository.WeatherForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherForecastRepositoryImpl @Inject constructor(private val remote: WeatherForecastRemoteDatasource) : WeatherForecastRepository {

    override suspend fun getForecast(latitude: Double, longitude: Double): WeatherEntity = withContext(Dispatchers.IO) {
        remote.getWeatherForecast(latitude, longitude).toEntity()
    }
}