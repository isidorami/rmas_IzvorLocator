package com.example.izvorlocator

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore

class PoiApplication: Application() {
    val db by lazy {Firebase.firestore}
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}