package com.example.izvorlocator.screens

import android.net.Uri
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
import androidx.compose.ui.res.painterResource
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.izvorlocator.ui.theme.Primary
import com.example.izvorlocator.ui.theme.Secondary

@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel = viewModel()) {
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { registerViewModel.onImagePicked(it) }
    }
    val imageUri = registerViewModel.imageUri.value

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
                        Button(
                            onClick = {
                                imagePickerLauncher.launch("image/*")
                            },
                            modifier = Modifier
                                .widthIn(140.dp)
                                .heightIn(48.dp),
                            contentPadding = PaddingValues(),
                            colors = ButtonDefaults.buttonColors(Color.Transparent),
                            enabled = true
                        ) {
                            Box(
                                modifier = Modifier
                                    .widthIn(140.dp)
                                    .heightIn(48.dp)
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            listOf(
                                                Secondary,
                                                Primary
                                            )
                                        ),
                                        shape = RoundedCornerShape(50.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Odaberi sliku",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
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
}

@Preview
@Composable
fun DefaultPreviewOfSignUpScreen(){
    RegisterScreen()
}