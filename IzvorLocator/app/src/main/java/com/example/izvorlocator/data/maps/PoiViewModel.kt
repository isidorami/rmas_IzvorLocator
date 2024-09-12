package com.example.izvorlocator.data.maps

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PoiViewModel(private val storageService: StorageService): ViewModel() {
    var selectedPoi: Poi by mutableStateOf(Poi())
        private set

    fun setCurrentPoi(poi: Poi) {
        selectedPoi = poi
    }

    fun resetCurrentPoi(){
        selectedPoi = Poi()
    }

    val pois: Flow<List<Poi>> = storageService.pois

    fun addPoi(pristupacnost: String, vrsta: String, kvalitet: String, slike: List<Uri>, lat: Double, lng: Double) {
        val p = Poi(
            pristupacnost = pristupacnost,
            vrsta = vrsta,
            kvalitet = kvalitet,
            lat = lat,
            lng = lng
        )
        viewModelScope.launch {
            storageService.save(p)
        }
    }

    fun deletePoi(id: String) {
        viewModelScope.launch {
            storageService.delete(id)
        }
    }

    fun editPoi(pristupacnost: String, vrsta: String, kvalitet: String, slika: List<Uri>, latLng: LatLng?){
        viewModelScope.launch {
            val p = Poi(
                id=selectedPoi.id,
                pristupacnost = pristupacnost,
                vrsta = vrsta,
                kvalitet = kvalitet
            )
            storageService.update(p)
        }
    }
}

class PoiViewModelFactory(private val storageService: StorageService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PoiViewModel::class.java)) {
            return PoiViewModel(storageService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}