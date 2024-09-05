package com.example.izvorlocator.screens

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
import com.example.izvorlocator.components.ButtonComponent
import com.example.izvorlocator.components.HeadingTextComponent
import com.example.izvorlocator.components.NormalTextComponent
import com.example.izvorlocator.components.TextFieldComponent
import com.example.izvorlocator.data.forgot.ForgotUIEvent
import com.example.izvorlocator.data.forgot.ForgotViewModel

@Composable
fun ForgotPasswordScreen(forgotViewModel: ForgotViewModel = viewModel()){
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            NormalTextComponent(value = stringResource(id = R.string.greeting))
            HeadingTextComponent(value = stringResource(id = R.string.forgot_password_naslov))
            Spacer(modifier = Modifier.height(40.dp))
            TextFieldComponent(
                labelValue = stringResource(id = R.string.email),
                painterResource(id = R.drawable.email),
                onTextChanged = {
                    forgotViewModel.onEvent(ForgotUIEvent.EmailChanged(it))
                },
                errorStatus = forgotViewModel.forgotUIState.value.emailError
            )
            Spacer(modifier = Modifier.height(80.dp))
            ButtonComponent(value = stringResource(id = R.string.forgot),
                onButtonClicked = {
                    forgotViewModel.onEvent(ForgotUIEvent.ForgotButtonClicked)
                },
                isEnabled = forgotViewModel.allValidationsPassed.value
            )
        }
    }
}

@Preview
@Composable
fun DefaultPreviewOfForgotPasswordScreen(){
    ForgotPasswordScreen()
}