package com.eaapps.data.history.datasource.local

import kotlinx.coroutines.flow.Flow

interface CacheDatasource {

    fun getHistoryForecast(): Flow<List<String>>

    suspend fun saveWeatherImageInHistory(path:String)

}