package com.eaapps.domain.forcast.history.repository

import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun getHistoryForecast(): Flow<List<String>>

    suspend fun saveWeatherImageInHistoryUsecase(path:String)

}