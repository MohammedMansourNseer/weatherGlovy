package com.eaapps.data.history.datasource.local

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CacheDatasourceImpl @Inject constructor(private val sharedPreferences: SharedPreferences) : CacheDatasource {

    override fun getHistoryForecast(): Flow<List<String>> = callbackFlow {
        trySend(historyForecastList())
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "weather_image") {
                trySend(historyForecastList())
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    private fun historyForecastList(): List<String> {
        val old = sharedPreferences.getString("weather_image", "")
        return if (!old.isNullOrEmpty()) {
            val typeToken = object : TypeToken<List<String>>() {
            }.type
            Gson().fromJson<List<String>>(old, typeToken).toMutableList()

        } else {
            emptyList()
        }
    }

    override suspend fun saveWeatherImageInHistory(path: String) = withContext(Dispatchers.IO) {
        val currentList = historyForecastList().toMutableList()
        currentList.add(0, path)
        sharedPreferences.edit().putString("weather_image", Gson().toJson(currentList)).apply()
    }
}