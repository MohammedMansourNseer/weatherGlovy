package com.eaapps.domain.forcast.core

sealed class UsecaseResult<T> {
    data class Success<T>(val data: T) : UsecaseResult<T>()

    data class Error(val message: String) : UsecaseResult<Nothing>()

}