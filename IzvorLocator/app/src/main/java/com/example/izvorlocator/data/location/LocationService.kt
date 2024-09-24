package com.example.izvorlocator.data.location

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.izvorlocator.MainActivity
import com.example.izvorlocator.R
import com.example.izvorlocator.data.pois.Poi
import com.example.izvorlocator.data.pois.StorageServiceSingleton
import com.example.izvorlocator.data.user.UserViewModel
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
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

    private val storageService = StorageServiceSingleton.getInstance()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        startForegroundService()
    }

    private fun startForegroundService() {
        //Log.d("proba", "Starting foreground service")
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Praćenje lokacije...")
            .setContentText("Aplikacija u pozadini prati vašu lokaciju.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("proba", "onStartCommand called with action: ${intent?.action}")
        when (intent?.action) {
            ACTION_START -> {
                Log.d("proba", "Starting location tracking")
                startLocationTracking()
            }
            ACTION_STOP -> {
                Log.d("proba", "Stopping location tracking")
                stop()
            }
        }
        return START_STICKY
    }

    private fun startLocationTracking() {
        serviceScope.launch {
            locationClient.getLocationUpdates(1000L)
                .catch { e -> e.printStackTrace() }
                .onEach { location ->
                    val lat = location.latitude
                    val lng = location.longitude

                    //Log.d("proba", "TRENUTNA LOKACIJA")
                    LocationTracker.updateLocation(lat, lng)

                    if(LocationTracker.isServiceRunning.value) {
                        checkProximityToMarkers(lat, lng)
                    }
                }
                .launchIn(serviceScope)
        }
    }

    private fun checkProximityToMarkers(lat: Double, lng: Double) {
        //Log.d("proba", "usli u checkProximityToMarkers")
        serviceScope.launch {
            // Dohvati POI listu iz skladišta podataka
            storageService.pois
                .catch { e -> e.printStackTrace() }
                .collect { poiList ->
                    poiList.forEach { marker ->
                        val distance = calculateDistance(lat, lng, marker.lat, marker.lng)
                        if (distance < PROXIMITY_THRESHOLD) {
                            sendProximityNotification(marker)
                            if(distance==0f){
                                val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                                visitPoi(marker.id, uid)
                            }
                        }
                    }
                }
        }
    }

    private fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        return results[0]
    }

    private fun sendProximityNotification(marker: Poi) {
        //Log.d("proba", "usli u notification")

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
            .setContentText("Na lokaciji (${marker.lat}, ${marker.lng}) postoji izvor.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(false)
            .setContentIntent(pendingIntent) // otvori aplikaciju
            .build()

        // prikazi notifikaciju preko menadzera
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }

    private suspend fun visitPoi(id: String, visitor: String) {
        //Log.d("proba", "usli u visit poi!!!")
        val p : Poi? = storageService.getPoi(id)
        if(p!=null){
            if(p.korisnikId==visitor || p.visitors.contains(visitor)){
                //Log.d("proba", "Korisnik kreirao ili vec obisao lokaciju!")
                return
            }
            else {
                val totalVisitors = p.visitors + visitor
                Log.d("proba", "Dodali visitora: $totalVisitors")
                val p2 = Poi(
                    id = p.id,
                    pristupacnost = p.pristupacnost,
                    vrsta = p.vrsta,
                    kvalitet = p.kvalitet,
                    korisnikId = p.korisnikId,
                    korisnikImePrezime = p.korisnikImePrezime,
                    lat = p.lat,
                    lng = p.lng,
                    visitors = totalVisitors
                )
                storageService.update(p2)
                // ako prvi put obilazi lokaciju dobija poene, posle toga ne
                UserViewModel.addPointsToUser(50)
            }
        }
    }

    private fun stop() {
        Log.d("proba", "USLI U STOP!")
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
        const val PROXIMITY_THRESHOLD = 50f // 20 metara
    }
}