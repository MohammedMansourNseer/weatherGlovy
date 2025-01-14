package com.eaapps.domain.forcast.weather.usecase

import com.eaapps.domain.forcast.core.UsecaseResult
import com.eaapps.domain.forcast.weather.entity.WeatherEntity
import com.eaapps.domain.forcast.weather.repository.WeatherForecastRepository
import java.net.UnknownHostException
import javax.inject.Inject

class GetWeatherForecastUsecase @Inject constructor(private val repository: WeatherForecastRepository) {

    suspend operator fun invoke(latitude: Double, longitude: Double): UsecaseResult<out WeatherEntity> =
        kotlin.runCatching {
            UsecaseResult.Success(repository.getForecast(latitude, longitude))
        }.getOrElse {
            UsecaseResult.Error(if (it is UnknownHostException)
                "Please check your internet connection"
            else it.message ?: "unknown error")
        }

}

