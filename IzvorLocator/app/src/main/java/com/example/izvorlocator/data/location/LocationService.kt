package com.example.izvorlocator.data.location

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.izvorlocator.MainActivity
import com.example.izvorlocator.R
import com.example.izvorlocator.data.pois.Poi
import com.example.izvorlocator.data.pois.StorageServiceSingleton
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LocationService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    private val storageService = StorageServiceSingleton.getInstance() // Use the singleton

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START ->startLocationTracking()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startLocationTracking() {
        Log.d("proba", "usli u start tracking")
        serviceScope.launch {
            storageService.pois
                .catch { e -> e.printStackTrace() }
                .collect { poiList ->
                    locationClient.getLocationUpdates(10000L)
                        .catch { e -> e.printStackTrace() }
                        .onEach { location ->
                            val lat = location.latitude
                            val lng = location.longitude
                            checkProximityToMarkers(lat, lng, poiList)

                            broadcastLocation(lat, lng)
                        }
                        .launchIn(serviceScope)
                }
        }
    }

    private fun broadcastLocation(lat: Double, lng: Double) {
        val intent = Intent("LOCATION_UPDATE")
        intent.putExtra("latitude", lat)
        intent.putExtra("longitude", lng)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun checkProximityToMarkers(lat: Double, lng: Double, poiList: List<Poi>) {
        Log.d("proba", "usli u check")
        poiList.forEach { marker ->
            val distance = calculateDistance(lat, lng, marker.lat, marker.lng)
            if (distance < PROXIMITY_THRESHOLD) {
                sendProximityNotification(marker)
            }
        }
    }

    private fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        Log.d("proba", "usli u distance")
        val results = FloatArray(1)
        android.location.Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        return results[0]
    }

    private fun sendProximityNotification(marker: Poi) {
        Log.d("proba", "usli u notification")

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Izvor je u blizini!")
            .setContentText("Izvor na lokaciji (${marker.lat}, ${marker.lng})")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(false)
            .setContentIntent(pendingIntent) // otvori aplikaciju
            .setAutoCancel(true) // i skloni notifikaciju
            .build()

        // prikazi notifikaciju preko menadzera
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val PROXIMITY_THRESHOLD = 100f // 100 metara
    }
}