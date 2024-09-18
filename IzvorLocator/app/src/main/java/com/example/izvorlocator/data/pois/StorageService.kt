package com.example.izvorlocator.data.pois

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object StorageServiceSingleton {
    private var instance: StorageService? = null

    // Function to initialize the singleton instance
    fun getInstance(): StorageService {
        if (instance == null) {
            instance = StorageService(FirebaseFirestore.getInstance())
        }
        return instance!!
    }
}

class StorageService(private val firestore: FirebaseFirestore){

    val pois: Flow<List<Poi>>
        get() =
            firestore
                .collection(POI_COLLECTION)
                .orderBy(CREATED_AT_FIELD, Query.Direction.DESCENDING)
                .dataObjects()

    suspend fun getPoi(poiId: String): Poi? =
        firestore.collection(POI_COLLECTION).document(poiId).get().await().toObject()

    suspend fun save(poi: Poi): String {
        val updatedPoi = poi.copy()
        return firestore.collection(POI_COLLECTION).add(updatedPoi).await().id
    }

    suspend fun update(poi: Poi): Void? =
        firestore.collection(POI_COLLECTION).document(poi.id).set(poi).await()

    suspend fun delete(poiId: String) {
        firestore.collection(POI_COLLECTION).document(poiId).delete().await()
    }

    companion object {
        private const val CREATED_AT_FIELD = "createdAt"
        private const val POI_COLLECTION = "pois"
    }
}