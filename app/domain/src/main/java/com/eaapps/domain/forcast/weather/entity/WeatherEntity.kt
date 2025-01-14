package com.eaapps.domain.forcast.weather.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class WeatherEntity(
    val location: LocationEntity,
    val current: CurrentEntity,
    val forecastDay: List<ForecastDayEntity>,
)

data class LocationEntity(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
)

data class CurrentEntity(
    val lastUpdatedEpoch: Long,
    val lastUpdated: String,
    val tempC: Double,
    val tempF: Double,
    val humidity: Int,
    val windMph: Double,
    val windKph: Double,
    val windDegree: Double,
    val windDirection: String,
    val condition: ConditionEntity,

    )

@Parcelize
data class ConditionEntity(
    val title: String,
    val icon: String,
    val code: Int,
) : Parcelable


@Parcelize
data class ForecastDayEntity(
    val id: Int,
    val date: String,
    val maxTempC: Int,
    val minTempC: Int,
    val maxTempF: Int,
    val minTempF: Int,
    val windMph: Double,
    val windKph: Double,
    val humidity: Int,
    val condition: ConditionEntity,
    val hour: List<HourDayEntity>,

    ) : Parcelable

@Parcelize
data class HourDayEntity(
    val time: String,
    val condition: ConditionEntity,
    val tempC: Double,
    val tempF: Double,
) : Parcelable
