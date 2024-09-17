package com.example.izvorlocator.screens.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.izvorlocator.components.ButtonComponent
import com.example.izvorlocator.data.location.LocationTracker
import com.example.izvorlocator.ui.theme.*

@Composable
fun SettingsScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(28.dp)
    ) {
        val isServiceRunning by LocationTracker.isServiceRunning.collectAsState()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Naša usluga prati vašu trenutnu lokaciju koristeći GPS i mrežne metode kako bi vam pružila obaveštenja o blizini drugih izvora. Ovo omogućava da stalno dobijate relevantne informacije i obaveštenja u vezi sa izvorima koji su blizu vašeg trenutnog položaja.",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            ButtonComponent(
                onButtonClicked = {
                    LocationTracker.updateServiceState(true)
                },
                value = "Uključi primanje obaveštenja",
                isEnabled = !isServiceRunning)

            Spacer(modifier = Modifier.height(16.dp))

            ButtonComponent(
                onButtonClicked = {
                    LocationTracker.updateServiceState(false)
                },
                value = "Isključi primanje obaveštenja",
                isEnabled = isServiceRunning
            )
        }
    }
}