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
    var selectedPoiImages: List<Uri> by mutableStateOf(emptyList())
        private set

    fun setCurrentPoi(poi: Poi) {
        selectedPoi = poi
        fetchImagesFromFirebase(poi.id) { images ->
            selectedPoiImages = images
        }
    }

    private fun fetchImagesFromFirebase(poiId: String, onImagesFetched: (List<Uri>) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().getReference("Pois/$poiId")

        storageReference.listAll()
            .addOnSuccessListener { result ->
                val allFiles = result.items
                val imageUris = mutableListOf<Uri>()
                val tasks = mutableListOf<Task<Uri>>()

                allFiles.forEach { fileReference ->
                    val downloadUrlTask = fileReference.downloadUrl
                    tasks.add(downloadUrlTask)
                    downloadUrlTask.addOnSuccessListener { uri ->
                        imageUris.add(uri)
                        Log.d("proba", "Added image URL: $uri")
                    }.addOnFailureListener { exception ->
                        Log.d("proba", "Failed to get download URL for ${fileReference.name}: ${exception.message}")
                    }
                }

                Tasks.whenAllSuccess<Uri>(tasks).addOnCompleteListener {
                    Log.d("proba", "Successfully fetched ${imageUris.size} images")
                    onImagesFetched(imageUris)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("proba", "Failed to list files: ${exception.message}")
            }
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
            val id = storageService.save(p)
            uploadImagesToFirebase(id, slike)
        }
    }

    fun deletePoi(id: String) {
        viewModelScope.launch {
            storageService.delete(id)
            /*TODO: BRISANJE SLIKA*/
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
            storageService.update(p)
            uploadImagesToFirebase(p.id, slike) //dodajemo još slika, ne brišu se stare
            selectedPoiImages = slike
            selectedPoi = p
        }
    }

    private fun uploadImagesToFirebase(poiId: String, imageUris: List<Uri>) {
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReference("Pois").child(poiId)

        var successfulUploads = 0
        val totalUploads = imageUris.size

        fun onUploadComplete() {
            successfulUploads++
            if (successfulUploads == totalUploads) {
                Log.d("proba", "uspesno dodate sve slike!")
            }
        }

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