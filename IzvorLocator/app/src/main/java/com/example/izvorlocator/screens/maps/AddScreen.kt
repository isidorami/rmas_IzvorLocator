package com.example.izvorlocator.screens.maps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.izvorlocator.R
import com.example.izvorlocator.components.ButtonComponent
import com.example.izvorlocator.components.RadioButtonGroup
import com.example.izvorlocator.components.TextField2Component
import com.example.izvorlocator.components.TextFieldComponent
import com.example.izvorlocator.data.maps.EditViewModel
import com.example.izvorlocator.data.maps.PoiViewModel
import com.example.izvorlocator.data.register.RegisterUIEvent
import com.example.izvorlocator.screens.users.ProfileScreen

@Composable
fun AddPoiScreen(editViewModel: EditViewModel = viewModel(),
                 poiViewModel: PoiViewModel,
                 navigateToMap: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField2Component(
            labelValue = "Naziv",
            onTextChanged = { editViewModel.naziv = it })
        TextField2Component(
            labelValue = "Dostupnost izvora",
            onTextChanged = {editViewModel.dostupnost = it })

        var selectedOptionIndex by remember { mutableStateOf(0) }
        val options = listOf("Pijaća", "Tehnička", "Zagađena")

        RadioButtonGroup(
            label = "Kvalitet vode",
            options = options,
            selectedOptionIndex = selectedOptionIndex,
            onOptionSelected = { index -> selectedOptionIndex = index }
        )
        var selectedOptionIndex2 by remember { mutableStateOf(0) }
        val options2 = listOf("Česma", "Prirodni")
        RadioButtonGroup(
            label = "Vrsta izvora",
            options = options2,
            selectedOptionIndex = selectedOptionIndex2,
            onOptionSelected = { index -> selectedOptionIndex2 = index }
        )

        Row {
            Text(
                text = "Latituda:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = editViewModel.lat.toString(),
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Row {
            Text(
                text = "Longituda:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = editViewModel.lng.toString(),
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
            ButtonComponent(
                value = stringResource(R.string.nazad),
                onButtonClicked = {
                    navigateToMap()
                },
                isEnabled = true)
            ButtonComponent(
                value = stringResource(R.string.dodaj_marker),
                onButtonClicked = {
                    poiViewModel.addPoi(
                        editViewModel.naziv,
                        editViewModel.dostupnost,
                        editViewModel.kvalitet,
                        editViewModel.vrsta,
                        editViewModel.slika!!,
                        editViewModel.lat,
                        editViewModel.lng
                    )
                    editViewModel.reset()
                    navigateToMap()
                },
                isEnabled = true)
    }
}