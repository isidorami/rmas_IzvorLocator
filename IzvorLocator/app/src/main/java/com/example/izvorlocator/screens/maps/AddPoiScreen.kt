package com.example.izvorlocator.screens.maps

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.izvorlocator.R
import com.example.izvorlocator.app.AppRouter
import com.example.izvorlocator.components.*
import com.example.izvorlocator.data.pois.EditViewModel
import com.example.izvorlocator.data.pois.PoiViewModel
import com.example.izvorlocator.data.user.UserViewModel
import com.example.izvorlocator.ui.theme.Background

@SuppressLint("DefaultLocale")
@Composable
fun AddPoiScreen(poiViewModel: PoiViewModel,
                 editViewModel: EditViewModel = viewModel(),
                 userViewModel: UserViewModel = viewModel()) {

    // Auto-reset ViewModel when this Composable is disposed (e.g. on back press)
    DisposableEffect(Unit) {
        onDispose {
            editViewModel.reset()
        }
    }

    val multipleImagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        editViewModel.onImagesPicked(uris)
    }
    val imageUris = editViewModel.slike

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background)
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ){
            ImageSwitcher(imageUris = imageUris)
            Spacer(modifier = Modifier.height(8.dp))
            SizedButtonComponent(
                value = "Odaberi slike",
                onButtonClicked = { multipleImagePickerLauncher.launch("image/*") },
                width = 150.dp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row{
            Text(
                text = "Latituda:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = String.format("%.5f", editViewModel.lat),
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
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = String.format("%.5f", editViewModel.lng),
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        var selectedOptionIndex by remember { mutableStateOf(0) }
        val options = listOf("Pijaća", "Tehnička")
        editViewModel.kvalitet = options[selectedOptionIndex]
        RadioButtonGroup(
            label = "Kvalitet vode",
            options = options,
            selectedOptionIndex = selectedOptionIndex,
            onOptionSelected = { index ->
                run {
                    selectedOptionIndex = index
                    editViewModel.kvalitet = options[selectedOptionIndex]
                }
            }
        )
        var selectedOptionIndex2 by remember { mutableStateOf(0) }
        val options2 = listOf("Česma", "Prirodni")
        editViewModel.vrsta = options2[selectedOptionIndex2]
        RadioButtonGroup(
            label = "Vrsta izvora",
            options = options2,
            selectedOptionIndex = selectedOptionIndex2,
            onOptionSelected = { index ->
                run {
                    selectedOptionIndex2 = index
                    editViewModel.vrsta = options2[selectedOptionIndex2]
                }
            }
        )
        TextField2Component(
            labelValue = "Pristupačnost izvora",
            onTextChanged = {editViewModel.pristupacnost = it
                editViewModel.pristupacnostError = (it=="") },
            isError = editViewModel.pristupacnostError,
            value = editViewModel.pristupacnost)
        Spacer(modifier = Modifier.height(8.dp))
            ButtonComponent(
                value = stringResource(R.string.dodaj_marker),
                onButtonClicked = {
                    poiViewModel.addPoi(
                        pristupacnost = editViewModel.pristupacnost,
                        vrsta = editViewModel.vrsta,
                        kvalitet = editViewModel.kvalitet,
                        slike = editViewModel.slike,
                        lat = editViewModel.lat,
                        lng = editViewModel.lng
                    )
                    userViewModel.addPointsToUser(30)
                    editViewModel.reset()
                    AppRouter.popBackStack()
                },
                isEnabled = true)
    }
}