package com.example.izvorlocator.screens.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.izvorlocator.components.ButtonComponent
import com.example.izvorlocator.components.NormalTextComponent
import com.example.izvorlocator.data.maps.Poi

@Composable
fun ListScreen(list: List<Poi>, navigateToViewPoi: () -> Unit, setSelectedPoi: (Poi) -> Unit) {
    LazyColumn {
        items(list) {
            PoiPreview(poi = it, navigateToViewPoi, setSelectedPoi)
        }
    }
}

@Composable
fun PoiPreview(poi:Poi, navigateToViewPoi: () -> Unit, setSelectedPoi: (Poi) -> Unit){
    Card(modifier = Modifier
        .padding(12.dp)
        .fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.padding(6.dp),
                text = "${poi.naziv}"
            )
            Spacer(Modifier.weight(1f).fillMaxHeight())

            ButtonComponent(
                value = "Pogledaj",
                onButtonClicked = {
                    setSelectedPoi(poi)
                    navigateToViewPoi()
                }, isEnabled = true)
        }
    }
}