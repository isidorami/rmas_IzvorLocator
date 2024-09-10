package com.example.izvorlocator.data.maps

import android.net.Uri
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Poi(
    @DocumentId val id: String = "",
    @ServerTimestamp val createdAt: Date = Date(),
    val naziv: String = "",
    val dostupnost: String = "", //tekstualno
    val vrsta: String = "", //česma ili prirodni
    val kvalitet: String = "",//pijaća, tehnička, zagađena voda
    val slika: List<Uri>? = null,
    val lat: Double = 0.0,
    val lng: Double = 0.0
)