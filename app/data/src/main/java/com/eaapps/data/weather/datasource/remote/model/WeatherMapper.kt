package com.eaapps.data.weather.datasource.remote.model

import android.text.format.DateUtils.formatDateTime
import com.eaapps.data.extension.formatDateTime
import com.eaapps.data.extension.toDayOfWeek
import com.eaapps.domain.forcast.weather.entity.ConditionEntity
import com.eaapps.domain.forcast.weather.entity.CurrentEntity
import com.eaapps.domain.forcast.weather.entity.ForecastDayEntity
import com.eaapps.domain.forcast.weather.entity.HourDayEntity
import com.eaapps.domain.forcast.weather.entity.LocationEntity
import com.eaapps.domain.forcast.weather.entity.WeatherEntity


fun WeatherForecastResponseDto.toEntity() = WeatherEntity(
    location = location.toEntity(),
    current = current.toEntity(),
    forecastDay = forecast.forecastDay.mapIndexed { index, item ->
        ForecastDayEntity(id = index, date = item.date.toDayOfWeek("yyyy-MM-dd"),
            maxTempC = item.day.maxTempC.toInt(),
            minTempC = item.day.minTempC.toInt(),
            maxTempF = item.day.maxTempF.toInt(),
            minTempF = item.day.minTempF.toInt(),
            windMph = item.day.maxWindMph,
            windKph = item.day.maxWindKph,
            humidity = item.day.avghumidity,
            condition = item.day.condition.toEntity(),
            hour = item.hour.map { h ->
                HourDayEntity(
                    time = h.time.formatDateTime("yyyy-MM-dd HH:mm", "h a"), condition = h.condition.toEntity(), tempC = h.tempC, tempF = h.tempF
                )
            })
    },
)

fun Location.toEntity() = LocationEntity(
    name = name, region = region, country = country, lat = lat, lon = lon
)

fun Current.toEntity() = CurrentEntity(
    lastUpdatedEpoch = lastUpdatedEpoch,
    lastUpdated = lastUpdated,
    humidity = humidity,
    tempC = tempC,
    tempF = tempF,
    windMph = windMph,
    windKph = windKph,
    windDirection = windDir,
    windDegree = windDegree,
    condition = condition.toEntity()
)

fun Condition.toEntity() = ConditionEntity(
    title = text, icon = icon.replaceFirst("//", "https://"), code = code
)