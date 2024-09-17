package com.example.izvorlocator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.izvorlocator.app.IzvorLocatorApp
import com.example.izvorlocator.data.pois.PoiViewModel
import com.example.izvorlocator.data.pois.PoiViewModelFactory
import com.example.izvorlocator.data.pois.StorageService


import android.Manifest
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.izvorlocator.data.location.LocationService

class MainActivity : ComponentActivity() {
    private val poiViewModel: PoiViewModel by viewModels {
        PoiViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )
        // pokretanje LocationService
        val startLocationService = Intent(this, LocationService::class.java).apply {
            action = LocationService.ACTION_START
        }
        startService(startLocationService)

        setContent {
            IzvorLocatorApp(poiViewModel)
        }
    }
}