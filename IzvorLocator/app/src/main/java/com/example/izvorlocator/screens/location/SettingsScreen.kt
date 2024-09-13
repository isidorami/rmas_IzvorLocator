package com.example.izvorlocator.screens.location

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.izvorlocator.components.ButtonComponent
import com.example.izvorlocator.data.location.LocationService
import com.example.izvorlocator.data.location.ServiceStateViewModel
import com.example.izvorlocator.ui.theme.*

@Composable
fun SettingsScreen(
    context: Context,
    startLocationService: Intent,
    stopLocationService: Intent,
    serviceStateViewModel: ServiceStateViewModel = viewModel()
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(28.dp)
    ) {
        val isServiceRunning by serviceStateViewModel.isServiceRunning.collectAsState()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Servis praćenja lokacije prati vašu trenutnu lokaciju koristeći GPS i metode bazirane na mreži. Aktiviranjem ove usluge omogućavamo kontinuirano praćenje vaše lokacije i pružanje relevantnih obaveštenja na osnovu vaše blizine drugim izvorima.",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            ButtonComponent(
                onButtonClicked = {
                    context.startService(startLocationService)
                    serviceStateViewModel.updateServiceState(true)
                },
                value = "Uključi praćenje lokacije",
                isEnabled = !isServiceRunning)

            Spacer(modifier = Modifier.height(16.dp))

            ButtonComponent(
                onButtonClicked = {
                    context.startService(stopLocationService)
                    serviceStateViewModel.updateServiceState(false)
                },
                value = "Isključi praćenje lokacije",
                isEnabled = isServiceRunning
            )
        }
    }
}