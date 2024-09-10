package com.example.izvorlocator.screens.maps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.izvorlocator.R
import com.example.izvorlocator.components.ButtonComponent
import com.example.izvorlocator.components.NormalTextComponent
import com.example.izvorlocator.data.maps.PoiViewModel
import com.example.izvorlocator.data.maps.Poi

@Composable
fun ViewPoiScreen(poiViewModel: PoiViewModel, navigateBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        PoiCard(poi = poiViewModel.selectedPoi)
        Row {
            ButtonComponent(
                value = stringResource(R.string.nazad),
                onButtonClicked = {
                    navigateBack()
                },
                isEnabled = true)
            ButtonComponent(
                value = stringResource(R.string.obrisi_marker),
                onButtonClicked = {
                    poiViewModel.deletePoi(poiViewModel.selectedPoi.id)
                    navigateBack()
                },
                isEnabled = true)
        }
    }
}

@Composable
fun PoiCard(poi: Poi) {
    ElevatedCard(modifier = Modifier
        .padding(12.dp)
        .fillMaxWidth()) {
        Column() {
            NormalTextComponent(value = poi.name)
            Text(modifier = Modifier.padding(6.dp), text = "Address: ${poi.address}")
            Row(modifier = Modifier.padding(6.dp)) {
                Text(text = "Latitude:")
                Text(text = poi.lat.toString())
            }
            Row(modifier = Modifier.padding(6.dp)) {
                Text(text = "Longitude:")
                Text(text = poi.lng.toString())
            }
        }

    }
}