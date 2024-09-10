package com.example.izvorlocator.screens.maps

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.izvorlocator.data.maps.Poi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    onMapLongClick:(LatLng)->Unit,
    list: List<Poi>,
    navigateToViewPoi: () -> Unit,
    setSelectedPoi: (Poi) -> Unit
) {
    val nis = LatLng(43.321445, 21.896104)
    /*TODO: pracenje realtime lokacije ovde*/
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(nis, 20f)
    }

    val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }
    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
        onMapLongClick = {latLng -> onMapLongClick(latLng)}
    ){
        list.forEach { poi ->
            Marker(
                state = MarkerState(position = LatLng(poi.lat,poi.lng)),
                title = poi.name,
                onClick = {
                    setSelectedPoi(poi)
                    navigateToViewPoi()
                    true
                }
            )
        }
    }
}