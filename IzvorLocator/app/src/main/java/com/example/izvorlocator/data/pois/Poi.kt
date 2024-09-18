package com.example.izvorlocator.data.pois

import android.net.Uri
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Poi(
    @DocumentId val id: String = "",
    @ServerTimestamp val createdAt: Date = Date(),
    val korisnikId: String = "", //ko je kreirao poi
    val korisnikImePrezime: String = "",
    val vrsta: String = "", //česma ili prirodni
    val kvalitet: String = "", //pijaća ili tehnička voda
    val pristupacnost: String = "", //tekstualno
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val visitors: List<String> = emptyList<String>() //pamti u bazi koji korisnici su ga obisli
)