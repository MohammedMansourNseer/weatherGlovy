package com.eaapps.weather_glovy.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.domain.forcast.history.usecase.GetHistoryUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val getHistoryUsecase: GetHistoryUsecase) : ViewModel() {

    private val _historyUiState = MutableStateFlow(HistoryUiState())
    val historyUiState = _historyUiState.asStateFlow()

    init {
        getHistory()
    }

    private fun getHistory() {
        viewModelScope.launch {
            _historyUiState.getAndUpdate { it.copy(loading = true) }
            getHistoryUsecase().collectLatest { list ->
                _historyUiState.getAndUpdate {
                    it.copy(loading = false, historyList = list)
                }
            }
        }
    }

}

data class HistoryUiState(val loading: Boolean = false, val historyList: List<String>? = null)