package com.example.izvorlocator.data.location

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object LocationTracker {
    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    fun updateLocation(lat: Double, lng: Double) {
        _currentLocation.value = LatLng(lat, lng)
    }

    private val _isServiceRunning = MutableStateFlow(false)
    val isServiceRunning: StateFlow<Boolean> = _isServiceRunning

    fun updateServiceState(isRunning: Boolean) {
        _isServiceRunning.value = isRunning
    }
}