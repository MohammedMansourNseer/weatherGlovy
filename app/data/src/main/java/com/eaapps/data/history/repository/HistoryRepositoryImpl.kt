package com.eaapps.data.history.repository

import com.eaapps.data.history.datasource.local.CacheDatasource
import com.eaapps.domain.forcast.history.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(private val cache: CacheDatasource) : HistoryRepository {
    override fun getHistoryForecast(): Flow<List<String>> = cache.getHistoryForecast()

    override suspend fun saveWeatherImageInHistoryUsecase(path: String) = cache.saveWeatherImageInHistory(path)
}