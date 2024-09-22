package com.example.izvorlocator.screens.pois

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.izvorlocator.R
import com.example.izvorlocator.app.AppRouter
import com.example.izvorlocator.components.*
import com.example.izvorlocator.data.pois.EditViewModel
import com.example.izvorlocator.data.pois.PoiViewModel
import com.example.izvorlocator.data.user.UserViewModel
import com.example.izvorlocator.ui.theme.Background
import com.example.izvorlocator.ui.theme.Primary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.File

@SuppressLint("DefaultLocale")
@Composable
fun AddPoiScreen(poiViewModel: PoiViewModel,
                 editViewModel: EditViewModel = viewModel()) {

    DisposableEffect(Unit) {
        onDispose {
            editViewModel.reset()
        }
    }

    var userNameSurname by remember { mutableStateOf("") }
    val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    LaunchedEffect(Unit) {
        UserViewModel.fetchUserNameAndSurname(uid) { fetchedNameSurname ->
            userNameSurname = fetchedNameSurname
        }
    }

    val multipleImagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        editViewModel.onImagesPicked(uris)
        Log.d("proba", "stanje editViewModel -- ${editViewModel.slike}")
    }
    val imageUris = editViewModel.slike

    var isShowingPickerDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var currentTempImageUri by remember { mutableStateOf<Uri?>(null) } // Use a mutable state to hold the current URI

    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            Log.d("proba", "Uspesno slikano! -- ${currentTempImageUri.toString()}")
            editViewModel.onImagesPicked(listOf(currentTempImageUri!!))
        }
    }
    fun launchCamera() {
        val timestamp = System.currentTimeMillis()
        val tempFile = File.createTempFile("temp_image_$timestamp", ".jpg", context.cacheDir).apply { deleteOnExit() }
        currentTempImageUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)

        takePictureLauncher.launch(currentTempImageUri!!)
    }

    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background)
            .padding(18.dp)
            .clickable (
                onClick = { focusManager.clearFocus() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
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
            SizedButtonComponent(value = "Odaberi slike",
                onButtonClicked = { isShowingPickerDialog = true },
                width = 150.dp)
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
        PristupacnostTextFieldComponent(
            labelValue = "Pristupačnost izvora",
            onTextChanged = {editViewModel.pristupacnost = it
                editViewModel.pristupacnostError = (it.length<3) },
            isError = editViewModel.pristupacnostError,
            value = editViewModel.pristupacnost
        )
        Spacer(modifier = Modifier.height(8.dp))
        ButtonComponent(
            value = stringResource(R.string.dodaj_marker),
            onButtonClicked = {
                if(editViewModel.pristupacnost.isNotEmpty() && imageUris.isNotEmpty()) {
                    Log.d("proba", "snima se novi poi")
                    poiViewModel.addPoi(
                            pristupacnost = editViewModel.pristupacnost,
                            vrsta = editViewModel.vrsta,
                            kvalitet = editViewModel.kvalitet,
                            slike = editViewModel.slike,
                            korisnikId = uid,
                            korisnikImePrezime = userNameSurname,
                            lat = editViewModel.lat,
                            lng = editViewModel.lng
                    )
                    UserViewModel.addPointsToUser(30)
                    editViewModel.reset()
                    AppRouter.popBackStack()
                }},
            isEnabled = true)
    }
    if(isShowingPickerDialog) {
        androidx.compose.material.AlertDialog(
            onDismissRequest = { isShowingPickerDialog = false },
            title = {
                androidx.compose.material.Text(
                    text = "Odaberi slike",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            buttons = {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    androidx.compose.material.TextButton(
                        onClick = {
                            multipleImagePickerLauncher.launch("image/*")
                            isShowingPickerDialog = false
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        androidx.compose.material.Text(
                            "Otvori galeriju",
                            color = Primary
                        )
                    }
                    androidx.compose.material.TextButton(onClick = {
                        launchCamera()
                        isShowingPickerDialog = false
                    }) {
                        androidx.compose.material.Text(
                            "Otvori kameru",
                            color = Primary
                        )
                    }
                }
            }
        )
    }
}