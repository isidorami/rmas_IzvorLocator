package com.example.izvorlocator.data.pois

import android.net.Uri
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Poi(
    @DocumentId val id: String = "",
    @ServerTimestamp val createdAt: Date = Date(),
    var slike: List<Uri>? = null, //nekoliko
    val vrsta: String = "", //česma ili prirodni
    val kvalitet: String = "",//pijaća ili tehnička voda
    val pristupacnost: String = "", //tekstualno
    val lat: Double = 0.0,
    val lng: Double = 0.0
)