package com.example.izvorlocator.screens.users

import android.net.Uri
import android.util.Log
import android.widget.Button
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.izvorlocator.R
import com.example.izvorlocator.app.AppRouter
import com.example.izvorlocator.app.Screen
import com.example.izvorlocator.components.ButtonComponent
import com.example.izvorlocator.components.ClickableLoginTextComponent
import com.example.izvorlocator.components.DividerTextComponent
import com.example.izvorlocator.components.HeadingTextComponent
import com.example.izvorlocator.components.PasswordTextFieldComponent
import com.example.izvorlocator.components.TextFieldComponent
import com.example.izvorlocator.data.register.RegisterUIEvent
import com.example.izvorlocator.data.register.RegisterViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.izvorlocator.components.CustomIndeterminateProgress
import com.example.izvorlocator.components.SizedButtonComponent
import java.io.File
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.AlertDialog
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.izvorlocator.ui.theme.Primary

@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel = viewModel()) {
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { registerViewModel.onImagePicked(it) }
        Log.d("proba", "uspesno odabrano!!!! ${uri.toString()}")
    }
    val imageUri = registerViewModel.imageUri.value
    val isLoading by remember { registerViewModel.isLoading }
    var isShowingPickerDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // privremeno se cuva slika napravljena kamerom
    val tempImageUri = remember {
        val timestamp = System.currentTimeMillis()
        val tempFile = File.createTempFile("temp_image_$timestamp}", ".jpg", context.cacheDir).apply { deleteOnExit() }
        FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)
    }
    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            Log.d("proba", "uspesno slikano! -- ${tempImageUri.toString()}")
            registerViewModel.onImagePicked(tempImageUri)
        }
    }

    Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeadingTextComponent(value = stringResource(id = R.string.register_naslov))
                Spacer(modifier = Modifier.height(20.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Image(
                            painter = rememberImagePainter(
                                data = imageUri ?: R.drawable.profile_photo
                            ),
                            contentDescription = "Profile Photo",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(40.dp))
                        SizedButtonComponent(value = "Odaberi sliku",
                            onButtonClicked = { isShowingPickerDialog = true },
                            width = 150.dp)
                    }
                }
                TextFieldComponent(
                    labelValue = stringResource(id = R.string.name),
                    imageVector = Icons.Default.Person,
                    onTextChanged = {
                        registerViewModel.onEvent(RegisterUIEvent.FirstnameChanged(it))
                    },
                    errorStatus = registerViewModel.registerUIState.value.firstnameError)
                TextFieldComponent(
                    labelValue = stringResource(id = R.string.surname),
                    imageVector = Icons.Default.Person,
                    onTextChanged = {
                        registerViewModel.onEvent(RegisterUIEvent.LastnameChanged(it))
                    },
                    errorStatus = registerViewModel.registerUIState.value.lastnameError)
                TextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    imageVector = Icons.Default.Email,
                    onTextChanged = {
                        registerViewModel.onEvent(RegisterUIEvent.EmailChanged(it))
                    },
                    errorStatus = registerViewModel.registerUIState.value.emailError)
                TextFieldComponent(
                    labelValue = stringResource(id = R.string.phone),
                    imageVector = Icons.Default.Phone,
                    onTextChanged = {
                        registerViewModel.onEvent(RegisterUIEvent.PhoneChanged(it))
                    },
                    errorStatus = registerViewModel.registerUIState.value.phoneError)
                PasswordTextFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    imageVector = Icons.Default.Lock,
                    onTextChanged = {
                        registerViewModel.onEvent(RegisterUIEvent.PasswordChanged(it))
                    },
                    errorStatus = registerViewModel.registerUIState.value.passwordError)
                Spacer(modifier = Modifier.height(20.dp))
                ButtonComponent(value = stringResource(id = R.string.register),
                    onButtonClicked = {
                        registerViewModel.onEvent(RegisterUIEvent.RegisterButtonClicked)
                    },
                    isEnabled = registerViewModel.allValidationsPassed.value
                )
                Spacer(modifier = Modifier.height(20.dp))
                DividerTextComponent()
                Spacer(modifier = Modifier.height(10.dp))
                ClickableLoginTextComponent(
                    question = stringResource(id = R.string.imas)+" "
                    , text = stringResource(id = R.string.login)+"!"
                    , onTextSelected = {
                        AppRouter.navigateTo(Screen.LoginScreen)
                    })

            }
        }
    if(isLoading) {
        CustomIndeterminateProgress(modifier = Modifier)
    }
    if(isShowingPickerDialog) {
        AlertDialog(
            onDismissRequest = { isShowingPickerDialog = false },
            title = {
                Text(
                    text = "Odaberi sliku",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            buttons = {
                Column (
                    modifier = Modifier.padding(16.dp)
                ){
                    TextButton(onClick = {
                        imagePickerLauncher.launch("image/*")
                        isShowingPickerDialog = false },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text("Otvori galeriju",
                            color = Primary)
                    }
                    TextButton(onClick = {
                        takePictureLauncher.launch(tempImageUri)
                        isShowingPickerDialog = false
                    }) {
                        Text("Otvori kameru",
                            color = Primary)
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun DefaultPreviewOfSignUpScreen(){
    RegisterScreen()
}