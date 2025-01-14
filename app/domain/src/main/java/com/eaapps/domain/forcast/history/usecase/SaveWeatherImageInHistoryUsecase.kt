package com.eaapps.domain.forcast.history.usecase

import com.eaapps.domain.forcast.history.repository.HistoryRepository
import javax.inject.Inject

class SaveWeatherImageInHistoryUsecase @Inject constructor(private val repository: HistoryRepository) {
    suspend operator fun invoke(path: String) = repository.saveWeatherImageInHistoryUsecase(path)
}

