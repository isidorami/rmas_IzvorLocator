package com.example.izvorlocator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.izvorlocator.app.IzvorLocatorApp
import com.example.izvorlocator.data.maps.PoiViewModel
import com.example.izvorlocator.data.maps.PoiViewModelFactory
import com.example.izvorlocator.data.maps.StorageService

class MainActivity : ComponentActivity() {
    private val poiViewModel: PoiViewModel by viewModels {
        PoiViewModelFactory(StorageService((application as PoiApplication).db))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IzvorLocatorApp(poiViewModel)
        }
    }
}