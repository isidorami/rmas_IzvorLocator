package com.example.izvorlocator.data.maps

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class EditViewModel: ViewModel() {
    var pristupacnost: String by mutableStateOf("")
    var vrsta: String by mutableStateOf("")
    var kvalitet: String by mutableStateOf("")
    var slike: List<Uri> by mutableStateOf(emptyList())

    var lat: Double by mutableDoubleStateOf(0.0)
        private set
    var lng: Double by mutableDoubleStateOf(0.0)
        private set

    var pristupacnostError: Boolean = false

    fun dodajLatLng(latLng: LatLng){
        lat = latLng.latitude
        lng = latLng.longitude
    }

    fun reset() {
        pristupacnost = ""
        vrsta = ""
        kvalitet = ""
        slike = emptyList()
        lat = 0.0
        lng = 0.0

        pristupacnostError = true
    }
    fun onImagesPicked(uri: List<Uri>) {
        slike = uri
    }
}