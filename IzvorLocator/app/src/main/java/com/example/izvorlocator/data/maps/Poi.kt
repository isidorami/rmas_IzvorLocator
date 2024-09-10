package com.example.izvorlocator.data.maps

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Poi(
    @DocumentId val id: String = "",
    @ServerTimestamp val createdAt: Date = Date(),
    val name: String = "",
    val address: String = "",
    val lat: Double=0.0,
    val lng: Double= 0.0
)