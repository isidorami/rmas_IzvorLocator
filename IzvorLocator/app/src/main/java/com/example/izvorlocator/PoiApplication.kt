package com.example.izvorlocator

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class PoiApplication: Application() {
    val db by lazy {Firebase.firestore}
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        // notifikacije -- za lokaciju
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}