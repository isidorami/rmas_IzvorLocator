package com.example.izvorlocator.screens.location

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.*
import com.example.izvorlocator.data.pois.Poi
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.izvorlocator.R
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import com.example.izvorlocator.data.location.LocationTracker
import com.example.izvorlocator.ui.theme.Primary

@Composable
fun MapScreen(
    addPoi: (LatLng) -> Unit,
    list: List<Poi>,
    navigateToViewPoi: () -> Unit,
    setSelectedPoi: (Poi) -> Unit,
) {
    // Praćenje trenutne lokacije korisnika iz LocationTracker
    val userLocation by LocationTracker.currentLocation.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(43.321445, 21.896104), 15f)
    }
    userLocation?.let {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
    }
    Scaffold(
        floatingActionButton = {
                FloatingActionButton(onClick = {
                    userLocation?.let { latLng -> addPoi(latLng) }
                },
                    containerColor = Primary,
                    contentColor = Color.White) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
            ) {
                list.forEach { poi ->
                    Marker(
                        state = MarkerState(position = LatLng(poi.lat, poi.lng)),
                        title = "",
                        onClick = {
                            setSelectedPoi(poi)
                            navigateToViewPoi()
                            true
                        }
                    )
                }

                userLocation?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "Moja lokacija",
                        icon = getScaledBitmapDescriptor(R.drawable.ic_user_location, LocalContext.current, 48)
                    )
                }
            }
        }
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun getScaledBitmapDescriptor(drawableResId: Int, context: Context, sizeDp: Int): BitmapDescriptor {
    val drawable: Drawable = context.getDrawable(drawableResId)!!

    val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap

    val scale = context.resources.displayMetrics.density
    val sizePx = (sizeDp * scale + 0.5f).toInt()
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, sizePx, sizePx, false)

    return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
}