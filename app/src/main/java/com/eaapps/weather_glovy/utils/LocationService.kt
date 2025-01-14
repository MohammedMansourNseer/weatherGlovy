@file:Suppress("DEPRECATION")

package com.eaapps.weather_glovy.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationService(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(onError: (String) -> Unit = {}): Location? = withContext(Dispatchers.IO) {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnFailureListener {
                onError(it.message ?: "unknown error")
            }.result
    }

    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(onError: (String) -> Unit = {}): Location? = suspendCoroutine { cont ->
        fusedLocationClient.lastLocation
            .addOnFailureListener {
                onError(it.message ?: "unknown error")
                cont.resume(null)
            }
            .addOnCompleteListener {
                cont.resume(it.result)
            }
    }


    @SuppressLint("MissingPermission")
    suspend fun getAddressFromLocation(location: Location, locale: Locale? = null): String? = withContext(Dispatchers.IO) {
        val geocoder = Geocoder(context, locale ?: Locale.getDefault())
        kotlin.runCatching {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                return@runCatching addresses.first().let {
                    buildString {
                         if (!it.locality.isNullOrEmpty()) {
                            append(it.locality)
                             append(" - ")
                         }
                        append(it.adminArea)

                    }
                 }
            }
            null
        }.getOrNull()
    }

    private fun createLocationRequest() = LocationRequest.Builder(10000)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .setMinUpdateIntervalMillis(5000).build()

    @SuppressLint("MissingPermission")
    fun flowLocationWhenUpdate(): Flow<Location?> = callbackFlow {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                trySend(result.lastLocation).isSuccess
            }
        }
        fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, Looper.getMainLooper()).addOnFailureListener {
            close(it)
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}