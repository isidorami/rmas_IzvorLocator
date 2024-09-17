package com.example.izvorlocator.data.pois

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.izvorlocator.app.AppRouter
import com.example.izvorlocator.app.Screen
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PoiViewModel(private val storageService: StorageService): ViewModel() {
    var selectedPoi: Poi by mutableStateOf(Poi())
        private set

    fun setCurrentPoi(poi: Poi) {
        selectedPoi = poi
    }

    fun resetCurrentPoi() {
        selectedPoi = Poi()
    }

    val pois: Flow<List<Poi>> = storageService.pois

    fun addPoi(
        pristupacnost: String,
        vrsta: String,
        kvalitet: String,
        slike: List<Uri>,
        lat: Double,
        lng: Double
    ) {
        val p = Poi(
            pristupacnost = pristupacnost,
            vrsta = vrsta,
            kvalitet = kvalitet,
            lat = lat,
            lng = lng
        )
        viewModelScope.launch {
            storageService.save(p)
            uploadImagesToFirebase(slike)
        }
    }

    fun deletePoi(id: String) {
        viewModelScope.launch {
            storageService.delete(id)
        }
    }

    fun editPoi(pristupacnost: String, vrsta: String, kvalitet: String, slike: List<Uri>) {
        viewModelScope.launch {
            val p = Poi(
                id = selectedPoi.id,
                pristupacnost = pristupacnost,
                vrsta = vrsta,
                kvalitet = kvalitet,
                lat = selectedPoi.lat,
                lng = selectedPoi.lng
            )
            uploadImagesToFirebase(slike) //dodajemo još slika, ne brišu se stare
            selectedPoi = p
            storageService.update(p)
        }
    }

    private fun uploadImagesToFirebase(imageUris: List<Uri>) {
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReference("Pois/${selectedPoi.id}")

        // Use a Counter to Track the Number of Successful Uploads
        var successfulUploads = 0
        val totalUploads = imageUris.size

        // Function to handle upload completion
        fun onUploadComplete() {
            successfulUploads++
            if (successfulUploads == totalUploads) {
                // All images uploaded successfully
                AppRouter.navigateTo(Screen.LoginScreen)
            }
        }

        // Iterate over the list of image URIs
        imageUris.forEach { imageUri ->
            val imageReference = storageReference.child("image_${System.currentTimeMillis()}")

            imageReference.putFile(imageUri)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("proba", "IMAGE UPLOAD HERE -> ${task.isSuccessful}")
                        onUploadComplete()
                    } else {
                        Log.d("proba", "IMAGE NOT UPLOADED")
                        Log.d("proba", "${task.exception?.message}")
                    }
                }
        }
    }
}

class PoiViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PoiViewModel::class.java)) {
            return PoiViewModel(StorageServiceSingleton.getInstance()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}