package com.eaapps.weather_glovy.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.domain.forcast.core.UsecaseResult
import com.eaapps.domain.forcast.history.usecase.SaveWeatherImageInHistoryUsecase
import com.eaapps.domain.forcast.weather.entity.WeatherEntity
import com.eaapps.domain.forcast.weather.usecase.GetWeatherForecastUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherForecastUsecase: GetWeatherForecastUsecase,
    private val saveWeatherImageInHistoryUsecase: SaveWeatherImageInHistoryUsecase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun getWeatherForecast(latitude: Double, longitude: Double) {
        _uiState.getAndUpdate { it.copy(loading = true, failureMessage = null, weatherForecast = null) }
        viewModelScope.launch {
            getWeatherForecastUsecase(latitude, longitude).apply {
                when (this) {
                    is UsecaseResult.Error -> _uiState.getAndUpdate { it.copy(loading = false, failureMessage = message) }
                    is UsecaseResult.Success -> _uiState.getAndUpdate { it.copy(loading = false, weatherForecast = data) }
                }
            }
        }
    }

    fun saveWeatherImageInHistory(path: String) {
        viewModelScope.launch {
            saveWeatherImageInHistoryUsecase(path)
        }
    }
}

data class HomeUiState(val loading: Boolean = false, val weatherForecast: WeatherEntity? = null, val failureMessage: String? = null)