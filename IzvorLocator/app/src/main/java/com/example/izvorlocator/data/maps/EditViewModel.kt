package com.example.izvorlocator.data.maps

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class EditViewModel: ViewModel() {
    var name: String by mutableStateOf("")
    var address:String by mutableStateOf("")

    var lat: Double by mutableStateOf(0.0)
        private set
    var lng: Double by mutableStateOf(0.0)
        private set
    //TODO: dodati funkciju za postavljanje vrednosti latitude i longitude

    fun dodajLatLng(latLng: LatLng){
        lat = latLng.latitude
        lng = latLng.longitude
    }

    fun reset() {
        name = ""
        address = ""
        lat = 0.0
        lng = 0.0
    }
}